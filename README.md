
Using Java, write a micro service that invokes AWS elastic search and make it available using API gateway.     
 
1. Test Data - http://askebsa.dol.gov/FOIA%20Files/2016/Latest/F_5500_2016_Latest.zip   
2. Search should be allowed by Plan name, Sponsor name and Sponsor State   
3. Use AWS best practices   

API Gateway :

1. Endpoint for search webservice :
https://4q41ja05o9.execute-api.us-west-1.amazonaws.com/uat/plans

2. URL for search by planName
https://4q41ja05o9.execute-api.us-west-1.amazonaws.com/uat/plans?planName= HEALTHMEDX%20401K

3. URL for search by sponsorName
https://4q41ja05o9.execute-api.us-west-1.amazonaws.com/uat/plans?sponsorName=OLIVERMCMILLAN

4. URL for search by sponsorState
https://4q41ja05o9.execute-api.us-west-1.amazonaws.com/uat/plans?sponsorState=CA

Notes: 
1. The plans can either be search by: planName, sponsorName, sponsorState.
   At least 1 search query parameter needs to be provided for search.
2. Combinational search on planName, sponsorName, sponsorState is allowed.
3. Sponsorstate query parameter accepts 2 character US state code.
4. The default search result size is 10
5. Pagination is supported for the result. Maximum number of search pages that can be navigated is 1000.
   Example to fetch the second page : https://4q41ja05o9.execute-api.us-west-1.amazonaws.com/uat/plans?sponsorState=CA?page=2
6. Results are returned in json format.
	 object contains :
	 a. totalRecords : Total records for the search search
	 b. currentpageNo : The current result's page No.
	 c. resultFromIndex : The starting index of the result retrieved. 
	 d. resultToIndex : The ending index of the result retrieved.
	 e. result : Contains the json plan details. The result will only fetch the results properties that has a non null value.
	 f. hasResult: The current page no retrieved contains result
7. In case of validation errors, response contains:
	a.  errorId : HTTP Error Code.
	b. errorMessage : Error descriptions.
