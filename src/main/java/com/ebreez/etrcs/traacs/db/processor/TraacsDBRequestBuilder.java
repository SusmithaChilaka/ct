package com.ebreez.etrcs.traacs.db.processor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.runtime.api.exception.ErrorTypeRepository;
import org.mule.runtime.api.event.Event;
import org.mule.runtime.api.event.EventContext;
import org.mule.runtime.api.message.Message;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.api.component.execution.ExecutableComponent;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.ebreez.etrcs.constants.ETRCSConstants;
import com.ebreez.etrcs.db.vo.TraacsDBVO;
import com.ebreez.etrcs.request.vo.TraacsInfoRequest;
import com.ebreez.etrcs.traacs.request.vo.MasterJson;
import com.ebreez.etrcs.traacs.response.vo.TraacsResponse;
import com.ebreez.etrcs.traacs.util.MapperUtil;
import com.ebreez.etrcs.traacs.vo.OtherJsonMaster;
import com.ebreez.etrcs.traacs.vo.TicketJsonMaster;

public abstract class TraacsDBRequestBuilder implements ExecutableComponent {
	
	/** logger used by this class. */
    private static Log logger = LogFactory.getLog(ETRCSConstants.LOGGER_NAME);
   
	
	@SuppressWarnings("unchecked")
	public Object onCall(EventContext eventContext) throws Exception {
		
		Message message = ((Event)eventContext).getMessage();

		TraacsInfoRequest tcsReq = (TraacsInfoRequest) ((Event) message).getVariables().get(ETRCSConstants.VAR_UPDATE_REQUEST).getValue();
		TraacsResponse response = (TraacsResponse) ((Event) message).getVariables().get(ETRCSConstants.VAR_JSON_RESPONSE).getValue();

		String[] ticketNos = null;
		
		Object jsonRQ = ((Event) message).getVariables().get(ETRCSConstants.VAR_JSON_REQUEST);
		String modifiedBy = ((Event) message).getVariables().get(ETRCSConstants.VAR_MODIFIED_BY).getValue().toString();

		
		TraacsDBVO dbvo = new TraacsDBVO();
		String flyinCode = null;
		String reqType = null;
		String tktNo = null;
		int rsCode = 0;
		String status = "PENDING";
		String txnType = null;
		String docNo = null;
		String rsCodeStr = null;
		String rsMessage = null;
		String product = null;
		String key="";
		String domain = null;
		
		if(jsonRQ instanceof MasterJson){			
			
			if ((null != ((MasterJson) jsonRQ).getJson_Other() && !((MasterJson) jsonRQ).getJson_Other().isEmpty()) &&
					(null != ((MasterJson) jsonRQ).getJson_Other().get(0).getSTR_SERVICE() && ((MasterJson) jsonRQ).getJson_Other().get(0).getSTR_SERVICE().equalsIgnoreCase("PACKAGE"))){
				MasterJson reqJson = (MasterJson) jsonRQ;

				if(null != ((MasterJson) jsonRQ).getJson_Ticket()){
					for(TicketJsonMaster tkts : ((MasterJson) jsonRQ).getJson_Ticket()){
						if(null != tktNo){
							if(null != tkts.getSTR_TICKET_NO())
								tktNo = tktNo.trim().concat("_" + tkts.getSTR_TICKET_NO().trim());
						}else{
							if(null != tkts.getSTR_TICKET_NO())
								tktNo = tkts.getSTR_TICKET_NO().trim();
						}
					}
					
					if(null != tktNo && tktNo.contains("_")){
						ticketNos = tktNo.split("_");
						Arrays.sort(ticketNos);
						tktNo = null;
						for (int i = 0; i < ticketNos.length; i++){
							if(tktNo == null){
								tktNo = ticketNos[i].trim();
							}else{
								tktNo = tktNo.trim().concat("_" + ticketNos[i].trim());
							}
						}
					}
				}
				
				if(null != ((MasterJson) jsonRQ).getJson_Hotel()){
					if(null != reqJson.getJson_Hotel().get(0).getSTR_SUPPLIER_CONF_NUMBER()){
						if(null != tktNo)
							tktNo = tktNo.trim().concat("_" + reqJson.getJson_Hotel().get(0).getSTR_SUPPLIER_CONF_NUMBER().trim());
						else
							tktNo = (reqJson.getJson_Hotel().get(0).getSTR_SUPPLIER_CONF_NUMBER().trim());
					}
				}
				
				if(null != ((MasterJson) jsonRQ).getJson_Other()){
					if(null != reqJson.getJson_Other().get(0).getSTR_SUPPLIER_CONF_NUMBER()){
						if(null != tktNo)
							tktNo = tktNo.trim().concat("_" + reqJson.getJson_Other().get(0).getSTR_SUPPLIER_CONF_NUMBER().trim());
						else
							tktNo = (reqJson.getJson_Other().get(0).getSTR_SUPPLIER_CONF_NUMBER().trim());
					}
				}
				
				flyinCode = reqJson.getJson_Other().get(0).getSTR_LPO_NO();
				
				if(null!=reqJson.getJson_Other().get(0).getSTR_REFUND_STATUS()){
					key="R-"+flyinCode;
				}else{
					key= "S-"+flyinCode;
				}
				
				product = ((MasterJson) jsonRQ).getJson_Other().get(0).getSTR_SERVICE();
				reqType = "NEW OR UPDATE";
				txnType = "ISSUE";
			}else if ((null != ((MasterJson) jsonRQ).getJson_Ticket() && !((MasterJson) jsonRQ).getJson_Ticket().isEmpty()) &&
					(null != ((MasterJson) jsonRQ).getJson_Hotel() && !((MasterJson) jsonRQ).getJson_Hotel().isEmpty())){
				MasterJson reqJson = (MasterJson) jsonRQ;

				for(TicketJsonMaster tkts : ((MasterJson) jsonRQ).getJson_Ticket()){
					if(null != tktNo){
						if(null != tkts.getSTR_TICKET_NO())
							tktNo = tktNo.trim().concat("_" + tkts.getSTR_TICKET_NO().trim());
					}else{
						if(null != tkts.getSTR_TICKET_NO())
							tktNo = tkts.getSTR_TICKET_NO().trim();
					}
				}
				
				if(null != tktNo && tktNo.contains("_")){
					ticketNos = tktNo.split("_");
					Arrays.sort(ticketNos);
					tktNo = null;
					for (int i = 0; i < ticketNos.length; i++){
						if(tktNo == null){
							tktNo = ticketNos[i].trim();
						}else{
							tktNo = tktNo.trim().concat("_" + ticketNos[i].trim());
						}
					}
				}
				
				if(null != reqJson.getJson_Hotel().get(0).getSTR_SUPPLIER_CONF_NUMBER()){
					tktNo = tktNo.trim().concat("_" + reqJson.getJson_Hotel().get(0).getSTR_SUPPLIER_CONF_NUMBER().trim());
				}
				
				flyinCode = reqJson.getJson_Ticket().get(0).getSTR_LPO_NO();
				
				if(null!=reqJson.getJson_Ticket().get(0).getSTR_REFUND_STATUS()){
					key="R-"+flyinCode;
				}else{
					key= "S-"+flyinCode;
				}
				
				product = reqJson.getJson_Ticket().get(0).getSTR_PRODUCT();
				reqType = "NEW OR UPDATE";
				txnType = "ISSUE";
			} else if (null != ((MasterJson) jsonRQ).getJson_Ticket() && !((MasterJson) jsonRQ).getJson_Ticket().isEmpty()) {
				MasterJson reqJson = (MasterJson) jsonRQ;

				for(TicketJsonMaster tkts : ((MasterJson) jsonRQ).getJson_Ticket()){
					if(null != tktNo){
						if(null != tkts.getSTR_TICKET_NO())
							tktNo = tktNo.trim().concat("_" + tkts.getSTR_TICKET_NO().trim());
					}else{
						if(null != tkts.getSTR_TICKET_NO())
							tktNo = tkts.getSTR_TICKET_NO().trim();
					}
				}
				
				if(null != tktNo && tktNo.contains("_")){
					ticketNos = tktNo.split("_");
					Arrays.sort(ticketNos);
					tktNo = null;
					for (int i = 0; i < ticketNos.length; i++){
						if(tktNo == null){
							tktNo = ticketNos[i].trim();
						}else{
							tktNo = tktNo.trim().concat("_" + ticketNos[i].trim());
						}
					}
				}
				
				flyinCode = reqJson.getJson_Ticket().get(0).getSTR_LPO_NO();
				
				if(null!=reqJson.getJson_Ticket().get(0).getSTR_REFUND_STATUS()){
					key="R-"+flyinCode;
				}else{
					key= "S-"+flyinCode;
				}
				
				product = reqJson.getJson_Ticket().get(0).getSTR_PRODUCT();
				reqType = "NEW OR UPDATE";
				txnType = "ISSUE";
			} else if (null != ((MasterJson) jsonRQ).getJson_Hotel() && !((MasterJson) jsonRQ).getJson_Hotel().isEmpty()) {
				MasterJson reqJson = (MasterJson) jsonRQ;
				flyinCode = reqJson.getJson_Hotel().get(0).getSTR_LPO_NO();
				if(null!=reqJson.getJson_Hotel().get(0).getSTR_REFUND_STATUS()){
					key="R-"+flyinCode;
				}else{
					key= "S-"+flyinCode;
				}
				
				if(null != reqJson.getJson_Hotel().get(0).getSTR_SUPPLIER_CONF_NUMBER()){
					tktNo = reqJson.getJson_Hotel().get(0).getSTR_SUPPLIER_CONF_NUMBER().trim();
				}
				
				product = reqJson.getJson_Hotel().get(0).getSTR_PRODUCT();
				reqType = "NEW OR UPDATE";
				txnType = "ISSUE";
			} else if (null != ((MasterJson) jsonRQ).getJson_Car() && !((MasterJson) jsonRQ).getJson_Car().isEmpty()) {
				MasterJson reqJson = (MasterJson) jsonRQ;
				flyinCode = reqJson.getJson_Car().get(0).getSTR_LPO_NO();
				if(null!=reqJson.getJson_Car().get(0).getSTR_REFUND_STATUS()){
					key="R-"+flyinCode;
				}else{
					key= "S-"+flyinCode;
				}
				
				if(null != reqJson.getJson_Car().get(0).getSTR_SUPPLIER_CONF_NUMBER().trim()){
					tktNo = reqJson.getJson_Car().get(0).getSTR_SUPPLIER_CONF_NUMBER().trim();
				}
				
				product = reqJson.getJson_Car().get(0).getSTR_PRODUCT();
				reqType = "NEW OR UPDATE";
				txnType = "ISSUE";
			} else if (null != ((MasterJson) jsonRQ).getJson_Other() && !((MasterJson) jsonRQ).getJson_Other().isEmpty()) {
				MasterJson reqJson = (MasterJson) jsonRQ;
				flyinCode = reqJson.getJson_Other().get(0).getSTR_LPO_NO();
				if(null!=((MasterJson) jsonRQ).getJson_master().getSTR_REFUND_DATE()){
					key="R-"+flyinCode;
				}else{
					key= "S-"+flyinCode;
				}

				for(OtherJsonMaster addOnConfNo : ((MasterJson) jsonRQ).getJson_Other()){
					if(null != tktNo){
						if(null != addOnConfNo.getSTR_SUPPLIER_CONF_NUMBER())
							tktNo = tktNo.trim().concat("_" + addOnConfNo.getSTR_SUPPLIER_CONF_NUMBER().trim());
					}else{
						if(null != addOnConfNo.getSTR_SUPPLIER_CONF_NUMBER())
							tktNo = addOnConfNo.getSTR_SUPPLIER_CONF_NUMBER().trim();
					}
				}
				
				if(null != tktNo && tktNo.contains("_")){
					ticketNos = tktNo.split("_");
					Arrays.sort(ticketNos);
					tktNo = null;
					for (int i = 0; i < ticketNos.length; i++){
						if(tktNo == null){
							tktNo = ticketNos[i].trim();
						}else{
							tktNo = tktNo.trim().concat("_" + ticketNos[i].trim());
						}
					}
				}
				
				product = "ADD-ON";
				reqType = "NEW OR UPDATE";
				txnType = "ISSUE";
			}
		}
		
		if( response == null )	{
			// Get exception type from Traacs response
			//ExceptionPayload execPayload = message.getExceptionPayload();
			ErrorTypeRepository execPayload =(ErrorTypeRepository) message.getPayload().getValue();
			if( execPayload == null )	{
				//rsMessage = message.getProperty( ETRCSConstants.TRAACS_EMPTY_RESPONSE, PropertyScope.INVOCATION );
				
				rsMessage = ((Map<String,TypedValue<?>>) message).get( ETRCSConstants.TRAACS_EMPTY_RESPONSE).getValue().toString();
				logger.info( "Empty/Incorrect response from Traacs " );
			}
			else if( execPayload != null )	{
				//Exception err = (Exception) execPayload.getException();	
				Exception err = (Exception) execPayload.getAnyErrorType();
				
				if( err instanceof NullPointerException ||
						err instanceof java.net.ConnectException ||
						err.getCause() instanceof NullPointerException ||
						err.getCause() instanceof java.net.ConnectException	)	{
					logger.info( "Error occured while connecting to Traacs... " + err );				
					//rsMessage = message.getProperty( ETRCSConstants.TRAACS_CONNECTION_ERR_MSG, PropertyScope.INVOCATION );
					
					rsMessage = ((Map<String,TypedValue<?>>) message).get( ETRCSConstants.TRAACS_CONNECTION_ERR_MSG).getValue().toString();
				}
				else {
					logger.info( "Problem in connectig to Traacs... " + err );				
					//rsMessage = message.getProperty( ETRCSConstants.TRAACS_OTHER_ERR_MSG, PropertyScope.INVOCATION );
					rsMessage = ((Map<String,TypedValue<?>>) message).get( ETRCSConstants.TRAACS_OTHER_ERR_MSG).getValue().toString();
				}			
			}
		}
		
		else if(response != null){
			 if(response.getInvoiceNo() != null && response.getInvoiceNo().contains(":")){
				 docNo = response.getInvoiceNo();
				 logger.info( "TRAACS Response DOC No - " + docNo );
								 
				 if(null != ((MasterJson) jsonRQ).getJson_Hotel() && !((MasterJson) jsonRQ).getJson_Hotel().isEmpty()){
				 	//docNo=docNo;
				 }else{
					String nwdocNo[]=docNo.split(":");
				 	docNo=nwdocNo[0];
				 }
				 
			 }
			 else if(!StringUtils.isEmpty(response.getInvoiceNo())){
				 docNo = response.getInvoiceNo();
			 }
			 rsCodeStr = response.getResponseCode();
			 if(response.getResponseMessage() != null && response.getResponseMessage().length()>1000){
				 rsMessage = response.getResponseMessage().substring(0, 1000);
				 logger.info("Complete error message : "+ response.getResponseMessage());
			 }else{
				 rsMessage = response.getResponseMessage();
			 }
		 	if(rsCodeStr != null){
				rsCode = Integer.parseInt(rsCodeStr);
			}
			
		 	if(rsCode ==0 || rsCode == 6 || rsCode == 7 ){
			 	Map<String, String> failureBookingListMap = ((Map<String, String>) ((Event) message).getVariables().get(ETRCSConstants.CON_FAILURE_CREATED_DTTM_KEY).getValue());
			 	String timeouthrs = (String)((Event) message).getVariables().get(ETRCSConstants.TIMEOUT_DIFFERENCE_IN_HOURS).getValue();
				String createdDTTM = null;
				if(null != failureBookingListMap){
					createdDTTM = failureBookingListMap.get(key);
					Date currentDate = new Date();
					
					DateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
					Date createdDTTMDate = format.parse(createdDTTM);
					
					long time = currentDate.getTime()-createdDTTMDate.getTime();
					long diffHours = time / (60 * 60 * 1000); //For Hours Difference (72)
					//long diffHours = ((time / 1000) / 60) % 60; //For Minutes Difference (35)
				
					if(diffHours>Integer.parseInt(timeouthrs)){
						rsCode = 9;
					}
				}
		 	}
		 	 //X=get created date & time from TraacsFlyindetails table
		 	//Y=get server date & time
		 	//if Y-X > 72 then change the status to time out
			
		 	status = MapperUtil.getRespStatus(rsCode);
			
			if(rsCode == 3 || rsCode == 4 || rsCode == 5){
				if(response.getRefNo() != null && response.getRefNo().contains(":")){
					docNo = response.getRefNo();
					String nwdocNo[]=docNo.split(":");
		            docNo=nwdocNo[0];
				}
				else if(response.getRefNo() != null){
					docNo = response.getRefNo();	
				}
			}
			logger.info( "TRAACS Response DOC No after spliting - " + docNo );
		}
		
		 dbvo.setFlyinCode( flyinCode );
		 dbvo.setDocNo( docNo );
		 dbvo.setRsCode( rsCode );
		 dbvo.setRsMessage( rsMessage );
		 dbvo.setTxnType( txnType );
		 dbvo.setTktType( reqType );
		 dbvo.setTktOrPnr( tktNo );
		 dbvo.setModifiedBy( modifiedBy );
		 dbvo.setProduct( product );
		 
		 String paxId = ((Event) message).getVariables().get(ETRCSConstants.REFUND_PAX_ID).getValue().toString();
		 if(null != paxId && paxId.contains(",")){
			 String[] paxIds = paxId.split(",");
			Arrays.sort(paxIds);
			paxId = null;
			for (int i = 0; i < paxIds.length; i++){
				if(paxId == null){
					paxId = paxIds[i].trim();
				}else{
					paxId = paxId.trim().concat("," + paxIds[i].trim());
				}
			}
		 }
		 dbvo.setPaxId(paxId);
		 
		 String segId = ((Event) message).getVariables().get(ETRCSConstants.REFUND_SEGMENT_ID).getValue().toString();
		 if(null != segId && segId.contains(",")){
			 String[] segIds = segId.split(",");
			Arrays.sort(segIds);
			segId = null;
			for (int i = 0; i < segIds.length; i++){
				if(segId == null){
					segId = segIds[i].trim();
				}else{
					segId = segId.trim().concat("," + segIds[i].trim());
				}
			}
		 }
		 dbvo.setSegmentId(segId);
		 
		// message.removeProperty(ETRCSConstants.REFUND_PAX_ID,PropertyScope.INVOCATION);
		 
		 ((Map<String,TypedValue<?>>) message).remove(ETRCSConstants.REFUND_PAX_ID);
		 
		// message.removeProperty(ETRCSConstants.REFUND_SEGMENT_ID,PropertyScope.INVOCATION);
		 
		 ((Map<String,TypedValue<?>>) message).remove(ETRCSConstants.REFUND_SEGMENT_ID);
		 
		 
		 if(jsonRQ instanceof MasterJson){
			if(null != ((MasterJson) jsonRQ).getJson_master()){
				MasterJson reqJson = (MasterJson) jsonRQ;
				if( null != reqJson.getJson_master().getSTR_REFUND_DATE() ) {
					if(null != product && product.equalsIgnoreCase("ADD-ON")){
						dbvo.setTypeOfService( ETRCSConstants.VAR_ADD_ON_REFUND_LABEL );
					}else{
						dbvo.setTypeOfService( ETRCSConstants.VAR_REFUND_LABEL );
					}
					
					if ( null != status && status.equalsIgnoreCase( "SOLD" ) ) {
						dbvo.setStatus( "REFUNDED" );
					} else {
						dbvo.setStatus( status );
					}
				} else {
					dbvo.setTypeOfService( ETRCSConstants.VAR_SALES_LABEL );
					dbvo.setStatus( status );
				}
			}
		 }
		 
		 if(null !=  ((Event) message).getVariables().get(ETRCSConstants.TRAACS_DOMAIN) ){
			 dbvo.setDomain( ((Event) message).getVariables().get(ETRCSConstants.TRAACS_DOMAIN).getValue().toString() ); 
		 }else{
			 Map<String, String> bookingDomainTypeMap = ((Map<String, String>) ((Event) message).getVariables().get(ETRCSConstants.CON_FAILURE_DOMAIN_TYPE).getValue());
			 if(!CollectionUtils.isEmpty(bookingDomainTypeMap)){
				 if(null != tcsReq &&
						null != tcsReq.getTraacInfoRQ() &&
						null != tcsReq.getTraacInfoRQ().getTraacDetails() && !tcsReq.getTraacInfoRQ().getTraacDetails().isEmpty()){
					
					 if(null != tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getMaster()){
						 domain = bookingDomainTypeMap.get(tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getMaster().getJsonfrom());
						 dbvo.setDomain(domain);
					 }
				 }
			 }
		 }
		 
		 if(null != tcsReq && null != tcsReq.getTraacInfoRQ() &&
					null != tcsReq.getTraacInfoRQ().getTraacDetails() && !tcsReq.getTraacInfoRQ().getTraacDetails().isEmpty() &&
					null != tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getMaster()){
				
			 if(null != tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getMaster().getEditAction() && 
					 tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getMaster().getEditAction().equals("EDITED")){
				 dbvo.setEdited("EDITED");
			 }else{
				 dbvo.setEdited("NOT_EDITED");				 
			 }
		 }
		 
		 return dbvo;
	}

}
