package com.pc.exercise.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.amazonaws.AmazonWebServiceResponse;
import com.amazonaws.http.HttpResponseHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pc.exercise.model.Item;
import com.pc.exercise.model.PlanSearchResponse;

/**
 * Response handler for the aws ES.
 *
 * @param <T>
 */
public class MyHttpResponseHandler<T> implements HttpResponseHandler<AmazonWebServiceResponse<PlanSearchResponse>> {
	
    @Override
    public AmazonWebServiceResponse<PlanSearchResponse> handle(com.amazonaws.http.HttpResponse response) throws Exception {
    		ObjectMapper mapper = new ObjectMapper();
    		// converts the inpuststream to map object and extract the required fields from response
    		Map<String, Object> jsonMap = mapper.readValue(response.getContent(), Map.class);
    		//System.out.println(" response received" + jsonMap);
    		
    		Map<String, Object> hitsNode = (Map<String, Object>) jsonMap.get("hits");
    		
    		Integer totalResultCount = (Integer) hitsNode.get("total");

    		List<Item> items = new ArrayList<>();
    		Item item = null;
    		for (Map<String, Object> hits : (List<Map>) hitsNode.get("hits") ) {
    			item = new Item();
    			item.setId((String) hits.get("_id"));
    			item.setSource(hits.get("_source"));
    			items.add(item);
    		}
    		
    		
    		PlanSearchResponse result = new PlanSearchResponse();
    		result.setTotalRecords(totalResultCount);
    		result.setResult(items);
  
    	  AmazonWebServiceResponse<PlanSearchResponse> awsResponse = new AmazonWebServiceResponse<PlanSearchResponse>();
    	  awsResponse.setResult(result);
    	  return awsResponse;
    }
   
    @Override
    public boolean needsConnectionLeftOpen() {
        return false;
    }

}