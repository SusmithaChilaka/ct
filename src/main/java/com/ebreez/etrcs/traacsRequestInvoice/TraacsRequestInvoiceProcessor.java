/** * */
package com.ebreez.etrcs.traacsRequestInvoice;

/** * @author Rakesh K * */


import java.text.DateFormat;
import java.text.SimpleDateFormat;
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


import com.ebreez.etrcs.constants.ETRCSConstants;
import com.ebreez.etrcs.db.vo.TraacsDBVO;
import com.ebreez.etrcs.request.vo.TraacsInvoiceRequest;
import com.ebreez.etrcs.traacs.util.MapperUtil;


public abstract class TraacsRequestInvoiceProcessor implements ExecutableComponent {
	
	/** logger used by this class. */
	private static Log logger = LogFactory.getLog(ETRCSConstants.LOGGER_NAME);
	
	@SuppressWarnings("unchecked")
	public Object onCall(EventContext eventContext) throws Exception {
		
		logger.info("Persisting TRAACS Status to DB");

		Message message = ((Event) eventContext).getMessage();
		TraacsInvoiceRequest traacsInvRQ = (TraacsInvoiceRequest) message.getPayload().getValue();

		String modifiedBy = ((Event) message).getVariables().get(ETRCSConstants.VAR_MODIFIED_BY).getValue().toString();
		
		TraacsDBVO dbvo = new TraacsDBVO();
		String flyinCode = null;
		String reqType = null;
		String tktNo = null;
		int rsCode = 0;
		String status = null;
		String txnType = null;
		String docNo = null;
		String rsCodeStr = null;
		String rsMessage = null;
		String product = null;
		String key="";

		if(traacsInvRQ != null){
			logger.info( "TRAACS Response DOC No - " + docNo );
			
			if(traacsInvRQ.getInvoiceRequest().getDOC_NO() != null && traacsInvRQ.getInvoiceRequest().getDOC_NO().contains(":")){
				docNo = traacsInvRQ.getInvoiceRequest().getDOC_NO();
				String nwdocNo[]=docNo.split(":");
				docNo=nwdocNo[0];
			}else if(traacsInvRQ.getInvoiceRequest().getDOC_NO() != null){
				docNo = traacsInvRQ.getInvoiceRequest().getDOC_NO();
			}
 
			if(null != traacsInvRQ.getInvoiceRequest().getRESPONSE_CODE()){
				rsCodeStr = traacsInvRQ.getInvoiceRequest().getRESPONSE_CODE().toString();
			}else{
				rsCodeStr = "0";
			}
 
			if(traacsInvRQ.getInvoiceRequest().getREMARKS() != null && traacsInvRQ.getInvoiceRequest().getREMARKS().length()>1000){
				rsMessage = traacsInvRQ.getInvoiceRequest().getREMARKS().substring(0, 1000);
				logger.info("Complete error message : "+ traacsInvRQ.getInvoiceRequest().getREMARKS());
			}else{
				rsMessage = traacsInvRQ.getInvoiceRequest().getREMARKS();
			}

			if(rsCodeStr != null){
				rsCode = Integer.parseInt(rsCodeStr);
			}

			if(rsCode == 0 || rsCode == 6 ){
				Map<String, String> failureBookingListMap = (Map<String, String>)((Event) message).getVariables().get(ETRCSConstants.CON_FAILURE_CREATED_DTTM_KEY).getValue();
				String timeouthrs = ((Event) message).getVariables().get(ETRCSConstants.TIMEOUT_DIFFERENCE_IN_HOURS).getValue().toString();
				String createdDTTM = null;
				
				if(null != failureBookingListMap){
					createdDTTM = failureBookingListMap.get(key);
					Date currentDate = new Date();
	
					DateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
					Date createdDTTMDate = format.parse(createdDTTM);
		
					long time = currentDate.getTime()-createdDTTMDate.getTime();
					long diffHours = time / (60 * 60 * 1000);
	
					if(diffHours>Integer.parseInt(timeouthrs)){
						rsCode = 9;
					}
				}
			}

			status = MapperUtil.getRespStatus(rsCode);

			if(rsCode == 3 || rsCode == 4 || rsCode == 5){
				if(traacsInvRQ.getInvoiceRequest().getDOC_NO() != null && traacsInvRQ.getInvoiceRequest().getDOC_NO().contains(":")){
					docNo = traacsInvRQ.getInvoiceRequest().getDOC_NO();
					String nwdocNo[]=docNo.split(":");
					docNo=nwdocNo[0];
				}else if(traacsInvRQ.getInvoiceRequest().getDOC_NO() != null){
					docNo = traacsInvRQ.getInvoiceRequest().getDOC_NO();	
				}
			}
			
			logger.info( "TRAACS Response DOC No after spliting - " + docNo );
		} else if( traacsInvRQ == null )	{
			
			ErrorTypeRepository execPayload =(ErrorTypeRepository) message.getPayload().getValue();
			if( execPayload == null )	{
				//rsMessage = ((Map<String,TypedValue<?>>) message).get( ETRCSConstants.TRAACS_EMPTY_RESPONSE).getValue().toString();
				
				rsMessage = ((Map<String,TypedValue<?>>) message).get( ETRCSConstants.TRAACS_EMPTY_RESPONSE).getValue().toString();
				logger.info( "Empty/Incorrect response from Traacs " );
			}else if( execPayload != null )	{
				//Exception err = (Exception) execPayload.getException();
				
				Exception err = (Exception) execPayload.getAnyErrorType();
				
				if( err instanceof NullPointerException ||
						err instanceof java.net.ConnectException ||
						err.getCause() instanceof NullPointerException ||
						err.getCause() instanceof java.net.ConnectException	)	{
					
					logger.info( "Error occured while connecting to Traacs... " + err );				
					//rsMessage = message.getProperty( ETRCSConstants.TRAACS_CONNECTION_ERR_MSG, PropertyScope.INVOCATION );
					
					rsMessage = ((Map<String,TypedValue<?>>) message).get( ETRCSConstants.TRAACS_CONNECTION_ERR_MSG ).getValue().toString();
				} else {
					logger.info( "Problem in connectig to Traacs... " + err );				
					//rsMessage = message.getProperty( ETRCSConstants.TRAACS_OTHER_ERR_MSG, PropertyScope.INVOCATION );
					
					rsMessage = ((Map<String,TypedValue<?>>) message).get( ETRCSConstants.TRAACS_CONNECTION_ERR_MSG ).getValue().toString();
				}
			}
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
		
		if( null != traacsInvRQ.getInvoiceRequest() && null != traacsInvRQ.getInvoiceRequest().getTYPE_OF_SERVICE() && 
				traacsInvRQ.getInvoiceRequest().getTYPE_OF_SERVICE().equals( ETRCSConstants.VAR_REFUND_LABEL ) ) {
			dbvo.setTypeOfService( ETRCSConstants.VAR_REFUND_LABEL );
	
			if ( null != status && status.equalsIgnoreCase( "SOLD" ) ) {
				dbvo.setStatus( "REFUNDED" );
			} else {
				dbvo.setStatus( status );
			}
		} else {
			dbvo.setTypeOfService( ETRCSConstants.VAR_SALES_LABEL );
			dbvo.setStatus( status );
		}
	 
		return dbvo;
	}

}
