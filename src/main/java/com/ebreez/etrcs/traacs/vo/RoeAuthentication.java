/**
 * 
 */
package com.ebreez.etrcs.traacs.vo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author Rakesh K
 *
 */

@JsonAutoDetect(
	    fieldVisibility = JsonAutoDetect.Visibility.ANY,
	    getterVisibility = JsonAutoDetect.Visibility.NONE,
	    setterVisibility = JsonAutoDetect.Visibility.NONE
	)
@JsonSerialize
@JsonInclude(Include.ALWAYS)
public class RoeAuthentication {
	
	@JsonProperty("STR_USER_NAME")
	private String STR_USER_NAME;
	
	@JsonProperty("STR_PASSWORD")
	private String STR_PASSWORD;
	

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

}
