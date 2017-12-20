package com.pc.exercise.model;

/**
 * Mapping object for error response
 */
public class ErrorVO {
	private Integer errorId;
	
	private String errorMessage;
	
	
	public Integer getErrorId() {
		return errorId;
	}
	public void setErrorId(Integer errorId) {
		this.errorId = errorId;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
		
	}
	
	public ErrorVO(Integer errorId, String errorMessage) {
		this.errorId = errorId;
		this.errorMessage = errorMessage;
	}
}
