/**
 * 
 */
package com.ebreez.etrcs.traacs.processor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.runtime.api.component.execution.ExecutableComponent;
import org.mule.runtime.api.event.Event;
import org.mule.runtime.api.event.EventContext;
import org.mule.runtime.api.message.Message;
import org.springframework.util.StringUtils;

import com.ebreez.etrcs.constants.ETRCSConstants;
import com.ebreez.etrcs.request.vo.TraacsInfoRequest;
import com.ebreez.etrcs.traacs.request.vo.MasterJson;
import com.ebreez.etrcs.traacs.vo.HotelJsonMaster;
import com.ebreez.etrcs.traacs.vo.OtherJsonMaster;
import com.ebreez.etrcs.traacs.vo.TicketJsonMaster;


public abstract class BuildPrimaryTraacsRequest implements ExecutableComponent {
	
	private static Log logger = LogFactory.getLog(ETRCSConstants.LOGGER_NAME);
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public Object onCall(EventContext eventContext) throws Exception {
		
		Message msg = ((Event) eventContext).getMessage();
		MasterJson payloadJson = (MasterJson) msg.getPayload().getValue();
		MasterJson traacs = (MasterJson) deepClone(payloadJson);
		Map<String, String> suppllierIdsMap = (Map<String, String>) ((Event) msg).getVariables().get(ETRCSConstants.VAR_SUPPLIER_DATA_MAP).getValue();
		Map<String, String> properties = (Map<String, String>) ((Event) eventContext).getVariables().get(ETRCSConstants.BEAN_ETRCS_APP_PROPERTIES).getValue();
		//Map<String, String> properties = eventContext.getMuleContext().getRegistry().get(ETRCSConstants.BEAN_ETRCS_APP_PROPERTIES);

		TraacsInfoRequest tcsReq = (TraacsInfoRequest)((Event) msg).getVariables().get(ETRCSConstants.VAR_UPDATE_REQUEST).getValue();
		String primServ = ((Event) msg).getVariables().get(ETRCSConstants.TRAACS_PRIMARY_DOMAIN).getValue().toString();
		String secServ = ((Event) msg).getVariables().get(ETRCSConstants.TRAACS_SECONDARY_DOMAIN).getValue().toString();
		String crossJson = ((Event) msg).getVariables().get(ETRCSConstants.TRAACS_CROSS_JSON).getValue().toString();
		
		String lpo = null;
		if(null != traacs){
			if(null != traacs.getJson_Ticket()){
				lpo = traacs.getJson_Ticket().get(0).getSTR_LPO_NO();
			}else if(null != traacs.getJson_Hotel()){
				lpo = traacs.getJson_Hotel().get(0).getSTR_LPO_NO();
			}else if(null != traacs.getJson_Car()){
				lpo = traacs.getJson_Car().get(0).getSTR_LPO_NO();
			}else if(null != traacs.getJson_Other()){
				lpo = traacs.getJson_Other().get(0).getSTR_LPO_NO();
			}
		}
		logger.info("BuildPrimaryTraacsRequest for Booking : " + lpo + " START");
		
		if(null != primServ){
			if(null != traacs && null != traacs.getStr_authentication_key()){
				if(null != primServ && primServ.equalsIgnoreCase("EGYPT")){
					traacs.getStr_authentication_key().setSTR_USER_NAME(properties.get(ETRCSConstants.TRAACS_USERNAME_EGYPT));
					traacs.getStr_authentication_key().setSTR_PASSWORD(properties.get(ETRCSConstants.TRAACS_PASSWORD_EGYPT));
				}else{
					traacs.getStr_authentication_key().setSTR_USER_NAME(properties.get(ETRCSConstants.TRAACS_USERNAME_GLOBAL));
					traacs.getStr_authentication_key().setSTR_PASSWORD(properties.get(ETRCSConstants.TRAACS_PASSWORD_GLOBAL));
				}
			}
			
			if(null != traacs.getJson_master()){
				if(null != primServ && primServ.equalsIgnoreCase("EGYPT")){
					
					String narration = traacs.getJson_master().getSTR_NARRATION();
					if(null != narration){
						traacs.getJson_master().setSTR_NARRATION("ICE" + " | " + narration);
					}
				}else{
					String narration = traacs.getJson_master().getSTR_NARRATION();
					if(null != narration){
						traacs.getJson_master().setSTR_NARRATION("ICK" + " | " + narration);
					}
				}
			}
			
			if(null != traacs.getJson_Ticket() && !traacs.getJson_Ticket().isEmpty()){
				for(TicketJsonMaster tkt : traacs.getJson_Ticket()){
					if((null != secServ && null != primServ && !primServ.equalsIgnoreCase(secServ)) || (null != crossJson && crossJson.equals("true"))){
						String sc = tkt.getSTR_SUPPLIER_CODE() != null ? tkt.getSTR_SUPPLIER_CODE().toUpperCase() : "";
						String suppCodeSec = suppllierIdsMap.get(sc);
						
						if((null != tkt.getSTR_GDS() && tkt.getSTR_GDS().equalsIgnoreCase("Amadeus")) && (null != primServ && primServ.equals("EGYPT")) && 
								(null != tkt.getSTR_TICKETING_AGENCY_OFFICE_ID() && (tkt.getSTR_TICKETING_AGENCY_OFFICE_ID().startsWith("CAI") || tkt.getSTR_TICKETING_AGENCY_OFFICE_ID().startsWith("ALY")))){
							//Do Nothing
						}else if((null != tkt.getSTR_GDS() && tkt.getSTR_GDS().equalsIgnoreCase("Amadeus")) && (null != primServ && primServ.equals("GLOBAL")) && 
								(null != tkt.getSTR_TICKETING_AGENCY_OFFICE_ID() && (tkt.getSTR_TICKETING_AGENCY_OFFICE_ID().startsWith("RUH") || tkt.getSTR_TICKETING_AGENCY_OFFICE_ID().startsWith("DMM")))){
							//Do Nothing
						}else{
							if(null != suppCodeSec){
								if(suppCodeSec.contains("!")){
									String[] s = suppCodeSec.split("!");
									if(s.length > 0)
										tkt.setSTR_SUPPLIER_CODE(s[1]);
								}else{
									tkt.setSTR_SUPPLIER_CODE(suppCodeSec);
								}
							}
						}
						
						if((null != primServ && primServ.equals("GLOBAL")) && 
								(null != tkt.getSTR_TICKETING_AGENCY_OFFICE_ID() && (tkt.getSTR_TICKETING_AGENCY_OFFICE_ID().startsWith("CAI") || tkt.getSTR_TICKETING_AGENCY_OFFICE_ID().startsWith("ALY")))){
							tkt.setSTR_TYPE(ETRCSConstants.HOTEL_STR_TYPE);
						}else if((null != primServ && primServ.equals("EGYPT")) && 
								(null != tkt.getSTR_TICKETING_AGENCY_OFFICE_ID() && (tkt.getSTR_TICKETING_AGENCY_OFFICE_ID().startsWith("RUH") || tkt.getSTR_TICKETING_AGENCY_OFFICE_ID().startsWith("DMM")))){
							tkt.setSTR_TYPE(ETRCSConstants.HOTEL_STR_TYPE);
						}else if(null != tkt.getSTR_TYPE() && tkt.getSTR_TYPE().equalsIgnoreCase("LCC")){
							tkt.setSTR_TYPE(ETRCSConstants.HOTEL_STR_TYPE);
						}
					}
					
					if(null != tkt.getSTR_TICKETING_AGENCY_OFFICE_ID() && tkt.getSTR_TICKETING_AGENCY_OFFICE_ID().equalsIgnoreCase("DXBDN3179")){
						tkt.setSTR_TYPE(ETRCSConstants.HOTEL_STR_TYPE);
						tkt.setSTR_GDS("ONLINE");
						tkt.setSTR_TICKET_TYPE("ET");
						tkt.setSTR_SUPPLIER_CODE(suppllierIdsMap.get("CLEARTRIP"));
					}
				}
			}
			
			if(null != traacs.getJson_Hotel() && !traacs.getJson_Hotel().isEmpty()){
				for(HotelJsonMaster htl : traacs.getJson_Hotel()){
					if((null != secServ && null != primServ && !primServ.equalsIgnoreCase(secServ)) || (null != crossJson && crossJson.equals("true"))){
						String sc = htl.getSTR_SUPPLIER_CODE() != null ? htl.getSTR_SUPPLIER_CODE().toUpperCase() : "";
						String suppCodeSec = suppllierIdsMap.get(sc);
						if(null != suppCodeSec){
							String[] dt = suppCodeSec.split("\\!");
							if (dt.length > 0) {
								suppCodeSec = dt[1];
							}
						}
						htl.setSTR_SUPPLIER_CODE(suppCodeSec);
					}
					
					if(null != primServ && primServ.equalsIgnoreCase("EGYPT")){
						String traacsDocNo = tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getMaster().getDocNoEG();
						if(!StringUtils.isEmpty(traacsDocNo) && traacsDocNo.contains(":")){
							String str[] = traacsDocNo.split(":");
							if(str.length > 1){
								htl.setSTR_VOUCHER_NO(str[1]);
							}else{
								htl.setSTR_VOUCHER_NO(str[0]);
							}
						}else{
							htl.setSTR_VOUCHER_NO(traacsDocNo);
						}
					}else{
						String traacsDocNo = tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getMaster().getDocNo();
						if(!StringUtils.isEmpty(traacsDocNo) && traacsDocNo.contains(":")){
							String str[] = traacsDocNo.split(":");
							if(str.length > 1){
								htl.setSTR_VOUCHER_NO(str[1]);
							}else{
								htl.setSTR_VOUCHER_NO(str[0]);
							}
						}else{
							htl.setSTR_VOUCHER_NO(traacsDocNo);
						}
					}
				}
			}		
			
			if(null != traacs.getJson_Other() && !traacs.getJson_Other().isEmpty()){
				for(OtherJsonMaster oth : traacs.getJson_Other()){
					if(null != primServ && primServ.equalsIgnoreCase("EGYPT")){
						String traacsDocNo = tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getMaster().getDocNoEG();
						if(!StringUtils.isEmpty(traacsDocNo) && traacsDocNo.contains(":")){
							String str[] = traacsDocNo.split(":");
							if(str.length > 1){
								oth.setSTR_VOUCHER_NO(str[1]);
							}else{
								oth.setSTR_VOUCHER_NO(str[0]);
							}
						}else{
							oth.setSTR_VOUCHER_NO(traacsDocNo);
						}
					}else{
						String traacsDocNo = tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getMaster().getDocNo();
						if(!StringUtils.isEmpty(traacsDocNo) && traacsDocNo.contains(":")){
							String str[] = traacsDocNo.split(":");
							if(str.length > 1){
								oth.setSTR_VOUCHER_NO(str[1]);
							}else{
								oth.setSTR_VOUCHER_NO(str[0]);
							}
						}else{
							oth.setSTR_VOUCHER_NO(traacsDocNo);
						}
					}
				}
			}
		}
		
		logger.info("BuildPrimaryTraacsRequest for Booking : " + lpo + " END");
		return traacs;
		
	}
	
	public static Object deepClone(Object object){
   	 
     try {
			ObjectOutputStream oos = null;
			ObjectInputStream ois = null;
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(object);
			oos.flush();
			ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray());
			ois = new ObjectInputStream(bin);
			return ois.readObject();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
     	
     	return null;
    }

}
