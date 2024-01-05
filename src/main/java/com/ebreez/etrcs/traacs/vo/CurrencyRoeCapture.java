/**
 * 
 */
package com.ebreez.etrcs.traacs.vo;

import java.util.HashMap;
import java.util.Map;

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
public class CurrencyRoeCapture {
	
	
	@JsonProperty("ARR_AUTHENTICATION_KEY")
	private RoeAuthentication roeAuthentication;
	
	@JsonProperty("ARR_ROE_DETAILS")
	Map<String, Map<String, String>> roeDetails = new HashMap<String, Map<String, String>>();
	

	public RoeAuthentication getRoeAuthentication() {
		return roeAuthentication;
	}

	public void setRoeAuthentication(RoeAuthentication roeAuthentication) {
		this.roeAuthentication = roeAuthentication;
	}

	/**
	 * @return the roeDetails
	 */
	public Map<String, Map<String, String>> getRoeDetails() {
		return roeDetails;
	}

	/**
	 * @param roeDetails the roeDetails to set
	 */
	public void setRoeDetails(Map<String, Map<String, String>> roeDetails) {
		this.roeDetails = roeDetails;
	}

	

}
