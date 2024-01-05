package com.ebreez.etrcs.traacs.util;

public class MapperUtil {

	public static Double multiplyValues(Double in1, Double in2) {
		Double value = null;
		if(in1 != null && in2 != null){
			value = in1 * in2;
			value = Math.round(value * 100D) / 100D;
		}
		return value;
	}
	
	public static String checkEmptyOrNullString( String inputString ) {
		String output = null;
		if( inputString == null || ( inputString.trim().length() == 0 ) )	{
			output =  new String();
		}
		
		return output;
	}

	public static String getDoubleAsCheckedString( Double aDouble )	{
		String checkedString = new String();
		
		if( aDouble == null || ( aDouble.doubleValue() == 0.0 ) ){
			checkedString = new String();
		}
		else {
			checkedString = checkedString + aDouble.doubleValue();
		}
		
		return checkedString;
	}
	
	public static String getTraacsPaymentMode(String mode){
		
		if(mode != null){
			switch(mode){
			case "BANK TRANSFER":  
				 return "Bank";
			case "CARD":  
				 return "Card"; 
			case "INVOICE_TO_COMPANY":  
				 return "Cash"; 
			case "CASH":  
				 return "Cash"; 
			case "CHEQUE":  
				return "Cash"; 
			case "SALARY_LINKED_PAY":  
			    return "Cash";
			default:
				return "Cash";
			}
		}else{
			return mode;
		}
	}
	
	public static String getRespStatus(int resposeCode){

		
			switch(resposeCode){
			case 0:  
				 return "PENDING";
			case 1:  
				 return "SUCCESS";
			case 2:  
				 return "UPDATED";
			case 3:
				 return "SOLD";
			case 6:
				return "MISMATCH";
			case 7:
				return "REQUESTED";
			case 9:
				return "TIME_OUT";
			default:
				return "PENDING";
			}
		
	
	}
}
