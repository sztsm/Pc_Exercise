#
Using Java, write a micro service that invokes AWS elastic search and make it available using API gateway.     
 
1. Test Data - http://askebsa.dol.gov/FOIA%20Files/2016/Latest/F_5500_2016_Latest.zip   
2. Search should be allowed by Plan name, Sponsor name and Sponsor State   
3. Use AWS best practices   

API Gateway :
1. Endpoint point search webservice :
https://4q41ja05o9.execute-api.us-west-1.amazonaws.com/uat/plans

2. URL for search by planName
https://4q41ja05o9.execute-api.us-west-1.amazonaws.com/uat/plans?planName=INSURANCE

3. URL for search by sponsorName
https://4q41ja05o9.execute-api.us-west-1.amazonaws.com/uat/plans?sponsorName=INC

4. URL for search by sponsorState
https://4q41ja05o9.execute-api.us-west-1.amazonaws.com/uat/plans?sponsorState=CA

Note : 
1. atleast 1 path parameter needs to be provided for search.
2. The result size is 10
3. Pagination is supported int the result
URL : https://4q41ja05o9.execute-api.us-west-1.amazonaws.com/uat/plans?sponsorState=CA?page1
4. combinational search on planName, sponsorName, sponsorState is allowed.
