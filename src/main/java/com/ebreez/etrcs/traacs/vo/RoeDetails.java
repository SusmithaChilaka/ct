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
public class RoeDetails {

	@JsonProperty("STR_EFFECTIVE_DATE")
	private String STR_EFFECTIVE_DATE;
	
	@JsonProperty("DBL_EXCHANGE_RATE")
	private String DBL_EXCHANGE_RATE;
	
	@JsonProperty("STR_CURRENCY")
	private String STR_CURRENCY;
	

	public String getSTR_EFFECTIVE_DATE() {
		return STR_EFFECTIVE_DATE;
	}

	public void setSTR_EFFECTIVE_DATE(String sTR_EFFECTIVE_DATE) {
		STR_EFFECTIVE_DATE = sTR_EFFECTIVE_DATE;
	}

	public String getDBL_EXCHANGE_RATE() {
		return DBL_EXCHANGE_RATE;
	}

	public void setDBL_EXCHANGE_RATE(String dBL_EXCHANGE_RATE) {
		DBL_EXCHANGE_RATE = dBL_EXCHANGE_RATE;
	}

	public String getSTR_CURRENCY() {
		return STR_CURRENCY;
	}

	public void setSTR_CURRENCY(String sTR_CURRENCY) {
		STR_CURRENCY = sTR_CURRENCY;
	}
}
