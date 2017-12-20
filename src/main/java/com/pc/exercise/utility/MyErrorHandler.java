package com.pc.exercise.utility;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.http.HttpResponseHandler;
/**
 * Error handling class for the ES service.
 *
 */
public class MyErrorHandler implements HttpResponseHandler<AmazonServiceException> {

    @Override
    public AmazonServiceException handle(com.amazonaws.http.HttpResponse response) throws Exception {
    		System.out.println("MyErrorHandler:handle enter");
        AmazonServiceException ase = new AmazonServiceException("");
        ase.setStatusCode(response.getStatusCode());
        ase.setErrorCode(response.getStatusText());
        
        System.out.println("MyErrorHandler:handle exit");
        return ase;
    }

    @Override
    public boolean needsConnectionLeftOpen() {
        return false;
    }
}
