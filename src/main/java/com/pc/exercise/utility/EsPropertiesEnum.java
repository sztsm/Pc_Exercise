package com.pc.exercise.utility;

/**
 * Maps the properties name in the elastic search index to the input search parameters
 * @author
 *
 */
public enum EsPropertiesEnum {
	PLAN_NAME ("planName"),
	SPONSOR_DFE_NAME ("sponsorName"),
	SPONS_DFE_MAIL_US_STATE ("sponsorState"),
	PAGE("page");
	
	String paramName;
	EsPropertiesEnum(String paramName) {
		this.paramName = paramName;
	}
	
	public String value() {
		return paramName;
	}
	
	public static EsPropertiesEnum fromValue(String value) {
		for (EsPropertiesEnum propertyEnum : EsPropertiesEnum.values()) {
			if (propertyEnum.value().equals(value)) {
				return propertyEnum;
			}
		}
		
		throw new IllegalArgumentException();
	}
}
