/**
 * 
 */
package com.ebreez.etrcs.traacs.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;



@JsonAutoDetect(
	    fieldVisibility = JsonAutoDetect.Visibility.ANY,
	    getterVisibility = JsonAutoDetect.Visibility.NONE,
	    setterVisibility = JsonAutoDetect.Visibility.NONE
	)
@JsonInclude(Include.ALWAYS)
public class SupportingDocsJson implements Serializable {
	
	/** *  */
	private static final long serialVersionUID = 1L;

	@JsonProperty("STR_SUPO_DOC_NO")
	private String STR_SUPO_DOC_NO;
	
	@JsonProperty("DBL_SELLING_CUR_AMOUNT") 
	private Double DBL_SELLING_CUR_AMOUNT;
	
	@JsonProperty("DBL_BASE_CUR_AMOUNT") 
	private Double DBL_BASE_CUR_AMOUNT;

	public String getSTR_SUPO_DOC_NO() {
		return STR_SUPO_DOC_NO;
	}

	public void setSTR_SUPO_DOC_NO(String sTR_SUPO_DOC_NO) {
		STR_SUPO_DOC_NO = sTR_SUPO_DOC_NO;
	}

	public Double getDBL_SELLING_CUR_AMOUNT() {
		return DBL_SELLING_CUR_AMOUNT;
	}

	public void setDBL_SELLING_CUR_AMOUNT(Double dBL_SELLING_CUR_AMOUNT) {
		DBL_SELLING_CUR_AMOUNT = dBL_SELLING_CUR_AMOUNT;
	}

	public Double getDBL_BASE_CUR_AMOUNT() {
		return DBL_BASE_CUR_AMOUNT;
	}

	public void setDBL_BASE_CUR_AMOUNT(Double dBL_BASE_CUR_AMOUNT) {
		DBL_BASE_CUR_AMOUNT = dBL_BASE_CUR_AMOUNT;
	}
}
