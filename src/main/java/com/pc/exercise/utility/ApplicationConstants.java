package com.pc.exercise.utility;

/**
 * Class is used to store constants used in the application.
 *
 */
public interface ApplicationConstants {
	
	String QUERY_PARAM_PLANNAME = "planName";
	String QUERY_PARAM_SPONSORNAME = "sponsorName";
	String QUERY_PARAM_SPONSORSTATE = "sponsorState";
	String STR_BLANK = "";
	String INVALID_QUERY_PARAM = "Atleast One valid query parameter must be provided for search. Refer to documentation for the list of allowed query parameters for search.";

	String INVALID_STATE_CODE = "Invalid state code. State code should be 2 Character in length";
	
	String SERVER_ERROR_MESSAGE = "Internal error during search";
	int HTTP_CODE_400 = 400;
	
	int HTTP_CODE_500 = 500;
	
	int RESULTS_PER_PAGE = 10;
	
	/* Elastic search configurations */
	String ES_HOST = "https://search-my-aws-es-domain1-skbazlw5ydvjx6qybbfe3ghkyi.us-west-1.es.amazonaws.com";
	
	String ES_SERVICE = "es";
	 
	String ES_REGION = "us-west-1";
	/* Elastic search parameters and keywords */
	
	String ES_SIZE_STR = "size";
	
	String ES_FROM_STR = "from";
	
	String ES_QUERY_STR = "query";
	
	String ES_MATCH_STR = "match";
	
	String ES_FUZZINESS_STR = "fuzziness";
	
	String ES_AUTO_STR = "auto";
	
	int MAX_RESULTSET_PAGINATION = 1000;
	
	String MAX_PAGINATION_LIMIT = "The Result window is too large. Please use more specific search parameters";
}
