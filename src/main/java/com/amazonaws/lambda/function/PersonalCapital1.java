package com.amazonaws.lambda.function;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.Request;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pc.exercise.model.PlanSearchRequest;
import com.pc.exercise.model.PlanSearchResponse;
import com.pc.exercise.exception.PlansValidationException;
import com.pc.exercise.model.LambdaProxyInput;
import com.pc.exercise.model.LambdaProxyOutput;
import com.pc.exercise.utility.ApplicationConstants;
import com.pc.exercise.utility.EsPropertiesEnum;

/**
 * This class acts as Lambda proxy class. 
 * 
 * @author 
 *
 */
public class PersonalCapital1 implements RequestHandler<LambdaProxyInput, LambdaProxyOutput> {
	 ElasticSearchClient esClient = new ElasticSearchClient();
	 
	 @Override
	 public LambdaProxyOutput handleRequest(LambdaProxyInput lambdaInput, Context context) {
	       
		 LambdaLogger logger = context.getLogger();
		 LambdaProxyOutput output = new LambdaProxyOutput();
		 ObjectMapper mapper = new ObjectMapper();
        try {
        		
        		logger.log("Starting lambda Execution : PersonalCapital1: input request" + lambdaInput);
        		
        		// validate and create a request object
        		PlanSearchRequest searchRequest = validateRequest(lambdaInput);
        		
        		// get the page for for the search
        		long pageNo = searchRequest.getPageNo();
            	
        		long fromIndex = (pageNo - 1) * ApplicationConstants.RESULTS_PER_PAGE;
        		
        		searchRequest.setPaginationStartIndex(fromIndex);
        		logger.log("search request" + searchRequest);
        		Request<?> request = esClient.generateRequest(searchRequest, context);
        		
        		PlanSearchResponse response = esClient.invokeSearch(request);
        		
        		long to = fromIndex + ApplicationConstants.RESULTS_PER_PAGE;
        		response.setCurrentpageNo(pageNo);
        		if (response.getResult() == null || response.getResult().size() == 0) {
            		response.setResultFromIndex(0);
            		response.setResultToIndex(0);
        		} else {
            		// index is zero based in elastic search. so add 1 before returning it to client
        			response.setHasResult(true);
            		response.setResultFromIndex(fromIndex+1);
            		response.setResultToIndex(to > response.getTotalRecords() ? response.getTotalRecords() : to);
        		}
            	 
            output.setStatusCode(200);
   		 	output.setBody(mapper.writeValueAsString(response));
        } catch (PlansValidationException ex) {
        		logger.log("Error during execution PlansValidationException" + ex);
        		output.setStatusCode(ex.getError().getErrorId());
        		try {
					output.setBody(mapper.writeValueAsString(ex.getError()));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
        } catch(AmazonServiceException awsExp) {
        		logger.log("Error during execution AmazonServiceException" + awsExp);
        		output.setStatusCode(ApplicationConstants.HTTP_CODE_500);
        		output.setBody(ApplicationConstants.SERVER_ERROR_MESSAGE);
 		} catch (Exception e) {
            logger.log("Error during execution" + e.getStackTrace());
            output.setStatusCode(ApplicationConstants.HTTP_CODE_500);
    			output.setBody(e.getMessage());
        }
		return output;

	 }	 
	
	 /**
	  * Validates the the query string parameter received.
	  * Search functionality requires that atleast one of these parameters are populated 
	  * - planName, sponsorName, sponsorSate.
	  * It also builds the request object with all the input parameters.
	  * @param lambdaInput
	  * @return PlanSearchRequest
	 * @throws PlansValidationException 
	  */
	 public static PlanSearchRequest  validateRequest(LambdaProxyInput lambdaInput) throws PlansValidationException{
		 
		 // validate for atleast one search parameter is provided
		 if (lambdaInput.getQueryStringParameters() == null) {
 			throw new PlansValidationException(ApplicationConstants.HTTP_CODE_400, ApplicationConstants.INVALID_QUERY_PARAM);
 		 } 
		 
		 // check for valid search parameters
		 PlanSearchRequest searchRequest = new PlanSearchRequest();
		 if (lambdaInput.getQueryStringParameters().get(ApplicationConstants.QUERY_PARAM_PLANNAME) != null
    			 && !ApplicationConstants.STR_BLANK.equals(
    					 lambdaInput.getQueryStringParameters().get(ApplicationConstants.QUERY_PARAM_PLANNAME).trim())) {
    		 	searchRequest.setPlanName(lambdaInput.getQueryStringParameters().get(ApplicationConstants.QUERY_PARAM_PLANNAME).trim());
    	 	}
    	 
	    	 if (lambdaInput.getQueryStringParameters().get(ApplicationConstants.QUERY_PARAM_SPONSORNAME) != null
	    			 && !ApplicationConstants.STR_BLANK.equals(
	    					 lambdaInput.getQueryStringParameters().get(ApplicationConstants.QUERY_PARAM_SPONSORNAME).trim())) {
	    		 searchRequest.setSponsorName(lambdaInput.getQueryStringParameters().get(ApplicationConstants.QUERY_PARAM_SPONSORNAME).trim());
	    	 }
	    	 
	    	 if (lambdaInput.getQueryStringParameters().get(ApplicationConstants.QUERY_PARAM_SPONSORSTATE) != null
	    			&& !ApplicationConstants.STR_BLANK.equals(
	    					lambdaInput.getQueryStringParameters().get(ApplicationConstants.QUERY_PARAM_SPONSORSTATE).trim())) {
	    		 searchRequest.setSponsorState(lambdaInput.getQueryStringParameters().get(ApplicationConstants.QUERY_PARAM_SPONSORSTATE).trim());
	    		 
	    		// state code is 2 characters for the plans
		    	 if (searchRequest.getSponsorState().length() != 2 ) {
		    		 throw new PlansValidationException(ApplicationConstants.HTTP_CODE_400, ApplicationConstants.INVALID_STATE_CODE);
		    	 } 
	    	 }
        
	    	 // default search will not be allowed. Validate atleast 1 query parameter is passed.
	    	 if (searchRequest.getPlanName() == null && searchRequest.getSponsorName() == null && searchRequest.getSponsorState() == null)  {
	    		 
	    		 throw new PlansValidationException(ApplicationConstants.HTTP_CODE_400, ApplicationConstants.INVALID_QUERY_PARAM);
	    	 }
	    	 
	    	 // populate search result pageNo. Set the default to 1
	    	 if (lambdaInput.getQueryStringParameters().get(EsPropertiesEnum.PAGE.value()) != null
         			&& !ApplicationConstants.STR_BLANK.equals(lambdaInput.getQueryStringParameters().get(EsPropertiesEnum.PAGE.value()).trim())) {
     		long pageNo = Long.parseLong(lambdaInput.getQueryStringParameters().get(
     						EsPropertiesEnum.PAGE.value()));
     		
     		// max pagination limit set on ES domain
     		if (pageNo > ApplicationConstants.MAX_RESULTSET_PAGINATION) {
     			 throw new PlansValidationException(ApplicationConstants.HTTP_CODE_400, ApplicationConstants.MAX_PAGINATION_LIMIT);
     		}
     		 searchRequest.setPageNo(pageNo<1? 1 :pageNo);
	    	 } else {
     	 	searchRequest.setPageNo(1);
	    	 }
		 
		 return searchRequest;
	 }
}
