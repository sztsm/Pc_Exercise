package com.amazonaws.lambda.function;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.AmazonWebServiceResponse;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.DefaultRequest;
import com.amazonaws.Request;
import com.amazonaws.Response;
import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.http.AmazonHttpClient;
import com.amazonaws.http.ExecutionContext;
import com.amazonaws.http.HttpMethodName;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pc.exercise.model.PlanSearchRequest;
import com.pc.exercise.model.PlanSearchResponse;
import com.pc.exercise.utility.ApplicationConstants;
import com.pc.exercise.utility.EsPropertiesEnum;
import com.pc.exercise.utility.MyErrorHandler;
import com.pc.exercise.utility.MyHttpResponseHandler;

public class ElasticSearchClient {
    private final static String endpoint = ApplicationConstants.ES_HOST + "/" + "plans/default/_search";
    
    private static AmazonHttpClient client;
    
    public ElasticSearchClient() {
    		client = new AmazonHttpClient(new ClientConfiguration());
    }
 
    /**
     * This method accepts the search input parameter and returns the search result.
     * @param request
     * @return PlanSearchResponse
     * @throws URISyntaxException
     */
    public PlanSearchResponse invokeSearch(Request<?> request) throws URISyntaxException {
    		// sign the request to AWS elastic search with aws credentials
    		performSigningSteps(request);
    		// call elastic search server
    		PlanSearchResponse response = sendRequest(request);
    		
    		
    		return response;
    }
    	
    /**
     * This method accepts the plansearchRequest object and creates appropritae json for the
     * Elastic search request.
     * When one than 1 search query parameters are present, it is interpreted as 'AND' action.
     * @param searchRequest
     * @param context
     * @return Request
     * @throws URISyntaxException
     */
    public Request<?> generateRequest(PlanSearchRequest searchRequest, Context context) throws URISyntaxException {
    		
    		LambdaLogger logger = context.getLogger();
    		logger.log("Enter generateRequest" );
    	
    		Request<?> request = new DefaultRequest<Void>(ApplicationConstants.ES_SERVICE);
        request.setEndpoint(new URI(endpoint));

        // create query object for elastic search
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
 	    json.put(ApplicationConstants.ES_SIZE_STR, ApplicationConstants.RESULTS_PER_PAGE);
 	    json.put(ApplicationConstants.ES_FROM_STR, searchRequest.getPaginationStartIndex());
 	    ObjectNode queryNode = json.putObject(ApplicationConstants.ES_QUERY_STR);
        
        List<ObjectNode> queryObjectsList = new ArrayList<ObjectNode>();
        
        // build a match search with fuzziness option for planName
        if (searchRequest.getPlanName() != null ) {
        		queryObjectsList.add(buildFuzzyMatchQuery(
        				EsPropertiesEnum.PLAN_NAME, searchRequest.getPlanName(), searchRequest.getPaginationStartIndex()));
        }
     // build a match search with fuzziness option for sponsor name
        if (searchRequest.getSponsorName() != null ) {
        		queryObjectsList.add(buildFuzzyMatchQuery(
    					EsPropertiesEnum.SPONSOR_DFE_NAME, searchRequest.getSponsorName(), searchRequest.getPaginationStartIndex()));
        }
     // build a match search for sponsor state
        if (searchRequest.getSponsorState() != null ) {
        		queryObjectsList.add(buildMatchQuery(EsPropertiesEnum.SPONS_DFE_MAIL_US_STATE, searchRequest.getSponsorState(), searchRequest.getPaginationStartIndex()));
        }
        
        // when more than 1 search parameters exits, construct a bool expression
        if (queryObjectsList.size() > 1) {
	    		ObjectNode boolNode = queryNode.putObject("bool");
	    		ArrayNode mustNode = boolNode.putArray("must");
	    		
	    		for (ObjectNode node : queryObjectsList) {
	    			mustNode.add(node);
	    		}
	    		
	    } else {
	    		queryNode.setAll(queryObjectsList.get(0));
	    }
      
        logger.log("ES query generated" + json.toString());
        
        request.setHttpMethod(HttpMethodName.POST);
        request.setContent(new ByteArrayInputStream(json.toString().getBytes()));
        // set headers
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type","application/json");
		request.setHeaders(headers );
        return request;
    }
    
    
    /**
     * Builds json Object for search for planName, SponsorName, 
     * with full text match and fuzziness.
     * @param key corresponds to the property in the elastic search index
     * @param value corresponds to the search value.
     * @return a json Object
     */
    private static ObjectNode buildFuzzyMatchQuery(EsPropertiesEnum propertyKey, String value, long startIndex) {
    		ObjectMapper mapper = new ObjectMapper();
 	    ObjectNode json = mapper.createObjectNode();
 	    ObjectNode matchNode = json.putObject(ApplicationConstants.ES_MATCH_STR);
 	    ObjectNode matchFieldName = matchNode.putObject(propertyKey.name().toLowerCase());
 	    matchFieldName.put(ApplicationConstants.ES_QUERY_STR, value);
 	    matchFieldName.put(ApplicationConstants.ES_FUZZINESS_STR, ApplicationConstants.ES_AUTO_STR);
 	    
 	    return json;
    	
    }
    
    /**
     * Builds json Object for search for Sponsor state, 
     * with full text match.
     * @param key corresponds to the property in the elastic search index
     * @param value corresponds to the search value.
     * @return a json Object
     */
    private static ObjectNode buildMatchQuery (EsPropertiesEnum propertyKey, String value, long startIndex) {
    		ObjectMapper mapper = new ObjectMapper();
    		ObjectNode json = mapper.createObjectNode();
    		ObjectNode matchNode = json.putObject(ApplicationConstants.ES_MATCH_STR);
    		matchNode.put(propertyKey.name().toLowerCase(), value);
 	    return json;
    	
    }
    
    /**
     * Signs the request using  region, service, and credentials. AWS4Signer
     * @param requestToSign
     */
    private static void performSigningSteps(Request<?> requestToSign) {
    		// get the AWS Credentials
    		AWSCredentialsProvider credsProvider = new DefaultAWSCredentialsProviderChain();
        AWSCredentials creds = credsProvider.getCredentials();

        // sign the request with the ES region and service name - "es" and aws credentials

        AWS4Signer signer = new AWS4Signer();
        signer.setRegionName(ApplicationConstants.ES_REGION);
        signer.setServiceName(ApplicationConstants.ES_SERVICE);
        signer.sign(requestToSign, creds);
    }
    
    /**
     * Sends the request to ES server with a HTTP client.
     * The response handler formats the response to PlanSearchResponse.
     * @param request
     * @return PlanSearchResponse
     */
    private static PlanSearchResponse sendRequest(Request<?> request) {
        MyErrorHandler errorHandler = new MyErrorHandler();
        MyHttpResponseHandler<PlanSearchResponse> responseHandler = new MyHttpResponseHandler<PlanSearchResponse>();
        Response<AmazonWebServiceResponse<PlanSearchResponse>> response = client
        														.requestExecutionBuilder()
        														.executionContext(new ExecutionContext(true))
        														.errorResponseHandler(errorHandler)
        														.request(request)
        														.execute(responseHandler);
        
        System.out.println("response is : " +  response.getAwsResponse().getResult()); 
    		return response.getAwsResponse().getResult();
    }

}
