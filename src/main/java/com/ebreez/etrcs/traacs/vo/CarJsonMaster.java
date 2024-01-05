/**
 * 
 */
package com.ebreez.etrcs.traacs.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
public class CarJsonMaster implements Serializable {
	
	/** *  */
	private static final long serialVersionUID = 1L;

	@JsonProperty("STR_TYPE")
	private String STR_TYPE;
	
	@JsonProperty("STR_REFUND_DATE")
	private String STR_REFUND_DATE;
	
	@JsonProperty("STR_VOUCHER_NO")
	private String STR_VOUCHER_NO;
	
	@JsonProperty("STR_ISSUE_DATE")
	private String STR_ISSUE_DATE;
	
	@JsonProperty("STR_MODE_OF_PAYMENT")
	private String STR_MODE_OF_PAYMENT;
	
	@JsonProperty("STR_SUPPLIER_CODE")
	private String STR_SUPPLIER_CODE;
	
	@JsonProperty("STR_CORP_CREDIT_CARD_ACCOUNT_CODE")
	private String STR_CORP_CREDIT_CARD_ACCOUNT_CODE;
	
	@JsonProperty("STR_CUSTOMER_CORP_CREDIT_CARD_ACCOUNT_CODE")
	private String STR_CUSTOMER_CORP_CREDIT_CARD_ACCOUNT_CODE;
	
	@JsonProperty("STR_RENTAL_COMPANY_CODE")
	private String STR_RENTAL_COMPANY_CODE;
	
	@JsonProperty("STR_RENTING_STATION")
	private String STR_RENTING_STATION;

	@JsonProperty("STR_DROP_STATION")
	private String STR_DROP_STATION;
	
	@JsonProperty("STR_FROM_DATE")
	private String STR_FROM_DATE;
	
	@JsonProperty("STR_TO_DATE")
	private String STR_TO_DATE;
	
	@JsonProperty("INT_NO_OF_CARS")
	private Integer INT_NO_OF_CARS;
	
	@JsonProperty("INT_NO_OF_DAYS")
	private Integer INT_NO_OF_DAYS;

	@JsonProperty("STR_TRAVELER_ID")
	private String STR_TRAVELER_ID;
	
	@JsonProperty("STR_PAX_NAME")
	private String STR_PAX_NAME;
	
	@JsonProperty("STR_ADDITIONAL_PAX")
	private String STR_ADDITIONAL_PAX;
	
	@JsonProperty("STR_SUPPLIER_CONF_NUMBER")
	private String STR_SUPPLIER_CONF_NUMBER;
	
	@JsonProperty("STR_CAR_CONF_NO")
	private String STR_CAR_CONF_NO;
	
	@JsonProperty("STR_REFUND_STAFF")
	private String STR_REFUND_STAFF;
	
	@JsonProperty("STR_REFUND_STAFF_EMAIL_ID")
	private String STR_REFUND_STAFF_EMAIL_ID;
	
	@JsonProperty("STR_LPO_NO")
	private String STR_LPO_NO;
	
	@JsonProperty("STR_PNR_NO")
	private String STR_PNR_NO;
	
	@JsonProperty("STR_STAFF")
	private String STR_STAFF;

	@JsonProperty("STR_STAFF_EMAIL_ID")
	private String STR_STAFF_EMAIL_ID;
	
	@JsonProperty("STR_CUSTOMER_REF_NO")
	private String STR_CUSTOMER_REF_NO;
	
	@JsonProperty("STR_CUSTOMER_EMP_NO")
	private String STR_CUSTOMER_EMP_NO;
	
	@JsonProperty("STR_REMARKS")
	private String STR_REMARKS;
	
	@JsonProperty("STR_REFUND_STATUS")
	private String STR_REFUND_STATUS;
	
	@JsonProperty("STR_BOOKING_DETAILS")
	private String STR_BOOKING_DETAILS;
	
	@JsonProperty("STR_CATEGORY")
	private String STR_CATEGORY;

	@JsonProperty("STR_VEHICLE")
	private String STR_VEHICLE;
	
	@JsonProperty("STR_MODEL")
	private String STR_MODEL;
	
	@JsonProperty("STR_TRANSMISSION")
	private String STR_TRANSMISSION;
	
	@JsonProperty("STR_PRODUCT")
	private String STR_PRODUCT;
	
	@JsonProperty("DBL_PURCHASE_ROE") 
	private Double DBL_PURCHASE_ROE;
	
	@JsonProperty("STR_PURCHASE_CUR_CODE")
	private String STR_PURCHASE_CUR_CODE;
	
	@JsonProperty("DBL_PURCHASE_CUR_TOTAL_FARE") 
	private Double DBL_PURCHASE_CUR_TOTAL_FARE;
	
	@JsonProperty("DBL_PURCHASE_CUR_TOTAL_MARKET_FARE") 
	private Double DBL_PURCHASE_CUR_TOTAL_MARKET_FARE;
	
	@JsonProperty("DBL_BASE_CUR_TOTAL_FARE") 
	private Double DBL_BASE_CUR_TOTAL_FARE;
	
	@JsonProperty("DBL_SELLING_CUR_TOTAL_FARE") 
	private Double DBL_SELLING_CUR_TOTAL_FARE;
	
	@JsonProperty("DBL_PURCHASE_CUR_TOTAL_MARKET_FARE_CREDIT_CARD") 
	private Double DBL_PURCHASE_CUR_TOTAL_MARKET_FARE_CREDIT_CARD;
	
	@JsonProperty("DBL_BASE_CUR_TOTAL_MARKET_FARE_CREDIT_CARD") 
	private Double DBL_BASE_CUR_TOTAL_MARKET_FARE_CREDIT_CARD;
	
	@JsonProperty("DBL_SELLING_CUR_TOTAL_MARKET_FARE_CREDIT_CARD") 
	private Double DBL_SELLING_CUR_TOTAL_MARKET_FARE_CREDIT_CARD;
	
	@JsonProperty("DBL_PURCHASE_CUR_TOTAL_TAX") 
	private Double DBL_PURCHASE_CUR_TOTAL_TAX;
	
	@JsonProperty("DBL_BASE_CUR_TOTAL_TAX") 
	private Double DBL_BASE_CUR_TOTAL_TAX;
	
	@JsonProperty("DBL_SELLING_CUR_TOTAL_TAX") 
	private Double DBL_SELLING_CUR_TOTAL_TAX;
	
	@JsonProperty("DBL_PURCHASE_CUR_TOTAL_TAX_CREDIT_CARD") 
	private Double DBL_PURCHASE_CUR_TOTAL_TAX_CREDIT_CARD;
	
	@JsonProperty("DBL_BASE_CUR_TOTAL_TAX_CREDIT_CARD") 
	private Double DBL_BASE_CUR_TOTAL_TAX_CREDIT_CARD;
	
	@JsonProperty("DBL_SELLING_CUR_TOTAL_TAX_CREDIT_CARD") 
	private Double DBL_SELLING_CUR_TOTAL_TAX_CREDIT_CARD;
	
	@JsonProperty("DBL_PURCHASE_CUR_STD_COMMISION") 
	private Double DBL_PURCHASE_CUR_STD_COMMISION;
	
	@JsonProperty("DBL_BASE_CUR_STD_COMMISION") 
	private Double DBL_BASE_CUR_STD_COMMISION;
	
	@JsonProperty("DBL_SELLING_CUR_STD_COMMISION") 
	private Double DBL_SELLING_CUR_STD_COMMISION;
	
	@JsonProperty("DBL_PURCHASE_CUR_SUP_COMMISION") 
	private Double DBL_PURCHASE_CUR_SUP_COMMISION;
	
	@JsonProperty("DBL_BASE_CUR_SUP_COMMISION") 
	private Double DBL_BASE_CUR_SUP_COMMISION;
	
	@JsonProperty("DBL_SELLING_CUR_SUP_COMMISION") 
	private Double DBL_SELLING_CUR_SUP_COMMISION;
	
	@JsonProperty("DBL_PURCHASE_CUR_SUPPLIER_FEE") 
	private Double DBL_PURCHASE_CUR_SUPPLIER_FEE;
	
	@JsonProperty("DBL_BASE_CUR_SUPPLIER_FEE") 
	private Double DBL_BASE_CUR_SUPPLIER_FEE;
	
	@JsonProperty("DBL_SELLING_CUR_SUPPLIER_FEE") 
	private Double DBL_SELLING_CUR_SUPPLIER_FEE;
	
	@JsonProperty("DBL_PURCHASE_CUR_SUPPLIER_CHARGE") 
	private Double DBL_PURCHASE_CUR_SUPPLIER_CHARGE;
	
	@JsonProperty("DBL_BASE_CUR_SUPPLIER") 
	private Double DBL_BASE_CUR_SUPPLIER;
	
	@JsonProperty("DBL_SELLING_CUR_SUPPLIER") 
	private Double DBL_SELLING_CUR_SUPPLIER;
	
	@JsonProperty("DBL_PURCHASE_CUR_SUPPLIER_NET") 
	private Double DBL_PURCHASE_CUR_SUPPLIER_NET;
	
	@JsonProperty("DBL_BASE_CUR_SUPPLIER_NET") 
	private Double DBL_BASE_CUR_SUPPLIER_NET;
	
	@JsonProperty("DBL_SELLING_CUR_SUPPLIER_NET") 
	private Double DBL_SELLING_CUR_SUPPLIER_NET;
	
	@JsonProperty("DBL_PURCHASE_CUR_AGENCY_CHARGE") 
	private Double DBL_PURCHASE_CUR_AGENCY_CHARGE;
	
	@JsonProperty("DBL_BASE_CUR_AGENCY_CHARGE") 
	private Double DBL_BASE_CUR_AGENCY_CHARGE;
	
	@JsonProperty("DBL_SELLING_CUR_AGENCY_CHARGE") 
	private Double DBL_SELLING_CUR_AGENCY_CHARGE;
	
	@JsonProperty("DBL_PURCHASE_CUR_SUPPLIER_AMOUNT") 
	private Double DBL_PURCHASE_CUR_SUPPLIER_AMOUNT;
	
	@JsonProperty("DBL_BASE_CUR_SUPPLIER_AMOUNT") 
	private Double DBL_BASE_CUR_SUPPLIER_AMOUNT;
	
	@JsonProperty("DBL_SELLING_CUR_SUPPLIER_AMOUNT") 
	private Double DBL_SELLING_CUR_SUPPLIER_AMOUNT;
	
	@JsonProperty("DBL_PURCHASE_CUR_SERVICE_FEE") 
	private Double DBL_PURCHASE_CUR_SERVICE_FEE;
	
	@JsonProperty("DBL_BASE_CUR_SERVICE_FEE") 
	private Double DBL_BASE_CUR_SERVICE_FEE;
	
	@JsonProperty("DBL_SELLING_CUR_SERVICE_FEE") 
	private Double DBL_SELLING_CUR_SERVICE_FEE;
	
	@JsonProperty("DBL_PURCHASE_CUR_EXTRA_EARNING") 
	private Double DBL_PURCHASE_CUR_EXTRA_EARNING;
	
	@JsonProperty("DBL_BASE_CUR_EXTRA_EARNING") 
	private Double DBL_BASE_CUR_EXTRA_EARNING;
	
	@JsonProperty("DBL_SELLING_CUR_EXTRA_EARNING") 
	private Double DBL_SELLING_CUR_EXTRA_EARNING;
	
	@JsonProperty("DBL_PURCHASE_CUR_PAYBACK_AMOUNT") 
	private Double DBL_PURCHASE_CUR_PAYBACK_AMOUNT;
	
	@JsonProperty("DBL_BASE_CUR_PAYBACK_AMOUNT") 
	private Double DBL_BASE_CUR_PAYBACK_AMOUNT;
	
	@JsonProperty("DBL_SELLING_CUR_PAYBACK_AMOUNT") 
	private Double DBL_SELLING_CUR_PAYBACK_AMOUNT;
	
	@JsonProperty("STR_PAYBACK_ACCOUNT_CODE")
	private String STR_PAYBACK_ACCOUNT_CODE;
	
	@JsonProperty("DBL_PURCHASE_CUR_CC_CHARGES") 
	private Double DBL_PURCHASE_CUR_CC_CHARGES;
	
	@JsonProperty("DBL_BASE_CUR_CC_CHARGES") 
	private Double DBL_BASE_CUR_CC_CHARGES;
	
	@JsonProperty("DBL_SELLING_CUR_CC_CHARGES") 
	private Double DBL_SELLING_CUR_CC_CHARGES;
	
	@JsonProperty("DBL_PURCHASE_CUR_DISCOUNT") 
	private Double DBL_PURCHASE_CUR_DISCOUNT;
	
	@JsonProperty("DBL_BASE_CUR_DISCOUNT") 
	private Double DBL_BASE_CUR_DISCOUNT;
	
	@JsonProperty("DBL_SELLING_CUR_DISCOUNT") 
	private Double DBL_SELLING_CUR_DISCOUNT;
	
	@JsonProperty("DBL_PURCHASE_CUR_PRICE") 
	private Double DBL_PURCHASE_CUR_PRICE;
	
	@JsonProperty("DBL_BASE_CUR_PRICE") 
	private Double DBL_BASE_CUR_PRICE;
	
	@JsonProperty("DBL_SELLING_CUR_PRICE") 
	private Double DBL_SELLING_CUR_PRICE;
	
	@JsonProperty("DBL_PURCHASE_CUR_CLIENT_NET") 
	private Double DBL_PURCHASE_CUR_CLIENT_NET;
	
	@JsonProperty("DBL_BASE_CUR_CLIENT_NET") 
	private Double DBL_BASE_CUR_CLIENT_NET;
	
	@JsonProperty("DBL_SELLING_CUR_CLIENT_NET") 
	private Double DBL_SELLING_CUR_CLIENT_NET;
	
	@JsonProperty("DBL_PURCHASE_CUR_INPUT_VAT") 
	private Double DBL_PURCHASE_CUR_INPUT_VAT;
	
	@JsonProperty("DBL_BASE_CUR_INPUT_VAT") 
	private Double DBL_BASE_CUR_INPUT_VAT;
	
	@JsonProperty("DBL_SELLING_CUR_INPUT_VAT") 
	private Double DBL_SELLING_CUR_INPUT_VAT;
	
	@JsonProperty("DBL_PURCHASE_CUR_OUTPUT_VAT") 
	private Double DBL_PURCHASE_CUR_OUTPUT_VAT;
	
	@JsonProperty("DBL_BASE_CUR_OUTPUT_VAT") 
	private Double DBL_BASE_CUR_OUTPUT_VAT;
	
	@JsonProperty("DBL_SELLING_CUR_OUTPUT_VAT") 
	private Double DBL_SELLING_CUR_OUTPUT_VAT;
		
	@JsonProperty("Car_Details")
	private List<CarDetailsJson> Car_Details;
	

	public String getSTR_TYPE() {
		return STR_TYPE;
	}

	public void setSTR_TYPE(String sTR_TYPE) {
		STR_TYPE = sTR_TYPE;
	}

	public String getSTR_VOUCHER_NO() {
		return STR_VOUCHER_NO;
	}

	public void setSTR_VOUCHER_NO(String sTR_VOUCHER_NO) {
		STR_VOUCHER_NO = sTR_VOUCHER_NO;
	}

	public String getSTR_ISSUE_DATE() {
		return STR_ISSUE_DATE;
	}

	public void setSTR_ISSUE_DATE(String sTR_ISSUE_DATE) {
		STR_ISSUE_DATE = sTR_ISSUE_DATE;
	}

	public String getSTR_MODE_OF_PAYMENT() {
		return STR_MODE_OF_PAYMENT;
	}

	public void setSTR_MODE_OF_PAYMENT(String sTR_MODE_OF_PAYMENT) {
		STR_MODE_OF_PAYMENT = sTR_MODE_OF_PAYMENT;
	}

	public String getSTR_SUPPLIER_CODE() {
		return STR_SUPPLIER_CODE;
	}

	public void setSTR_SUPPLIER_CODE(String sTR_SUPPLIER_CODE) {
		STR_SUPPLIER_CODE = sTR_SUPPLIER_CODE;
	}

	public String getSTR_CORP_CREDIT_CARD_ACCOUNT_CODE() {
		return STR_CORP_CREDIT_CARD_ACCOUNT_CODE;
	}

	public void setSTR_CORP_CREDIT_CARD_ACCOUNT_CODE(String sTR_CORP_CREDIT_CARD_ACCOUNT_CODE) {
		STR_CORP_CREDIT_CARD_ACCOUNT_CODE = sTR_CORP_CREDIT_CARD_ACCOUNT_CODE;
	}

	public String getSTR_CUSTOMER_CORP_CREDIT_CARD_ACCOUNT_CODE() {
		return STR_CUSTOMER_CORP_CREDIT_CARD_ACCOUNT_CODE;
	}

	public void setSTR_CUSTOMER_CORP_CREDIT_CARD_ACCOUNT_CODE(String sTR_CUSTOMER_CORP_CREDIT_CARD_ACCOUNT_CODE) {
		STR_CUSTOMER_CORP_CREDIT_CARD_ACCOUNT_CODE = sTR_CUSTOMER_CORP_CREDIT_CARD_ACCOUNT_CODE;
	}

	public String getSTR_RENTAL_COMPANY_CODE() {
		return STR_RENTAL_COMPANY_CODE;
	}

	public void setSTR_RENTAL_COMPANY_CODE(String sTR_RENTAL_COMPANY_CODE) {
		STR_RENTAL_COMPANY_CODE = sTR_RENTAL_COMPANY_CODE;
	}

	public String getSTR_RENTING_STATION() {
		return STR_RENTING_STATION;
	}

	public void setSTR_RENTING_STATION(String sTR_RENTING_STATION) {
		STR_RENTING_STATION = sTR_RENTING_STATION;
	}

	public String getSTR_DROP_STATION() {
		return STR_DROP_STATION;
	}

	public void setSTR_DROP_STATION(String sTR_DROP_STATION) {
		STR_DROP_STATION = sTR_DROP_STATION;
	}

	public String getSTR_FROM_DATE() {
		return STR_FROM_DATE;
	}

	public void setSTR_FROM_DATE(String sTR_FROM_DATE) {
		STR_FROM_DATE = sTR_FROM_DATE;
	}

	public String getSTR_TO_DATE() {
		return STR_TO_DATE;
	}

	public void setSTR_TO_DATE(String sTR_TO_DATE) {
		STR_TO_DATE = sTR_TO_DATE;
	}

	public Integer getINT_NO_OF_CARS() {
		return INT_NO_OF_CARS;
	}

	public void setINT_NO_OF_CARS(Integer iNT_NO_OF_CARS) {
		INT_NO_OF_CARS = iNT_NO_OF_CARS;
	}

	public Integer getINT_NO_OF_DAYS() {
		return INT_NO_OF_DAYS;
	}

	public void setINT_NO_OF_DAYS(Integer iNT_NO_OF_DAYS) {
		INT_NO_OF_DAYS = iNT_NO_OF_DAYS;
	}

	public String getSTR_TRAVELER_ID() {
		return STR_TRAVELER_ID;
	}

	public void setSTR_TRAVELER_ID(String sTR_TRAVELER_ID) {
		STR_TRAVELER_ID = sTR_TRAVELER_ID;
	}

	public String getSTR_PAX_NAME() {
		return STR_PAX_NAME;
	}

	public void setSTR_PAX_NAME(String sTR_PAX_NAME) {
		STR_PAX_NAME = sTR_PAX_NAME;
	}

	public String getSTR_ADDITIONAL_PAX() {
		return STR_ADDITIONAL_PAX;
	}

	public void setSTR_ADDITIONAL_PAX(String sTR_ADDITIONAL_PAX) {
		STR_ADDITIONAL_PAX = sTR_ADDITIONAL_PAX;
	}

	public String getSTR_SUPPLIER_CONF_NUMBER() {
		return STR_SUPPLIER_CONF_NUMBER;
	}

	public void setSTR_SUPPLIER_CONF_NUMBER(String sTR_SUPPLIER_CONF_NUMBER) {
		STR_SUPPLIER_CONF_NUMBER = sTR_SUPPLIER_CONF_NUMBER;
	}

	public String getSTR_CAR_CONF_NO() {
		return STR_CAR_CONF_NO;
	}

	public void setSTR_CAR_CONF_NO(String sTR_CAR_CONF_NO) {
		STR_CAR_CONF_NO = sTR_CAR_CONF_NO;
	}

	public String getSTR_LPO_NO() {
		return STR_LPO_NO;
	}

	public void setSTR_LPO_NO(String sTR_LPO_NO) {
		STR_LPO_NO = sTR_LPO_NO;
	}

	public String getSTR_PNR_NO() {
		return STR_PNR_NO;
	}

	public void setSTR_PNR_NO(String sTR_PNR_NO) {
		STR_PNR_NO = sTR_PNR_NO;
	}

	public String getSTR_STAFF() {
		return STR_STAFF;
	}

	public void setSTR_STAFF(String sTR_STAFF) {
		STR_STAFF = sTR_STAFF;
	}

	public String getSTR_STAFF_EMAIL_ID() {
		return STR_STAFF_EMAIL_ID;
	}

	public void setSTR_STAFF_EMAIL_ID(String sTR_STAFF_EMAIL_ID) {
		STR_STAFF_EMAIL_ID = sTR_STAFF_EMAIL_ID;
	}

	public String getSTR_CUSTOMER_REF_NO() {
		return STR_CUSTOMER_REF_NO;
	}

	public void setSTR_CUSTOMER_REF_NO(String sTR_CUSTOMER_REF_NO) {
		STR_CUSTOMER_REF_NO = sTR_CUSTOMER_REF_NO;
	}

	public String getSTR_CUSTOMER_EMP_NO() {
		return STR_CUSTOMER_EMP_NO;
	}

	public void setSTR_CUSTOMER_EMP_NO(String sTR_CUSTOMER_EMP_NO) {
		STR_CUSTOMER_EMP_NO = sTR_CUSTOMER_EMP_NO;
	}

	public String getSTR_REMARKS() {
		return STR_REMARKS;
	}

	public void setSTR_REMARKS(String sTR_REMARKS) {
		STR_REMARKS = sTR_REMARKS;
	}

	public String getSTR_BOOKING_DETAILS() {
		return STR_BOOKING_DETAILS;
	}

	public void setSTR_BOOKING_DETAILS(String sTR_BOOKING_DETAILS) {
		STR_BOOKING_DETAILS = sTR_BOOKING_DETAILS;
	}

	public String getSTR_CATEGORY() {
		return STR_CATEGORY;
	}

	public void setSTR_CATEGORY(String sTR_CATEGORY) {
		STR_CATEGORY = sTR_CATEGORY;
	}

	public String getSTR_VEHICLE() {
		return STR_VEHICLE;
	}

	public void setSTR_VEHICLE(String sTR_VEHICLE) {
		STR_VEHICLE = sTR_VEHICLE;
	}

	public String getSTR_MODEL() {
		return STR_MODEL;
	}

	public void setSTR_MODEL(String sTR_MODEL) {
		STR_MODEL = sTR_MODEL;
	}

	public String getSTR_TRANSMISSION() {
		return STR_TRANSMISSION;
	}

	public void setSTR_TRANSMISSION(String sTR_TRANSMISSION) {
		STR_TRANSMISSION = sTR_TRANSMISSION;
	}

	public String getSTR_PRODUCT() {
		return STR_PRODUCT;
	}

	public void setSTR_PRODUCT(String sTR_PRODUCT) {
		STR_PRODUCT = sTR_PRODUCT;
	}

	public Double getDBL_PURCHASE_ROE() {
		return DBL_PURCHASE_ROE;
	}

	public void setDBL_PURCHASE_ROE(Double dBL_PURCHASE_ROE) {
		DBL_PURCHASE_ROE = dBL_PURCHASE_ROE;
	}

	public String getSTR_PURCHASE_CUR_CODE() {
		return STR_PURCHASE_CUR_CODE;
	}

	public void setSTR_PURCHASE_CUR_CODE(String sTR_PURCHASE_CUR_CODE) {
		STR_PURCHASE_CUR_CODE = sTR_PURCHASE_CUR_CODE;
	}

	public Double getDBL_PURCHASE_CUR_TOTAL_FARE() {
		return DBL_PURCHASE_CUR_TOTAL_FARE;
	}

	public void setDBL_PURCHASE_CUR_TOTAL_FARE(Double dBL_PURCHASE_CUR_TOTAL_FARE) {
		DBL_PURCHASE_CUR_TOTAL_FARE = dBL_PURCHASE_CUR_TOTAL_FARE;
	}

	public Double getDBL_BASE_CUR_TOTAL_FARE() {
		return DBL_BASE_CUR_TOTAL_FARE;
	}

	public void setDBL_BASE_CUR_TOTAL_FARE(Double dBL_BASE_CUR_TOTAL_FARE) {
		DBL_BASE_CUR_TOTAL_FARE = dBL_BASE_CUR_TOTAL_FARE;
	}

	public Double getDBL_SELLING_CUR_TOTAL_FARE() {
		return DBL_SELLING_CUR_TOTAL_FARE;
	}

	public void setDBL_SELLING_CUR_TOTAL_FARE(Double dBL_SELLING_CUR_TOTAL_FARE) {
		DBL_SELLING_CUR_TOTAL_FARE = dBL_SELLING_CUR_TOTAL_FARE;
	}

	public Double getDBL_PURCHASE_CUR_TOTAL_MARKET_FARE_CREDIT_CARD() {
		return DBL_PURCHASE_CUR_TOTAL_MARKET_FARE_CREDIT_CARD;
	}

	public void setDBL_PURCHASE_CUR_TOTAL_MARKET_FARE_CREDIT_CARD(Double dBL_PURCHASE_CUR_TOTAL_MARKET_FARE_CREDIT_CARD) {
		DBL_PURCHASE_CUR_TOTAL_MARKET_FARE_CREDIT_CARD = dBL_PURCHASE_CUR_TOTAL_MARKET_FARE_CREDIT_CARD;
	}

	public Double getDBL_BASE_CUR_TOTAL_MARKET_FARE_CREDIT_CARD() {
		return DBL_BASE_CUR_TOTAL_MARKET_FARE_CREDIT_CARD;
	}

	public void setDBL_BASE_CUR_TOTAL_MARKET_FARE_CREDIT_CARD(Double dBL_BASE_CUR_TOTAL_MARKET_FARE_CREDIT_CARD) {
		DBL_BASE_CUR_TOTAL_MARKET_FARE_CREDIT_CARD = dBL_BASE_CUR_TOTAL_MARKET_FARE_CREDIT_CARD;
	}

	public Double getDBL_SELLING_CUR_TOTAL_MARKET_FARE_CREDIT_CARD() {
		return DBL_SELLING_CUR_TOTAL_MARKET_FARE_CREDIT_CARD;
	}

	public void setDBL_SELLING_CUR_TOTAL_MARKET_FARE_CREDIT_CARD(Double dBL_SELLING_CUR_TOTAL_MARKET_FARE_CREDIT_CARD) {
		DBL_SELLING_CUR_TOTAL_MARKET_FARE_CREDIT_CARD = dBL_SELLING_CUR_TOTAL_MARKET_FARE_CREDIT_CARD;
	}

	public Double getDBL_PURCHASE_CUR_TOTAL_TAX() {
		return DBL_PURCHASE_CUR_TOTAL_TAX;
	}

	public void setDBL_PURCHASE_CUR_TOTAL_TAX(Double dBL_PURCHASE_CUR_TOTAL_TAX) {
		DBL_PURCHASE_CUR_TOTAL_TAX = dBL_PURCHASE_CUR_TOTAL_TAX;
	}

	public Double getDBL_BASE_CUR_TOTAL_TAX() {
		return DBL_BASE_CUR_TOTAL_TAX;
	}

	public void setDBL_BASE_CUR_TOTAL_TAX(Double dBL_BASE_CUR_TOTAL_TAX) {
		DBL_BASE_CUR_TOTAL_TAX = dBL_BASE_CUR_TOTAL_TAX;
	}

	public Double getDBL_SELLING_CUR_TOTAL_TAX() {
		return DBL_SELLING_CUR_TOTAL_TAX;
	}

	public void setDBL_SELLING_CUR_TOTAL_TAX(Double dBL_SELLING_CUR_TOTAL_TAX) {
		DBL_SELLING_CUR_TOTAL_TAX = dBL_SELLING_CUR_TOTAL_TAX;
	}

	public Double getDBL_PURCHASE_CUR_TOTAL_TAX_CREDIT_CARD() {
		return DBL_PURCHASE_CUR_TOTAL_TAX_CREDIT_CARD;
	}

	public void setDBL_PURCHASE_CUR_TOTAL_TAX_CREDIT_CARD(Double dBL_PURCHASE_CUR_TOTAL_TAX_CREDIT_CARD) {
		DBL_PURCHASE_CUR_TOTAL_TAX_CREDIT_CARD = dBL_PURCHASE_CUR_TOTAL_TAX_CREDIT_CARD;
	}

	public Double getDBL_BASE_CUR_TOTAL_TAX_CREDIT_CARD() {
		return DBL_BASE_CUR_TOTAL_TAX_CREDIT_CARD;
	}

	public void setDBL_BASE_CUR_TOTAL_TAX_CREDIT_CARD(Double dBL_BASE_CUR_TOTAL_TAX_CREDIT_CARD) {
		DBL_BASE_CUR_TOTAL_TAX_CREDIT_CARD = dBL_BASE_CUR_TOTAL_TAX_CREDIT_CARD;
	}

	public Double getDBL_SELLING_CUR_TOTAL_TAX_CREDIT_CARD() {
		return DBL_SELLING_CUR_TOTAL_TAX_CREDIT_CARD;
	}

	public void setDBL_SELLING_CUR_TOTAL_TAX_CREDIT_CARD(Double dBL_SELLING_CUR_TOTAL_TAX_CREDIT_CARD) {
		DBL_SELLING_CUR_TOTAL_TAX_CREDIT_CARD = dBL_SELLING_CUR_TOTAL_TAX_CREDIT_CARD;
	}

	public Double getDBL_PURCHASE_CUR_STD_COMMISION() {
		return DBL_PURCHASE_CUR_STD_COMMISION;
	}

	public void setDBL_PURCHASE_CUR_STD_COMMISION(Double dBL_PURCHASE_CUR_STD_COMMISION) {
		DBL_PURCHASE_CUR_STD_COMMISION = dBL_PURCHASE_CUR_STD_COMMISION;
	}

	public Double getDBL_BASE_CUR_STD_COMMISION() {
		return DBL_BASE_CUR_STD_COMMISION;
	}

	public void setDBL_BASE_CUR_STD_COMMISION(Double dBL_BASE_CUR_STD_COMMISION) {
		DBL_BASE_CUR_STD_COMMISION = dBL_BASE_CUR_STD_COMMISION;
	}

	public Double getDBL_SELLING_CUR_SUP_COMMISION() {
		return DBL_SELLING_CUR_SUP_COMMISION;
	}

	public void setDBL_SELLING_CUR_SUP_COMMISION(Double dBL_SELLING_CUR_SUP_COMMISION) {
		DBL_SELLING_CUR_SUP_COMMISION = dBL_SELLING_CUR_SUP_COMMISION;
	}

	public Double getDBL_PURCHASE_CUR_SUPPLIER_FEE() {
		return DBL_PURCHASE_CUR_SUPPLIER_FEE;
	}

	public void setDBL_PURCHASE_CUR_SUPPLIER_FEE(Double dBL_PURCHASE_CUR_SUPPLIER_FEE) {
		DBL_PURCHASE_CUR_SUPPLIER_FEE = dBL_PURCHASE_CUR_SUPPLIER_FEE;
	}

	public Double getDBL_BASE_CUR_SUPPLIER_FEE() {
		return DBL_BASE_CUR_SUPPLIER_FEE;
	}

	public void setDBL_BASE_CUR_SUPPLIER_FEE(Double dBL_BASE_CUR_SUPPLIER_FEE) {
		DBL_BASE_CUR_SUPPLIER_FEE = dBL_BASE_CUR_SUPPLIER_FEE;
	}

	public Double getDBL_SELLING_CUR_SUPPLIER_FEE() {
		return DBL_SELLING_CUR_SUPPLIER_FEE;
	}

	public void setDBL_SELLING_CUR_SUPPLIER_FEE(Double dBL_SELLING_CUR_SUPPLIER_FEE) {
		DBL_SELLING_CUR_SUPPLIER_FEE = dBL_SELLING_CUR_SUPPLIER_FEE;
	}

	public Double getDBL_PURCHASE_CUR_SUPPLIER_AMOUNT() {
		return DBL_PURCHASE_CUR_SUPPLIER_AMOUNT;
	}

	public void setDBL_PURCHASE_CUR_SUPPLIER_AMOUNT(Double dBL_PURCHASE_CUR_SUPPLIER_AMOUNT) {
		DBL_PURCHASE_CUR_SUPPLIER_AMOUNT = dBL_PURCHASE_CUR_SUPPLIER_AMOUNT;
	}

	public Double getDBL_BASE_CUR_SUPPLIER_AMOUNT() {
		return DBL_BASE_CUR_SUPPLIER_AMOUNT;
	}

	public void setDBL_BASE_CUR_SUPPLIER_AMOUNT(Double dBL_BASE_CUR_SUPPLIER_AMOUNT) {
		DBL_BASE_CUR_SUPPLIER_AMOUNT = dBL_BASE_CUR_SUPPLIER_AMOUNT;
	}

	public Double getDBL_SELLING_CUR_SUPPLIER_AMOUNT() {
		return DBL_SELLING_CUR_SUPPLIER_AMOUNT;
	}

	public void setDBL_SELLING_CUR_SUPPLIER_AMOUNT(Double dBL_SELLING_CUR_SUPPLIER_AMOUNT) {
		DBL_SELLING_CUR_SUPPLIER_AMOUNT = dBL_SELLING_CUR_SUPPLIER_AMOUNT;
	}

	public Double getDBL_PURCHASE_CUR_SERVICE_FEE() {
		return DBL_PURCHASE_CUR_SERVICE_FEE;
	}

	public void setDBL_PURCHASE_CUR_SERVICE_FEE(Double dBL_PURCHASE_CUR_SERVICE_FEE) {
		DBL_PURCHASE_CUR_SERVICE_FEE = dBL_PURCHASE_CUR_SERVICE_FEE;
	}

	public Double getDBL_BASE_CUR_SERVICE_FEE() {
		return DBL_BASE_CUR_SERVICE_FEE;
	}

	public void setDBL_BASE_CUR_SERVICE_FEE(Double dBL_BASE_CUR_SERVICE_FEE) {
		DBL_BASE_CUR_SERVICE_FEE = dBL_BASE_CUR_SERVICE_FEE;
	}

	public Double getDBL_SELLING_CUR_SERVICE_FEE() {
		return DBL_SELLING_CUR_SERVICE_FEE;
	}

	public void setDBL_SELLING_CUR_SERVICE_FEE(Double dBL_SELLING_CUR_SERVICE_FEE) {
		DBL_SELLING_CUR_SERVICE_FEE = dBL_SELLING_CUR_SERVICE_FEE;
	}

	public Double getDBL_PURCHASE_CUR_EXTRA_EARNING() {
		return DBL_PURCHASE_CUR_EXTRA_EARNING;
	}

	public void setDBL_PURCHASE_CUR_EXTRA_EARNING(Double dBL_PURCHASE_CUR_EXTRA_EARNING) {
		DBL_PURCHASE_CUR_EXTRA_EARNING = dBL_PURCHASE_CUR_EXTRA_EARNING;
	}

	public Double getDBL_BASE_CUR_EXTRA_EARNING() {
		return DBL_BASE_CUR_EXTRA_EARNING;
	}

	public void setDBL_BASE_CUR_EXTRA_EARNING(Double dBL_BASE_CUR_EXTRA_EARNING) {
		DBL_BASE_CUR_EXTRA_EARNING = dBL_BASE_CUR_EXTRA_EARNING;
	}

	public Double getDBL_SELLING_CUR_EXTRA_EARNING() {
		return DBL_SELLING_CUR_EXTRA_EARNING;
	}

	public void setDBL_SELLING_CUR_EXTRA_EARNING(Double dBL_SELLING_CUR_EXTRA_EARNING) {
		DBL_SELLING_CUR_EXTRA_EARNING = dBL_SELLING_CUR_EXTRA_EARNING;
	}

	public Double getDBL_PURCHASE_CUR_PAYBACK_AMOUNT() {
		return DBL_PURCHASE_CUR_PAYBACK_AMOUNT;
	}

	public void setDBL_PURCHASE_CUR_PAYBACK_AMOUNT(Double dBL_PURCHASE_CUR_PAYBACK_AMOUNT) {
		DBL_PURCHASE_CUR_PAYBACK_AMOUNT = dBL_PURCHASE_CUR_PAYBACK_AMOUNT;
	}

	public Double getDBL_BASE_CUR_PAYBACK_AMOUNT() {
		return DBL_BASE_CUR_PAYBACK_AMOUNT;
	}

	public void setDBL_BASE_CUR_PAYBACK_AMOUNT(Double dBL_BASE_CUR_PAYBACK_AMOUNT) {
		DBL_BASE_CUR_PAYBACK_AMOUNT = dBL_BASE_CUR_PAYBACK_AMOUNT;
	}

	public Double getDBL_SELLING_CUR_PAYBACK_AMOUNT() {
		return DBL_SELLING_CUR_PAYBACK_AMOUNT;
	}

	public void setDBL_SELLING_CUR_PAYBACK_AMOUNT(Double dBL_SELLING_CUR_PAYBACK_AMOUNT) {
		DBL_SELLING_CUR_PAYBACK_AMOUNT = dBL_SELLING_CUR_PAYBACK_AMOUNT;
	}

	public String getSTR_PAYBACK_ACCOUNT_CODE() {
		return STR_PAYBACK_ACCOUNT_CODE;
	}

	public void setSTR_PAYBACK_ACCOUNT_CODE(String sTR_PAYBACK_ACCOUNT_CODE) {
		STR_PAYBACK_ACCOUNT_CODE = sTR_PAYBACK_ACCOUNT_CODE;
	}

	public Double getDBL_PURCHASE_CUR_CC_CHARGES() {
		return DBL_PURCHASE_CUR_CC_CHARGES;
	}

	public void setDBL_PURCHASE_CUR_CC_CHARGES(Double dBL_PURCHASE_CUR_CC_CHARGES) {
		DBL_PURCHASE_CUR_CC_CHARGES = dBL_PURCHASE_CUR_CC_CHARGES;
	}

	public Double getDBL_BASE_CUR_CC_CHARGES() {
		return DBL_BASE_CUR_CC_CHARGES;
	}

	public void setDBL_BASE_CUR_CC_CHARGES(Double dBL_BASE_CUR_CC_CHARGES) {
		DBL_BASE_CUR_CC_CHARGES = dBL_BASE_CUR_CC_CHARGES;
	}

	public Double getDBL_SELLING_CUR_CC_CHARGES() {
		return DBL_SELLING_CUR_CC_CHARGES;
	}

	public void setDBL_SELLING_CUR_CC_CHARGES(Double dBL_SELLING_CUR_CC_CHARGES) {
		DBL_SELLING_CUR_CC_CHARGES = dBL_SELLING_CUR_CC_CHARGES;
	}

	public Double getDBL_PURCHASE_CUR_DISCOUNT() {
		return DBL_PURCHASE_CUR_DISCOUNT;
	}

	public void setDBL_PURCHASE_CUR_DISCOUNT(Double dBL_PURCHASE_CUR_DISCOUNT) {
		DBL_PURCHASE_CUR_DISCOUNT = dBL_PURCHASE_CUR_DISCOUNT;
	}

	public Double getDBL_BASE_CUR_DISCOUNT() {
		return DBL_BASE_CUR_DISCOUNT;
	}

	public void setDBL_BASE_CUR_DISCOUNT(Double dBL_BASE_CUR_DISCOUNT) {
		DBL_BASE_CUR_DISCOUNT = dBL_BASE_CUR_DISCOUNT;
	}

	public Double getDBL_SELLING_CUR_DISCOUNT() {
		return DBL_SELLING_CUR_DISCOUNT;
	}

	public void setDBL_SELLING_CUR_DISCOUNT(Double dBL_SELLING_CUR_DISCOUNT) {
		DBL_SELLING_CUR_DISCOUNT = dBL_SELLING_CUR_DISCOUNT;
	}

	public Double getDBL_PURCHASE_CUR_PRICE() {
		return DBL_PURCHASE_CUR_PRICE;
	}

	public void setDBL_PURCHASE_CUR_PRICE(Double dBL_PURCHASE_CUR_PRICE) {
		DBL_PURCHASE_CUR_PRICE = dBL_PURCHASE_CUR_PRICE;
	}

	public Double getDBL_BASE_CUR_PRICE() {
		return DBL_BASE_CUR_PRICE;
	}

	public void setDBL_BASE_CUR_PRICE(Double dBL_BASE_CUR_PRICE) {
		DBL_BASE_CUR_PRICE = dBL_BASE_CUR_PRICE;
	}

	public Double getDBL_SELLING_CUR_PRICE() {
		return DBL_SELLING_CUR_PRICE;
	}

	public void setDBL_SELLING_CUR_PRICE(Double dBL_SELLING_CUR_PRICE) {
		DBL_SELLING_CUR_PRICE = dBL_SELLING_CUR_PRICE;
	}

	public String getSTR_REFUND_DATE() {
		return STR_REFUND_DATE;
	}

	public void setSTR_REFUND_DATE(String sTR_REFUND_DATE) {
		STR_REFUND_DATE = sTR_REFUND_DATE;
	}

	public String getSTR_REFUND_STAFF() {
		return STR_REFUND_STAFF;
	}

	public void setSTR_REFUND_STAFF(String sTR_REFUND_STAFF) {
		STR_REFUND_STAFF = sTR_REFUND_STAFF;
	}

	public String getSTR_REFUND_STAFF_EMAIL_ID() {
		return STR_REFUND_STAFF_EMAIL_ID;
	}

	public void setSTR_REFUND_STAFF_EMAIL_ID(String sTR_REFUND_STAFF_EMAIL_ID) {
		STR_REFUND_STAFF_EMAIL_ID = sTR_REFUND_STAFF_EMAIL_ID;
	}

	public String getSTR_REFUND_STATUS() {
		return STR_REFUND_STATUS;
	}

	public void setSTR_REFUND_STATUS(String sTR_REFUND_STATUS) {
		STR_REFUND_STATUS = sTR_REFUND_STATUS;
	}

	public Double getDBL_SELLING_CUR_STD_COMMISION() {
		return DBL_SELLING_CUR_STD_COMMISION;
	}

	public void setDBL_SELLING_CUR_STD_COMMISION(Double dBL_SELLING_CUR_STD_COMMISION) {
		DBL_SELLING_CUR_STD_COMMISION = dBL_SELLING_CUR_STD_COMMISION;
	}

	public Double getDBL_PURCHASE_CUR_SUP_COMMISION() {
		return DBL_PURCHASE_CUR_SUP_COMMISION;
	}

	public void setDBL_PURCHASE_CUR_SUP_COMMISION(Double dBL_PURCHASE_CUR_SUP_COMMISION) {
		DBL_PURCHASE_CUR_SUP_COMMISION = dBL_PURCHASE_CUR_SUP_COMMISION;
	}

	public Double getDBL_BASE_CUR_SUP_COMMISION() {
		return DBL_BASE_CUR_SUP_COMMISION;
	}

	public void setDBL_BASE_CUR_SUP_COMMISION(Double dBL_BASE_CUR_SUP_COMMISION) {
		DBL_BASE_CUR_SUP_COMMISION = dBL_BASE_CUR_SUP_COMMISION;
	}

	public Double getDBL_PURCHASE_CUR_SUPPLIER_CHARGE() {
		return DBL_PURCHASE_CUR_SUPPLIER_CHARGE;
	}

	public void setDBL_PURCHASE_CUR_SUPPLIER_CHARGE(Double dBL_PURCHASE_CUR_SUPPLIER_CHARGE) {
		DBL_PURCHASE_CUR_SUPPLIER_CHARGE = dBL_PURCHASE_CUR_SUPPLIER_CHARGE;
	}

	public Double getDBL_BASE_CUR_SUPPLIER() {
		return DBL_BASE_CUR_SUPPLIER;
	}

	public void setDBL_BASE_CUR_SUPPLIER(Double dBL_BASE_CUR_SUPPLIER) {
		DBL_BASE_CUR_SUPPLIER = dBL_BASE_CUR_SUPPLIER;
	}

	public Double getDBL_SELLING_CUR_SUPPLIER() {
		return DBL_SELLING_CUR_SUPPLIER;
	}

	public void setDBL_SELLING_CUR_SUPPLIER(Double dBL_SELLING_CUR_SUPPLIER) {
		DBL_SELLING_CUR_SUPPLIER = dBL_SELLING_CUR_SUPPLIER;
	}

	public Double getDBL_PURCHASE_CUR_SUPPLIER_NET() {
		return DBL_PURCHASE_CUR_SUPPLIER_NET;
	}

	public void setDBL_PURCHASE_CUR_SUPPLIER_NET(Double dBL_PURCHASE_CUR_SUPPLIER_NET) {
		DBL_PURCHASE_CUR_SUPPLIER_NET = dBL_PURCHASE_CUR_SUPPLIER_NET;
	}

	public Double getDBL_BASE_CUR_SUPPLIER_NET() {
		return DBL_BASE_CUR_SUPPLIER_NET;
	}

	public void setDBL_BASE_CUR_SUPPLIER_NET(Double dBL_BASE_CUR_SUPPLIER_NET) {
		DBL_BASE_CUR_SUPPLIER_NET = dBL_BASE_CUR_SUPPLIER_NET;
	}

	public Double getDBL_SELLING_CUR_SUPPLIER_NET() {
		return DBL_SELLING_CUR_SUPPLIER_NET;
	}

	public void setDBL_SELLING_CUR_SUPPLIER_NET(Double dBL_SELLING_CUR_SUPPLIER_NET) {
		DBL_SELLING_CUR_SUPPLIER_NET = dBL_SELLING_CUR_SUPPLIER_NET;
	}

	public Double getDBL_PURCHASE_CUR_AGENCY_CHARGE() {
		return DBL_PURCHASE_CUR_AGENCY_CHARGE;
	}

	public void setDBL_PURCHASE_CUR_AGENCY_CHARGE(Double dBL_PURCHASE_CUR_AGENCY_CHARGE) {
		DBL_PURCHASE_CUR_AGENCY_CHARGE = dBL_PURCHASE_CUR_AGENCY_CHARGE;
	}

	public Double getDBL_BASE_CUR_AGENCY_CHARGE() {
		return DBL_BASE_CUR_AGENCY_CHARGE;
	}

	public void setDBL_BASE_CUR_AGENCY_CHARGE(Double dBL_BASE_CUR_AGENCY_CHARGE) {
		DBL_BASE_CUR_AGENCY_CHARGE = dBL_BASE_CUR_AGENCY_CHARGE;
	}

	public Double getDBL_SELLING_CUR_AGENCY_CHARGE() {
		return DBL_SELLING_CUR_AGENCY_CHARGE;
	}

	public void setDBL_SELLING_CUR_AGENCY_CHARGE(Double dBL_SELLING_CUR_AGENCY_CHARGE) {
		DBL_SELLING_CUR_AGENCY_CHARGE = dBL_SELLING_CUR_AGENCY_CHARGE;
	}

	public Double getDBL_PURCHASE_CUR_CLIENT_NET() {
		return DBL_PURCHASE_CUR_CLIENT_NET;
	}

	public void setDBL_PURCHASE_CUR_CLIENT_NET(Double dBL_PURCHASE_CUR_CLIENT_NET) {
		DBL_PURCHASE_CUR_CLIENT_NET = dBL_PURCHASE_CUR_CLIENT_NET;
	}

	public Double getDBL_BASE_CUR_CLIENT_NET() {
		return DBL_BASE_CUR_CLIENT_NET;
	}

	public void setDBL_BASE_CUR_CLIENT_NET(Double dBL_BASE_CUR_CLIENT_NET) {
		DBL_BASE_CUR_CLIENT_NET = dBL_BASE_CUR_CLIENT_NET;
	}

	public Double getDBL_SELLING_CUR_CLIENT_NET() {
		return DBL_SELLING_CUR_CLIENT_NET;
	}

	public void setDBL_SELLING_CUR_CLIENT_NET(Double dBL_SELLING_CUR_CLIENT_NET) {
		DBL_SELLING_CUR_CLIENT_NET = dBL_SELLING_CUR_CLIENT_NET;
	}

	public List<CarDetailsJson> getCar_Details() {
		return Car_Details;
	}

	public void setCar_Details(List<CarDetailsJson> car_Details) {
		Car_Details = car_Details;
	}

	public Double getDBL_PURCHASE_CUR_TOTAL_MARKET_FARE() {
		return DBL_PURCHASE_CUR_TOTAL_MARKET_FARE;
	}

	public void setDBL_PURCHASE_CUR_TOTAL_MARKET_FARE(Double dBL_PURCHASE_CUR_TOTAL_MARKET_FARE) {
		DBL_PURCHASE_CUR_TOTAL_MARKET_FARE = dBL_PURCHASE_CUR_TOTAL_MARKET_FARE;
	}

	public Double getDBL_PURCHASE_CUR_INPUT_VAT() {
		return DBL_PURCHASE_CUR_INPUT_VAT;
	}

	public void setDBL_PURCHASE_CUR_INPUT_VAT(Double dBL_PURCHASE_CUR_INPUT_VAT) {
		DBL_PURCHASE_CUR_INPUT_VAT = dBL_PURCHASE_CUR_INPUT_VAT;
	}

	public Double getDBL_BASE_CUR_INPUT_VAT() {
		return DBL_BASE_CUR_INPUT_VAT;
	}

	public void setDBL_BASE_CUR_INPUT_VAT(Double dBL_BASE_CUR_INPUT_VAT) {
		DBL_BASE_CUR_INPUT_VAT = dBL_BASE_CUR_INPUT_VAT;
	}

	public Double getDBL_SELLING_CUR_INPUT_VAT() {
		return DBL_SELLING_CUR_INPUT_VAT;
	}

	public void setDBL_SELLING_CUR_INPUT_VAT(Double dBL_SELLING_CUR_INPUT_VAT) {
		DBL_SELLING_CUR_INPUT_VAT = dBL_SELLING_CUR_INPUT_VAT;
	}

	public Double getDBL_PURCHASE_CUR_OUTPUT_VAT() {
		return DBL_PURCHASE_CUR_OUTPUT_VAT;
	}

	public void setDBL_PURCHASE_CUR_OUTPUT_VAT(Double dBL_PURCHASE_CUR_OUTPUT_VAT) {
		DBL_PURCHASE_CUR_OUTPUT_VAT = dBL_PURCHASE_CUR_OUTPUT_VAT;
	}

	public Double getDBL_BASE_CUR_OUTPUT_VAT() {
		return DBL_BASE_CUR_OUTPUT_VAT;
	}

	public void setDBL_BASE_CUR_OUTPUT_VAT(Double dBL_BASE_CUR_OUTPUT_VAT) {
		DBL_BASE_CUR_OUTPUT_VAT = dBL_BASE_CUR_OUTPUT_VAT;
	}

	public Double getDBL_SELLING_CUR_OUTPUT_VAT() {
		return DBL_SELLING_CUR_OUTPUT_VAT;
	}

	public void setDBL_SELLING_CUR_OUTPUT_VAT(Double dBL_SELLING_CUR_OUTPUT_VAT) {
		DBL_SELLING_CUR_OUTPUT_VAT = dBL_SELLING_CUR_OUTPUT_VAT;
	}
}
