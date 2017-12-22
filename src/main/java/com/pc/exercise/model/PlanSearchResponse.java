package com.pc.exercise.model;

import java.util.List;
/**
 * Maintains the Elastic search response
 */
public class PlanSearchResponse {
	// Total records matching the search
	private long totalRecords;
	
	private boolean hasResult;
	
	// search result
	private List<Item> result;
	
	// the page no the search result belongs to
	private long currentpageNo;
	// index of the first item to retrieve from the search result
	private long resultFromIndex;
	
	// index of the first item to retrieve from the search result
	private long resultToIndex;
	

	public PlanSearchResponse(){}
	
	public long getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(long total) {
		this.totalRecords = total;
	}

	public List<Item> getResult() {
		return result;
	}

	public void setResult(List<Item> result) {
		this.result = result;
	}

	public long getCurrentpageNo() {
		return currentpageNo;
	}

	public void setCurrentpageNo(long currentpageNo) {
		this.currentpageNo = currentpageNo;
	}

	public long getResultFromIndex() {
		return resultFromIndex;
	}

	public void setResultFromIndex(long resultFromIndex) {
		this.resultFromIndex = resultFromIndex;
	}

	public long getResultToIndex() {
		return resultToIndex;
	}

	public void setResultToIndex(long resultToIndex) {
		this.resultToIndex = resultToIndex;
	}
	
	 public boolean isHasResult() {
		return hasResult;
	}

	public void setHasResult(boolean hasResult) {
		this.hasResult = hasResult;
	}

	@Override
	    public String toString() {
	    		return "Response : { totalRecords: "+ totalRecords + ", currentpageNo :" + currentpageNo + ", resultFromIndex:" +resultFromIndex +
	    				", resultToIndex: " + resultToIndex + ", result: " + result + ", hasResult: " + hasResult + " }";
	    }
	
}
