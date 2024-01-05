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
public class CarDetailsJson implements Serializable {
	
	/** *  */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("STR_FROM_DATE")
	private String STR_FROM_DATE;
	
	@JsonProperty("STR_TO_DATE")
	private String STR_TO_DATE;
	
	@JsonProperty("INT_NO_OF_CARS")
	private Integer INT_NO_OF_CARS;
	
	@JsonProperty("INT_NO_OF_DAYS")
	private Integer INT_NO_OF_DAYS;
	
	@JsonProperty("STR_RENTING_PERIOD")
	private String STR_RENTING_PERIOD;
	
	@JsonProperty("STR_CATEGORY")
	private String STR_CATEGORY;

	@JsonProperty("STR_VEHICLE")
	private String STR_VEHICLE;
	
	@JsonProperty("STR_MODEL")
	private String STR_MODEL;
	
	@JsonProperty("STR_TRANSMISSION")
	private String STR_TRANSMISSION;
	
	@JsonProperty("STR_PAX_NAME")
	private String STR_PAX_NAME;
	
	@JsonProperty("DBL_PURCHASE_CUR_RATE_PER_DAY") 
	private Double DBL_PURCHASE_CUR_RATE_PER_DAY;
	
	@JsonProperty("DBL_BASE_CUR_RATE_PER_DAY") 
	private Double DBL_BASE_CUR_RATE_PER_DAY;
	
	@JsonProperty("DBL_SELLING_CUR_RATE_PER_DAY") 
	private Double DBL_SELLING_CUR_RATE_PER_DAY;
	
	@JsonProperty("DBL_PURCHASE_CUR_TAX_PER_DAY") 
	private Double DBL_PURCHASE_CUR_TAX_PER_DAY;
	
	@JsonProperty("DBL_BASE_CUR_TAX_PER_DAY") 
	private Double DBL_BASE_CUR_TAX_PER_DAY;
	
	@JsonProperty("DBL_SELLING_CUR_TAX_PER_DAY") 
	private Double DBL_SELLING_CUR_TAX_PER_DAY;
	
	@JsonProperty("DBL_PURCHASE_CUR_SUP_COMMISION") 
	private Double DBL_PURCHASE_CUR_SUP_COMMISION;
	
	@JsonProperty("DBL_BASE_CUR_SUP_COMMISION") 
	private Double DBL_BASE_CUR_SUP_COMMISION;
	
	@JsonProperty("DBL_SELLING_CUR_SUP_COMMISION") 
	private Double DBL_SELLING_CUR_SUP_COMMISION;
	
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

	public String getSTR_PAX_NAME() {
		return STR_PAX_NAME;
	}

	public void setSTR_PAX_NAME(String sTR_PAX_NAME) {
		STR_PAX_NAME = sTR_PAX_NAME;
	}

	public Double getDBL_PURCHASE_CUR_RATE_PER_DAY() {
		return DBL_PURCHASE_CUR_RATE_PER_DAY;
	}

	public void setDBL_PURCHASE_CUR_RATE_PER_DAY(Double dBL_PURCHASE_CUR_RATE_PER_DAY) {
		DBL_PURCHASE_CUR_RATE_PER_DAY = dBL_PURCHASE_CUR_RATE_PER_DAY;
	}

	public Double getDBL_BASE_CUR_RATE_PER_DAY() {
		return DBL_BASE_CUR_RATE_PER_DAY;
	}

	public void setDBL_BASE_CUR_RATE_PER_DAY(Double dBL_BASE_CUR_RATE_PER_DAY) {
		DBL_BASE_CUR_RATE_PER_DAY = dBL_BASE_CUR_RATE_PER_DAY;
	}

	public Double getDBL_SELLING_CUR_RATE_PER_DAY() {
		return DBL_SELLING_CUR_RATE_PER_DAY;
	}

	public void setDBL_SELLING_CUR_RATE_PER_DAY(Double dBL_SELLING_CUR_RATE_PER_DAY) {
		DBL_SELLING_CUR_RATE_PER_DAY = dBL_SELLING_CUR_RATE_PER_DAY;
	}

	public Double getDBL_PURCHASE_CUR_TAX_PER_DAY() {
		return DBL_PURCHASE_CUR_TAX_PER_DAY;
	}

	public void setDBL_PURCHASE_CUR_TAX_PER_DAY(Double dBL_PURCHASE_CUR_TAX_PER_DAY) {
		DBL_PURCHASE_CUR_TAX_PER_DAY = dBL_PURCHASE_CUR_TAX_PER_DAY;
	}

	public Double getDBL_BASE_CUR_TAX_PER_DAY() {
		return DBL_BASE_CUR_TAX_PER_DAY;
	}

	public void setDBL_BASE_CUR_TAX_PER_DAY(Double dBL_BASE_CUR_TAX_PER_DAY) {
		DBL_BASE_CUR_TAX_PER_DAY = dBL_BASE_CUR_TAX_PER_DAY;
	}

	public Double getDBL_SELLING_CUR_TAX_PER_DAY() {
		return DBL_SELLING_CUR_TAX_PER_DAY;
	}

	public void setDBL_SELLING_CUR_TAX_PER_DAY(Double dBL_SELLING_CUR_TAX_PER_DAY) {
		DBL_SELLING_CUR_TAX_PER_DAY = dBL_SELLING_CUR_TAX_PER_DAY;
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

	public Double getDBL_SELLING_CUR_SUP_COMMISION() {
		return DBL_SELLING_CUR_SUP_COMMISION;
	}

	public void setDBL_SELLING_CUR_SUP_COMMISION(Double dBL_SELLING_CUR_SUP_COMMISION) {
		DBL_SELLING_CUR_SUP_COMMISION = dBL_SELLING_CUR_SUP_COMMISION;
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

	/**
	 * @return the sTR_RENTING_PERIOD
	 */
	public String getSTR_RENTING_PERIOD() {
		return STR_RENTING_PERIOD;
	}

	/**
	 * @param sTR_RENTING_PERIOD the sTR_RENTING_PERIOD to set
	 */
	public void setSTR_RENTING_PERIOD(String sTR_RENTING_PERIOD) {
		STR_RENTING_PERIOD = sTR_RENTING_PERIOD;
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
