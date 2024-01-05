/**
 * 
 */
package com.ebreez.etrcs.traacs.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Rakesh K
 *
 */

@JsonAutoDetect(
	    fieldVisibility = JsonAutoDetect.Visibility.ANY,
	    getterVisibility = JsonAutoDetect.Visibility.NONE,
	    setterVisibility = JsonAutoDetect.Visibility.NONE
	)
@JsonInclude(Include.ALWAYS)
public class SharingJsonMaster implements Serializable {
	
	/** *  */
	private static final long serialVersionUID = 1L;

	@JsonProperty("STR_DATE")
	private String STR_DATE;
	
	@JsonProperty("STR_AGAINST_DOC_NO")
	private String STR_AGAINST_DOC_NO;
	
	@JsonProperty("STR_REFERENCE")
	private String STR_REFERENCE;
	
	@JsonProperty("STR_ADVANCE_RECEIPT_NO")
	private String STR_ADVANCE_RECEIPT_NO;
	
	@JsonProperty("STR_MODE_OF_PAYMENT")
	private String STR_MODE_OF_PAYMENT;
	
	@JsonProperty("STR_NARRATION")
	private String STR_NARRATION;
	
	@JsonProperty("STR_SUB_CUSTOMER_CODE")
	private String STR_SUB_CUSTOMER_CODE;
	
	@JsonProperty("DBL_SELLING_ROE") 
	private Double DBL_SELLING_ROE;
	
	@JsonProperty("STR_SELLING_CUR_CODE")
	private String STR_SELLING_CUR_CODE;
	
	@JsonProperty("STR_ACCOUNT_CODE")
	private String STR_ACCOUNT_CODE;
	
	@JsonProperty("DBL_SELLING_CUR_AMOUNT") 
	private Double DBL_SELLING_CUR_AMOUNT;
	
	@JsonProperty("DBL_BASE_CUR_AMOUNT") 
	private Double DBL_BASE_CUR_AMOUNT;
	
	@JsonProperty("STR_DEPOSITORS_NAME")
	private String STR_DEPOSITORS_NAME;
	
	@JsonProperty("STR_CHEQUE_NO")
	private String STR_CHEQUE_NO;
	
	@JsonProperty("STR_CHEQUE_DATE")
	private String STR_CHEQUE_DATE;
	
	@JsonProperty("STR_NAME_OF_CHEQUE")
	private String STR_NAME_OF_CHEQUE;
	
	@JsonProperty("STR_BANK_NAME")
	private String STR_BANK_NAME;
	
	@JsonProperty("STR_ID_NO")
	private String STR_ID_NO;
	
	@JsonProperty("STR_POS_ID")
	private String STR_POS_ID;
	
	@JsonProperty("STR_CC_NO")
	private String STR_CC_NO;
	
	@JsonProperty("STR_PAY_ID")
	private String STR_PAY_ID;
	
	@JsonProperty("ARR_SUPO_DOCS")
	private List<SupportingDocsJson> ARR_SUPO_DOCS;

	public String getSTR_DATE() {
		return STR_DATE;
	}

	public void setSTR_DATE(String sTR_DATE) {
		STR_DATE = sTR_DATE;
	}

	public String getSTR_AGAINST_DOC_NO() {
		return STR_AGAINST_DOC_NO;
	}

	public void setSTR_AGAINST_DOC_NO(String sTR_AGAINST_DOC_NO) {
		STR_AGAINST_DOC_NO = sTR_AGAINST_DOC_NO;
	}

	public String getSTR_REFERENCE() {
		return STR_REFERENCE;
	}

	public void setSTR_REFERENCE(String sTR_REFERENCE) {
		STR_REFERENCE = sTR_REFERENCE;
	}

	public String getSTR_ADVANCE_RECEIPT_NO() {
		return STR_ADVANCE_RECEIPT_NO;
	}

	public void setSTR_ADVANCE_RECEIPT_NO(String sTR_ADVANCE_RECEIPT_NO) {
		STR_ADVANCE_RECEIPT_NO = sTR_ADVANCE_RECEIPT_NO;
	}

	public String getSTR_MODE_OF_PAYMENT() {
		return STR_MODE_OF_PAYMENT;
	}

	public void setSTR_MODE_OF_PAYMENT(String sTR_MODE_OF_PAYMENT) {
		STR_MODE_OF_PAYMENT = sTR_MODE_OF_PAYMENT;
	}

	public String getSTR_SUB_CUSTOMER_CODE() {
		return STR_SUB_CUSTOMER_CODE;
	}

	public void setSTR_SUB_CUSTOMER_CODE(String sTR_SUB_CUSTOMER_CODE) {
		STR_SUB_CUSTOMER_CODE = sTR_SUB_CUSTOMER_CODE;
	}

	public Double getDBL_SELLING_ROE() {
		return DBL_SELLING_ROE;
	}

	public void setDBL_SELLING_ROE(Double dBL_SELLING_ROE) {
		DBL_SELLING_ROE = dBL_SELLING_ROE;
	}

	public String getSTR_SELLING_CUR_CODE() {
		return STR_SELLING_CUR_CODE;
	}

	public void setSTR_SELLING_CUR_CODE(String sTR_SELLING_CUR_CODE) {
		STR_SELLING_CUR_CODE = sTR_SELLING_CUR_CODE;
	}

	public String getSTR_ACCOUNT_CODE() {
		return STR_ACCOUNT_CODE;
	}

	public void setSTR_ACCOUNT_CODE(String sTR_ACCOUNT_CODE) {
		STR_ACCOUNT_CODE = sTR_ACCOUNT_CODE;
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

	public String getSTR_DEPOSITORS_NAME() {
		return STR_DEPOSITORS_NAME;
	}

	public void setSTR_DEPOSITORS_NAME(String sTR_DEPOSITORS_NAME) {
		STR_DEPOSITORS_NAME = sTR_DEPOSITORS_NAME;
	}

	public String getSTR_CHEQUE_NO() {
		return STR_CHEQUE_NO;
	}

	public void setSTR_CHEQUE_NO(String sTR_CHEQUE_NO) {
		STR_CHEQUE_NO = sTR_CHEQUE_NO;
	}

	public String getSTR_CHEQUE_DATE() {
		return STR_CHEQUE_DATE;
	}

	public void setSTR_CHEQUE_DATE(String sTR_CHEQUE_DATE) {
		STR_CHEQUE_DATE = sTR_CHEQUE_DATE;
	}

	public String getSTR_NAME_OF_CHEQUE() {
		return STR_NAME_OF_CHEQUE;
	}

	public void setSTR_NAME_OF_CHEQUE(String sTR_NAME_OF_CHEQUE) {
		STR_NAME_OF_CHEQUE = sTR_NAME_OF_CHEQUE;
	}

	public String getSTR_BANK_NAME() {
		return STR_BANK_NAME;
	}

	public void setSTR_BANK_NAME(String sTR_BANK_NAME) {
		STR_BANK_NAME = sTR_BANK_NAME;
	}

	public String getSTR_ID_NO() {
		return STR_ID_NO;
	}

	public void setSTR_ID_NO(String sTR_ID_NO) {
		STR_ID_NO = sTR_ID_NO;
	}

	public String getSTR_POS_ID() {
		return STR_POS_ID;
	}

	public void setSTR_POS_ID(String sTR_POS_ID) {
		STR_POS_ID = sTR_POS_ID;
	}

	public String getSTR_CC_NO() {
		return STR_CC_NO;
	}

	public void setSTR_CC_NO(String sTR_CC_NO) {
		STR_CC_NO = sTR_CC_NO;
	}

	public String getSTR_PAY_ID() {
		return STR_PAY_ID;
	}

	public void setSTR_PAY_ID(String sTR_PAY_ID) {
		STR_PAY_ID = sTR_PAY_ID;
	}

	public List<SupportingDocsJson> getARR_SUPO_DOCS() {
		return ARR_SUPO_DOCS;
	}

	public void setARR_SUPO_DOCS(List<SupportingDocsJson> aRR_SUPO_DOCS) {
		ARR_SUPO_DOCS = aRR_SUPO_DOCS;
	}
	
}
