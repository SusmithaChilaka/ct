package com.ebreez.etrcs.traacs.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonAutoDetect(
	    fieldVisibility = JsonAutoDetect.Visibility.ANY,
	    getterVisibility = JsonAutoDetect.Visibility.NONE,
	    setterVisibility = JsonAutoDetect.Visibility.NONE
	)
@JsonSerialize
	@JsonIgnoreProperties(ignoreUnknown = true)
public class Authentication implements Serializable {
	
	/** *  */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("STR_USER_NAME")
	private String STR_USER_NAME;
	
	@JsonProperty("STR_PASSWORD")
	private String STR_PASSWORD;
	
	@JsonProperty("STR_AUTHENTICATION")
	private String STR_AUTHENTICATION;
	
	@JsonProperty("STR_REFERENCE_KEY")
	private String STR_REFERENCE_KEY;

	public String getSTR_USER_NAME() {
		return STR_USER_NAME;
	}

	public void setSTR_USER_NAME(String sTR_USER_NAME) {
		STR_USER_NAME = sTR_USER_NAME;
	}

	public String getSTR_PASSWORD() {
		return STR_PASSWORD;
	}

	public void setSTR_PASSWORD(String sTR_PASSWORD) {
		STR_PASSWORD = sTR_PASSWORD;
	}

	public String getSTR_AUTHENTICATION() {
		return STR_AUTHENTICATION;
	}

	public void setSTR_AUTHENTICATION(String sTR_AUTHENTICATION) {
		STR_AUTHENTICATION = sTR_AUTHENTICATION;
	}

	public String getSTR_REFERENCE_KEY() {
		return STR_REFERENCE_KEY;
	}

	public void setSTR_REFERENCE_KEY(String sTR_REFERENCE_KEY) {
		STR_REFERENCE_KEY = sTR_REFERENCE_KEY;
	}
	
}
