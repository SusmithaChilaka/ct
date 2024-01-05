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
public class MasterDetailsJson implements Serializable {
	
	/** *  */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("STR_DOCUMENT_NO")
	private String STR_DOCUMENT_NO;
	
	@JsonProperty("STR_ACTION")
	private String STR_ACTION;
	
	@JsonProperty("STR_INVOICE_DATE") 
	private String STR_INVOICE_DATE;
	
	@JsonProperty("STR_REFUND_DATE") 
	private String STR_REFUND_DATE;
	
	@JsonProperty("STR_COST_CENTRE_CODE") 
	private String STR_COST_CENTRE_CODE;
	
	@JsonProperty("STR_DEPARTMENT_CODE")
	private String STR_DEPARTMENT_CODE;
	
	@JsonProperty("STR_AGAINST_DOC_NO")
	private String STR_AGAINST_DOC_NO;
	
	@JsonProperty("STR_FILE_NUMBER") 
	private String STR_FILE_NUMBER;
	
	@JsonProperty("STR_ADVANCE_RECEIPT_NO")
	private String STR_ADVANCE_RECEIPT_NO;
	
	@JsonProperty("STR_REFERENCE")
	private String STR_REFERENCE;
	
	@JsonProperty("STR_SUB_CUSTOMER_CODE")
	private String STR_SUB_CUSTOMER_CODE;
	
	@JsonProperty("STR_SALES_MAN_CODE")
	private String STR_SALES_MAN_CODE;
	
	@JsonProperty("STR_NARRATION") 
	private String STR_NARRATION;
	
	@JsonProperty("STR_INVOICE_FOOT_NOTE")
	private String STR_INVOICE_FOOT_NOTE;
	
	@JsonProperty("DBL_SELLING_ROE") 
	private Double DBL_SELLING_ROE;
	
	@JsonProperty("STR_SELLING_CUR_CODE")
	private String STR_SELLING_CUR_CODE;
	
	@JsonProperty("STR_ACCOUNT_CODE") 
	private String STR_ACCOUNT_CODE;
	
	@JsonProperty("STR_CORP_CLIENT_CODE") 
	private String STR_CORP_CLIENT_CODE;
	
	@JsonProperty("STR_DEPOSITORS_NAME")
	private String STR_DEPOSITORS_NAME;
	
	@JsonProperty("STR_CHEQUE_NO")
	private String STR_CHEQUE_NO;
	
	@JsonProperty("STR_CHEQUE_DATE") 
	private String STR_CHEQUE_DATE;
	
	@JsonProperty("STR_BANK_NAME")
	private String STR_BANK_NAME;
	
	@JsonProperty("STR_NAME_OF_CHEQUE")
	private String STR_NAME_OF_CHEQUE;
	
	@JsonProperty("STR_ID_NO")
	private String STR_ID_NO;
	
	@JsonProperty("STR_POS_ID")
	private String STR_POS_ID;
	
	@JsonProperty("STR_CC_NO") 
	private String STR_CC_NO;
	
	@JsonProperty("STR_PAY_ID")
	private String STR_PAY_ID;
	
	@JsonProperty("DBL_COUPON_DISCOUNT") 
	private Double DBL_COUPON_DISCOUNT;
	

	public String getSTR_DOCUMENT_NO() {
		return STR_DOCUMENT_NO;
	}

	public void setSTR_DOCUMENT_NO(String sTR_DOCUMENT_NO) {
		STR_DOCUMENT_NO = sTR_DOCUMENT_NO;
	}

	public String getSTR_ACTION() {
		return STR_ACTION;
	}

	public void setSTR_ACTION(String sTR_ACTION) {
		STR_ACTION = sTR_ACTION;
	}

	public String getSTR_INVOICE_DATE() {
		return STR_INVOICE_DATE;
	}

	public void setSTR_INVOICE_DATE(String sTR_INVOICE_DATE) {
		STR_INVOICE_DATE = sTR_INVOICE_DATE;
	}

	public String getSTR_COST_CENTRE_CODE() {
		return STR_COST_CENTRE_CODE;
	}

	public void setSTR_COST_CENTRE_CODE(String sTR_COST_CENTRE_CODE) {
		STR_COST_CENTRE_CODE = sTR_COST_CENTRE_CODE;
	}

	public String getSTR_DEPARTMENT_CODE() {
		return STR_DEPARTMENT_CODE;
	}

	public void setSTR_DEPARTMENT_CODE(String sTR_DEPARTMENT_CODE) {
		STR_DEPARTMENT_CODE = sTR_DEPARTMENT_CODE;
	}

	public String getSTR_AGAINST_DOC_NO() {
		return STR_AGAINST_DOC_NO;
	}

	public void setSTR_AGAINST_DOC_NO(String sTR_AGAINST_DOC_NO) {
		STR_AGAINST_DOC_NO = sTR_AGAINST_DOC_NO;
	}

	public String getSTR_FILE_NUMBER() {
		return STR_FILE_NUMBER;
	}

	public void setSTR_FILE_NUMBER(String sTR_FILE_NUMBER) {
		STR_FILE_NUMBER = sTR_FILE_NUMBER;
	}

	public String getSTR_ADVANCE_RECEIPT_NO() {
		return STR_ADVANCE_RECEIPT_NO;
	}

	public void setSTR_ADVANCE_RECEIPT_NO(String sTR_ADVANCE_RECEIPT_NO) {
		STR_ADVANCE_RECEIPT_NO = sTR_ADVANCE_RECEIPT_NO;
	}

	public String getSTR_REFERENCE() {
		return STR_REFERENCE;
	}

	public void setSTR_REFERENCE(String sTR_REFERENCE) {
		STR_REFERENCE = sTR_REFERENCE;
	}

	public String getSTR_SUB_CUSTOMER_CODE() {
		return STR_SUB_CUSTOMER_CODE;
	}

	public void setSTR_SUB_CUSTOMER_CODE(String sTR_SUB_CUSTOMER_CODE) {
		STR_SUB_CUSTOMER_CODE = sTR_SUB_CUSTOMER_CODE;
	}

	public String getSTR_SALES_MAN_CODE() {
		return STR_SALES_MAN_CODE;
	}

	public void setSTR_SALES_MAN_CODE(String sTR_SALES_MAN_CODE) {
		STR_SALES_MAN_CODE = sTR_SALES_MAN_CODE;
	}

	public String getSTR_NARRATION() {
		return STR_NARRATION;
	}

	public void setSTR_NARRATION(String sTR_NARRATION) {
		STR_NARRATION = sTR_NARRATION;
	}

	public String getSTR_INVOICE_FOOT_NOTE() {
		return STR_INVOICE_FOOT_NOTE;
	}

	public void setSTR_INVOICE_FOOT_NOTE(String sTR_INVOICE_FOOT_NOTE) {
		STR_INVOICE_FOOT_NOTE = sTR_INVOICE_FOOT_NOTE;
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

	public String getSTR_CORP_CLIENT_CODE() {
		return STR_CORP_CLIENT_CODE;
	}

	public void setSTR_CORP_CLIENT_CODE(String sTR_CORP_CLIENT_CODE) {
		STR_CORP_CLIENT_CODE = sTR_CORP_CLIENT_CODE;
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

	public String getSTR_BANK_NAME() {
		return STR_BANK_NAME;
	}

	public void setSTR_BANK_NAME(String sTR_BANK_NAME) {
		STR_BANK_NAME = sTR_BANK_NAME;
	}

	public String getSTR_NAME_OF_CHEQUE() {
		return STR_NAME_OF_CHEQUE;
	}

	public void setSTR_NAME_OF_CHEQUE(String sTR_NAME_OF_CHEQUE) {
		STR_NAME_OF_CHEQUE = sTR_NAME_OF_CHEQUE;
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

	public Double getDBL_COUPON_DISCOUNT() {
		return DBL_COUPON_DISCOUNT;
	}

	public void setDBL_COUPON_DISCOUNT(Double dBL_COUPON_DISCOUNT) {
		DBL_COUPON_DISCOUNT = dBL_COUPON_DISCOUNT;
	}

	public String getSTR_REFUND_DATE() {
		return STR_REFUND_DATE;
	}

	public void setSTR_REFUND_DATE(String sTR_REFUND_DATE) {
		STR_REFUND_DATE = sTR_REFUND_DATE;
	}

}
