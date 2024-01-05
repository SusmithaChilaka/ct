package com.ebreez.etrcs.traacs.processor;

import java.util.List;
import java.util.Map;

import org.mule.runtime.api.component.execution.ExecutableComponent;
import org.mule.runtime.api.event.Event;
import org.mule.runtime.api.event.EventContext;
import org.mule.runtime.api.message.Message;

import com.ebreez.etrcs.constants.ETRCSConstants;
import com.ebreez.etrcs.request.vo.TraacsInfo;
import com.ebreez.etrcs.request.vo.TraacsInfoRequest;
import com.ebreez.etrcs.traacs.mappers.TraacsBaseMapper;
import com.ebreez.etrcs.traacs.request.vo.MasterJson;
import com.ebreez.etrcs.traacs.vo.Authentication;


/**
 * The purpose of this class is to
 * 1) initialize the authentication object for TRAACS JSON
 * 2) initilize the flight and hotel mappers 
 * 3) invoke the mappers to do the mapping of input JSON to Traacs JSON
 * 4) Set the resulted Flight and hotel JSON list objects to flow Variables
 * @author Veeramalleswarudu Seelam
 *
 */
public abstract class BuildTraacsRequestProcessor implements ExecutableComponent {


	@SuppressWarnings("unchecked")
	public Object onCall(EventContext eventContext) throws Exception {
		
		Message message = ((Event) eventContext).getMessage();
		Message.Builder messageBuilder = Message.builder(message); 

		Map<String, String> properties = (Map<String, String>) ((Event) eventContext).getVariables().get(ETRCSConstants.BEAN_ETRCS_APP_PROPERTIES).getValue();
	
		Authentication auth = new Authentication();
		auth.setSTR_USER_NAME(properties.get(ETRCSConstants.TRAACS_USERNAME_GLOBAL));
		auth.setSTR_PASSWORD(properties.get(ETRCSConstants.TRAACS_PASSWORD_GLOBAL));
			
		TraacsInfoRequest tcsReq = (TraacsInfoRequest)((Event) message).getVariables().get(ETRCSConstants.VAR_UPDATE_REQUEST).getValue();
		List<TraacsInfo> traacInfoList = tcsReq.getTraacInfoRQ().getTraacDetails();
		
		TraacsBaseMapper activityMapper = null;
		MasterJson activity = null;
		
		TraacsBaseMapper packagelMapper = null;
		MasterJson packages = null;
		
		TraacsBaseMapper fltPlusHtlMapper = null;
		MasterJson fltPlusHtl = null;
		
		TraacsBaseMapper flightsMapper = null;
		MasterJson tickets = null;
		
		TraacsBaseMapper hotelsMapper = null;
		MasterJson hotels = null;
		
		TraacsBaseMapper carTransferMapper = null;
		MasterJson carTransfer = null;
		
		TraacsBaseMapper otherMapper = null;
		MasterJson other = null;
		
		
		if(!traacInfoList.isEmpty() && null != traacInfoList.get(0).getOthers() && 
				 (!traacInfoList.get(0).getOthers().isEmpty() && traacInfoList.get(0).getOthers().get(0).getProduct().equalsIgnoreCase("ACTIVITY"))){
			activityMapper = loadTraacsJSONRequestMapper( eventContext );
			activity = (MasterJson) activityMapper.map( auth, properties, message, "activity" );
		}else if(!traacInfoList.isEmpty() && null != traacInfoList.get(0).getOthers() && 
				 (!traacInfoList.get(0).getOthers().isEmpty() && traacInfoList.get(0).getOthers().get(0).getProduct().equalsIgnoreCase("PACKAGE"))){
			packagelMapper = loadTraacsJSONRequestMapper( eventContext );
			packages = (MasterJson) packagelMapper.map( auth, properties, message, "package" );
		}else if((!traacInfoList.isEmpty() && null != traacInfoList.get(0).getFlights() && !traacInfoList.get(0).getFlights().isEmpty()) && 
				(!traacInfoList.isEmpty() && null != traacInfoList.get(0).getHotels() && !traacInfoList.get(0).getHotels().isEmpty())){
			fltPlusHtlMapper = loadTraacsJSONRequestMapper( eventContext );
			fltPlusHtl = (MasterJson) fltPlusHtlMapper.map( auth, properties, message, "fltHtl" );
		}else if(!traacInfoList.isEmpty() && null != traacInfoList.get(0).getFlights() && !traacInfoList.get(0).getFlights().isEmpty()){
			flightsMapper = loadTraacsJSONRequestMapper( eventContext );
			tickets = (MasterJson) flightsMapper.map( auth, properties, message, "flight" );
		}else if(!traacInfoList.isEmpty() && null != traacInfoList.get(0).getHotels() && !traacInfoList.get(0).getHotels().isEmpty()){
			hotelsMapper = loadTraacsJSONRequestMapper( eventContext );
			hotels = (MasterJson) hotelsMapper.map( auth, properties, message, "hotel" );
		}else if(!traacInfoList.isEmpty() && null != traacInfoList.get(0).getCars() && !traacInfoList.get(0).getCars().isEmpty()){
			carTransferMapper = loadTraacsJSONRequestMapper( eventContext );
			carTransfer = (MasterJson) carTransferMapper.map( auth, properties, message, "car" );
		}else if(!traacInfoList.isEmpty() && null != traacInfoList.get(0).getOthers() && !traacInfoList.get(0).getOthers().isEmpty()){
			otherMapper = loadTraacsJSONRequestMapper( eventContext );
			other = (MasterJson) otherMapper.map( auth, properties, message, "other" );
		}	
		
		//message.setInvocationProperty(ETRCSConstants.VAR_TICKET_REQUEST, tickets );
		//message.setInvocationProperty(ETRCSConstants.VAR_HOTEL_REQUEST, hotels );
		//message.setInvocationProperty(ETRCSConstants.VAR_CAR_REQUEST, carTransfer );
		//message.setInvocationProperty(ETRCSConstants.VAR_FLIGHT_HOTEL_REQUEST, fltPlusHtl);
		//message.setInvocationProperty(ETRCSConstants.VAR_PACKAGE_REQUEST, packages);
		//message.setInvocationProperty(ETRCSConstants.VAR_ACTIVITY_REQUEST, activity);
		//message.setInvocationProperty(ETRCSConstants.VAR_OTHER_REQUEST, other);
		//here iam changed ((Map<String, String>)to ((Map<String, MasterJson>)
		
		 ((Map<String, MasterJson>) messageBuilder).put(ETRCSConstants.VAR_TICKET_REQUEST, tickets );
		 ((Map<String, MasterJson>) messageBuilder).put(ETRCSConstants.VAR_HOTEL_REQUEST, hotels );
		 ((Map<String, MasterJson>) messageBuilder).put(ETRCSConstants.VAR_CAR_REQUEST, carTransfer );
		 ((Map<String, MasterJson>) messageBuilder).put(ETRCSConstants.VAR_FLIGHT_HOTEL_REQUEST, fltPlusHtl);
		 ((Map<String, MasterJson>) messageBuilder).put(ETRCSConstants.VAR_PACKAGE_REQUEST, packages);
		 ((Map<String, MasterJson>) messageBuilder).put(ETRCSConstants.VAR_ACTIVITY_REQUEST, activity);
		 ((Map<String, MasterJson>) messageBuilder).put(ETRCSConstants.VAR_OTHER_REQUEST, other);
		

		
		
		if(null != activity && null != activity.getJson_master() && null != activity.getJson_master().getSTR_REFUND_DATE()){
			//message.setInvocationProperty(ETRCSConstants.VAR_FLOW_TYPE, "REFUND" );
			 ((Map<String, String>) messageBuilder).put(ETRCSConstants.VAR_FLOW_TYPE, "REFUND" );
		}else if(null != packages && null != packages.getJson_master() && null != packages.getJson_master().getSTR_REFUND_DATE()){
			//message.setInvocationProperty(ETRCSConstants.VAR_FLOW_TYPE, "REFUND" );
			 ((Map<String, String>) messageBuilder).put(ETRCSConstants.VAR_FLOW_TYPE, "REFUND" );
		}else if(null != fltPlusHtl && null != fltPlusHtl.getJson_master() && null != fltPlusHtl.getJson_master().getSTR_REFUND_DATE()){
			//message.setInvocationProperty(ETRCSConstants.VAR_FLOW_TYPE, "REFUND" );
			 ((Map<String, String>) messageBuilder).put(ETRCSConstants.VAR_FLOW_TYPE, "REFUND" );
		}else if(null != tickets && null != tickets.getJson_master() && null != tickets.getJson_master().getSTR_REFUND_DATE()){
			//message.setInvocationProperty(ETRCSConstants.VAR_FLOW_TYPE, "REFUND" );
			 ((Map<String, String>) messageBuilder).put(ETRCSConstants.VAR_FLOW_TYPE, "REFUND" );
		}else if(null != hotels && null != hotels.getJson_master() && null != hotels.getJson_master().getSTR_REFUND_DATE()){
			//message.setInvocationProperty(ETRCSConstants.VAR_FLOW_TYPE, "REFUND" );
			 ((Map<String, String>) messageBuilder).put(ETRCSConstants.VAR_FLOW_TYPE, "REFUND" );
		}else if(null != carTransfer && null != carTransfer.getJson_master() && null != carTransfer.getJson_master().getSTR_REFUND_DATE()){
			//message.setInvocationProperty(ETRCSConstants.VAR_FLOW_TYPE, "REFUND" );
			 ((Map<String, String>) messageBuilder).put(ETRCSConstants.VAR_FLOW_TYPE, "REFUND" );
		}else if((null != other && null != other.getJson_master() && null != other.getJson_master().getSTR_REFUND_DATE()) && 
				(null == fltPlusHtl && null == tickets && null == hotels && null == carTransfer)){
			//message.setInvocationProperty(ETRCSConstants.VAR_FLOW_TYPE, "REFUND" );
			 ((Map<String, String>) messageBuilder).put(ETRCSConstants.VAR_FLOW_TYPE, "REFUND" );
		}else{
			//message.setInvocationProperty(ETRCSConstants.VAR_FLOW_TYPE, "SALES" );
			 ((Map<String, String>) messageBuilder).put(ETRCSConstants.VAR_FLOW_TYPE, "SALES" );
		}
		
		if(null != activity){
			//message.setInvocationProperty(ETRCSConstants.VAR_PRODUCT, "ACTIVITY" );
			//message.setInvocationProperty(ETRCSConstants.CON_FLYIN_CODE_KEY, activity.getJson_Other().get(0).getSTR_LPO_NO() );
			
			 ((Map<String, String>) messageBuilder).put(ETRCSConstants.VAR_PRODUCT, "ACTIVITY" );
			 ((Map<String, String>) messageBuilder).put(ETRCSConstants.CON_FLYIN_CODE_KEY, activity.getJson_Other().get(0).getSTR_LPO_NO() );
		}else if(null != packages){
			//message.setInvocationProperty(ETRCSConstants.VAR_PRODUCT, "PACKAGES" );
			//message.setInvocationProperty(ETRCSConstants.CON_FLYIN_CODE_KEY, packages.getJson_Other().get(0).getSTR_LPO_NO() );
			
			 ((Map<String, String>) messageBuilder).put(ETRCSConstants.VAR_PRODUCT, "PACKAGES" );
			 ((Map<String, String>) messageBuilder).put(ETRCSConstants.CON_FLYIN_CODE_KEY, packages.getJson_Other().get(0).getSTR_LPO_NO() );
		}else if((null != fltPlusHtl && null != fltPlusHtl.getJson_Ticket() && !fltPlusHtl.getJson_Ticket().isEmpty()) && 
				(null != fltPlusHtl && null != fltPlusHtl.getJson_Hotel() && !fltPlusHtl.getJson_Hotel().isEmpty())){
			//message.setInvocationProperty(ETRCSConstants.VAR_PRODUCT, "FLT+HTL" );
			//message.setInvocationProperty(ETRCSConstants.CON_FLYIN_CODE_KEY, fltPlusHtl.getJson_Ticket().get(0).getSTR_LPO_NO() );
			
			
			 ((Map<String, String>) messageBuilder).put(ETRCSConstants.VAR_PRODUCT, "FLT+HTL" );
			 ((Map<String, String>) messageBuilder).put(ETRCSConstants.CON_FLYIN_CODE_KEY, fltPlusHtl.getJson_Ticket().get(0).getSTR_LPO_NO() );
		}else if(null != tickets && null != tickets.getJson_Ticket() && !tickets.getJson_Ticket().isEmpty()){
			//message.setInvocationProperty(ETRCSConstants.VAR_PRODUCT, "FLIGHT" );
			//message.setInvocationProperty(ETRCSConstants.CON_FLYIN_CODE_KEY, tickets.getJson_Ticket().get(0).getSTR_LPO_NO() );
			
			 ((Map<String, String>) messageBuilder).put(ETRCSConstants.VAR_PRODUCT, "FLIGHT" );
			 ((Map<String, String>) messageBuilder).put(ETRCSConstants.CON_FLYIN_CODE_KEY, tickets.getJson_Ticket().get(0).getSTR_LPO_NO() );
		}else if(null != hotels && null != hotels.getJson_Hotel() && !hotels.getJson_Hotel().isEmpty()){
			//message.setInvocationProperty(ETRCSConstants.VAR_PRODUCT, "HOTEL" );
			//message.setInvocationProperty(ETRCSConstants.CON_FLYIN_CODE_KEY, hotels.getJson_Hotel().get(0).getSTR_LPO_NO() );
			
			
			 ((Map<String, String>) messageBuilder).put(ETRCSConstants.VAR_PRODUCT, "HOTEL" );
			 ((Map<String, String>) messageBuilder).put(ETRCSConstants.CON_FLYIN_CODE_KEY, hotels.getJson_Hotel().get(0).getSTR_LPO_NO() );
		}else if(null != carTransfer && null != carTransfer.getJson_Car() && !carTransfer.getJson_Car().isEmpty()){
			if(null != carTransfer.getJson_Car().get(0).getSTR_PRODUCT() && 
					(carTransfer.getJson_Car().get(0).getSTR_PRODUCT().equalsIgnoreCase("Transfers") ||
					carTransfer.getJson_Car().get(0).getSTR_PRODUCT().equalsIgnoreCase(ETRCSConstants.CON_TRANSFER_PRODUCT))){
				//message.setInvocationProperty(ETRCSConstants.VAR_PRODUCT, "CAR" );
				//message.setInvocationProperty(ETRCSConstants.VAR_CAR_TRANSFER, "TRANSFER" );
				//message.setInvocationProperty(ETRCSConstants.CON_FLYIN_CODE_KEY, carTransfer.getJson_Car().get(0).getSTR_LPO_NO() );
				
				 ((Map<String, String>) messageBuilder).put(ETRCSConstants.VAR_PRODUCT, "CAR" );
				 ((Map<String, String>) messageBuilder).put(ETRCSConstants.VAR_CAR_TRANSFER, "TRANSFER" );
				 ((Map<String, String>) messageBuilder).put(ETRCSConstants.CON_FLYIN_CODE_KEY, carTransfer.getJson_Car().get(0).getSTR_LPO_NO() );
			}else{
				//message.setInvocationProperty(ETRCSConstants.VAR_PRODUCT, "CAR" );
				//message.setInvocationProperty(ETRCSConstants.VAR_CAR_TRANSFER, "CAR" );
				//message.setInvocationProperty(ETRCSConstants.CON_FLYIN_CODE_KEY, carTransfer.getJson_Car().get(0).getSTR_LPO_NO() );
				
				 ((Map<String, String>) messageBuilder).put(ETRCSConstants.VAR_PRODUCT, "CAR" );
				 ((Map<String, String>) messageBuilder).put(ETRCSConstants.VAR_CAR_TRANSFER, "CAR" );
				 ((Map<String, String>) messageBuilder).put(ETRCSConstants.CON_FLYIN_CODE_KEY, carTransfer.getJson_Car().get(0).getSTR_LPO_NO() );
			}
			
		}else if(null != other && !other.getJson_Other().isEmpty()){
			//message.setInvocationProperty(ETRCSConstants.VAR_PRODUCT, "OTHER" );
			
			 ((Map<String, String>) messageBuilder).put(ETRCSConstants.VAR_PRODUCT, "OTHER" );
		}
		
		return message;
	}
	
	private TraacsBaseMapper loadTraacsJSONRequestMapper( EventContext evtCtx ) {
		//TraacsBaseMapper mapper = (TraacsBaseMapper) evtCtx.getMuleContext().getRegistry().lookupObject( "TraacsMasterMapperFactoryBean");
		
		
		
		
		TraacsBaseMapper mapper = (TraacsBaseMapper)((Event) evtCtx).getVariables().get( "TraacsMasterMapperFactoryBean").getValue();
		
		
		return mapper;		
	}
}
