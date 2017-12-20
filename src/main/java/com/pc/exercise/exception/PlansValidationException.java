package com.pc.exercise.exception;

import com.pc.exercise.model.ErrorVO;

/**
 */
public class PlansValidationException extends Exception{
	
	private static final long serialVersionUID = -1194746609576145989L;
	private ErrorVO error;

	public PlansValidationException(Integer statusCode, String message) {
		super(message);
		this.error = new ErrorVO(statusCode, message);
		
	}

	public ErrorVO getError() {
		return error;
	}

	public void setError(ErrorVO error) {
		this.error = error;
	}

	
}
