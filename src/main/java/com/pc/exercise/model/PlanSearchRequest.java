package com.pc.exercise.model;

/**
 * Maintains the elastic search request data.
 *
 */
public class PlanSearchRequest {
	private String planName;
	private String sponsorName;
	private String sponsorState;
	// requested page number
	private long pageNo;
	// index of the first item to retrieve from the search result
	private long paginationStartIndex;
	
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public String getSponsorName() {
		return sponsorName;
	}
	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName;
	}
	public String getSponsorState() {
		return sponsorState;
	}
	public void setSponsorState(String sponsorState) {
		this.sponsorState = sponsorState;
	}
	
	public PlanSearchRequest(){
	}
	public long getPageNo() {
		return pageNo;
	}
	public void setPageNo(long pageNo) {
		this.pageNo = pageNo;
	}
	public long getPaginationStartIndex() {
		return paginationStartIndex;
	}
	public void setPaginationStartIndex(long paginationStartIndex) {
		this.paginationStartIndex = paginationStartIndex;
	}
	
	@Override
	public String toString() {
		return "Input : { planName: "+ planName + ", sponsorName :" + sponsorName + ", sponsorState:" +sponsorState +
				", pageNo: " + pageNo + ", paginationStartIndex: " + paginationStartIndex;

	}
}
