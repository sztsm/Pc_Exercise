package com.pc.exercise.model;

/**
 * Mapping object for the elastic search results
 */
public  class Item {
	private String id;
	
	private Object source;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Object getSource() {
		return source;
	}

	public void setSource(Object object) {
		this.source = object;
	}
	
	@Override
	public String toString() {
		return "id: " + id + " , source :" + source;
	}
	
}