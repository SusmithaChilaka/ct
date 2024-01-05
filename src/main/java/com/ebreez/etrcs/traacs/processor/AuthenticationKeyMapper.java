package com.ebreez.etrcs.traacs.processor;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.mule.runtime.api.event.Event;
import org.mule.runtime.api.event.EventContext;
import org.mule.runtime.api.message.Message;
import org.mule.runtime.api.component.execution.ExecutableComponent;
import org.springframework.util.CollectionUtils;

import com.ebreez.etrcs.constants.ETRCSConstants;
import com.ebreez.etrcs.request.vo.TraacsInfoRequest;
import com.ebreez.etrcs.traacs.request.vo.MasterJson;
import com.ebreez.etrcs.traacs.vo.Authentication;
import com.ebreez.etrcs.traacs.vo.TRAACSCacheMap;
import com.ebreez.etrcs.traacs.vo.TicketJsonMaster;

public abstract class AuthenticationKeyMapper implements ExecutableComponent {

	@SuppressWarnings("unchecked")
	public Object onCall(EventContext eventContext) throws Exception {
		Message message = ((Event) eventContext).getMessage();
		String sTR_AUTHENTICATION = TRAACSCacheMap.getCacheAuthKey();
		String flyinCode = null;
		String product = null;
		String domainCT = null;
		Object obj = message.getPayload();
		Authentication auth = null;
		String DOMAIN_TYPE = "DOMAIN_TYPE";
		
		Message.Builder messageBuilder = Message.builder(message);
	
		Message.Builder messageBuilder1 = Message.builder((Message) eventContext);
		
		Map<String, String> properties = (Map<String, String>) ((Event) eventContext).getVariables().get(ETRCSConstants.BEAN_ETRCS_APP_PROPERTIES).getValue();
		
		String flyinGlbDomainName = properties.get(ETRCSConstants.FLYIN_GLOBAL_DOMAIN);
		String flyinEgDomainName = properties.get(ETRCSConstants.FLYIN_EGYPT_DOMAIN);
		
		String[] flyinGlbDomainNameList = flyinGlbDomainName.split("\\|");
		String[] flyinEgDomainNameList = flyinEgDomainName.split("\\|");
		
		Map<String, String> suppllierIdsMap = ((Map<String, String>) ((Event) message).getVariables().get(ETRCSConstants.VAR_SUPPLIER_DATA_MAP).getValue());
		TraacsInfoRequest tcsReq = (TraacsInfoRequest) ((Event) message).getVariables().get(ETRCSConstants.VAR_UPDATE_REQUEST).getValue();
		
		String domainTypeKey = null;
		List<Map<String,Object>> dbDmainType = ((List<Map<String,Object>>) ((Event) message).getVariables().get(ETRCSConstants.DOMAIN_TYPE).getValue());
		if(dbDmainType != null && !dbDmainType.isEmpty()){
			for(Map<String,Object> sid : dbDmainType){
				 domainTypeKey = (String)sid.get(DOMAIN_TYPE);
			}
		}
		
		String companyId = null;
		if(null != tcsReq &&
				null != tcsReq.getTraacInfoRQ() &&
				null != tcsReq.getTraacInfoRQ().getTraacDetails() && !tcsReq.getTraacInfoRQ().getTraacDetails().isEmpty()){
			
			if(null != tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getMaster()){
				companyId = tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getMaster().getCompanyId();
				domainCT = tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getMaster().getDomainTypeCT();
			}
			
			if(null != tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getFlights() && !tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getFlights().isEmpty()){
				product = tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getFlights().get(0).getProduct();
			}else if(null != tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getHotels() && !tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getHotels().isEmpty()){
				product = tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getHotels().get(0).getProduct();
			}else if(null != tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getCars() && !tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getCars().isEmpty()){
				product = tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getCars().get(0).getProduct();
			}else if(null != tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getOthers() && !tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getOthers().isEmpty()){
				product = tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getOthers().get(0).getProduct();
			}
		}
		
		//Egypt & KSA Account
		String priSrvc = null;
		String scndSrvc = null;
		String ctCrossJson = null;
		String isCrossJson = null;
		String paymentMode = null;
		
		if(obj instanceof MasterJson){
			
			if(null != ((MasterJson) obj).getJson_master()){

				String paymentCurr = null;
				paymentCurr = ((MasterJson) obj).getJson_master().getSTR_SELLING_CUR_CODE();
				
				if(null != paymentCurr && !paymentCurr.equalsIgnoreCase("EGP")){
					priSrvc = "GLOBAL";
				}else{
					priSrvc = "EGYPT";
				}
				
				String s = ((MasterJson) obj).getJson_master().getSTR_NARRATION();
				if(null != s){
					String[] s1 = s.split("\\|");
					for(int i = 0; i < s1.length; i++){
						if(s1[i].toLowerCase().contains("cash")){
							paymentMode = "cash";
							break;
						}else if(s1[i].toLowerCase().contains("invoice-to-company")){
							paymentMode = "invoice-to-company";
							break;
						}
					}
				}
			}
			
			if(null != product && (product.equalsIgnoreCase("FLT+HTL") || product.equalsIgnoreCase("PACKAGE"))){
				
				String supplierCode = null;
				String supplierOfficeId = null;
				
				String fltFphScndSrvc = null;
				String htlFphScndSrvc = null;
				
				MasterJson ticket = (MasterJson) obj;
				
				if(null != ((MasterJson) obj).getJson_Ticket() && !((MasterJson) obj).getJson_Ticket().isEmpty()){
					
					if(null != ticket.getJson_Ticket().get(0).getSTR_LPO_NO() && 
							(ticket.getJson_Ticket().get(0).getSTR_LPO_NO().startsWith("E") || ticket.getJson_Ticket().get(0).getSTR_LPO_NO().startsWith("C") ||
							 ticket.getJson_Ticket().get(0).getSTR_LPO_NO().startsWith("22") || ticket.getJson_Ticket().get(0).getSTR_LPO_NO().startsWith("33"))){
						if(null != paymentMode && 
								(paymentMode.equalsIgnoreCase("cash") || paymentMode.equalsIgnoreCase("invoice-to-company"))){
							
							String domainType = suppllierIdsMap.get(ETRCSConstants.VAR_DOMAIN_TYPE_PREFIX + companyId);
							if(null != domainType){
								priSrvc = domainTypeKey = domainType;
							}
						}
					}
	
					if(null != ticket.getJson_Ticket().get(0).getSTR_GDS()){
						supplierCode = ticket.getJson_Ticket().get(0).getSTR_GDS();
					}
					
					if(null != ticket.getJson_Ticket().get(0).getSTR_TICKETING_AGENCY_OFFICE_ID()){
						supplierOfficeId = ticket.getJson_Ticket().get(0).getSTR_TICKETING_AGENCY_OFFICE_ID();
					}
					
					if(null != domainCT){
						if(null != ticket.getJson_Ticket().get(0).getSTR_PURCHASE_CUR_CODE() && 
								ticket.getJson_Ticket().get(0).getSTR_PURCHASE_CUR_CODE().equalsIgnoreCase("EGP")){
							fltFphScndSrvc = "EGYPT";
						}else{
							fltFphScndSrvc = "GLOBAL";
						}
						ctCrossJson = "false";
					}else if(null != supplierCode && supplierCode.equalsIgnoreCase("AMADEUS") && null != supplierOfficeId){
						
						String egOfficeID = properties.get(ETRCSConstants.EGYPT_OFFICE_ID);
						String ctOfficeID = properties.get(ETRCSConstants.CLEARTRIP_OFFICE_ID);
						
						String[] egOfficeIDList = egOfficeID.split("\\|");
						String[] ctOfficeIDList = ctOfficeID.split("\\|");
						if(Arrays.asList(egOfficeIDList).contains(supplierOfficeId)){
							fltFphScndSrvc = "EGYPT";
						}else if(Arrays.asList(ctOfficeIDList).contains(supplierOfficeId)){
							
							if(null != domainTypeKey && null != flyinGlbDomainNameList && 
									(Arrays.asList(flyinGlbDomainNameList).contains(domainTypeKey) || domainTypeKey.equals("GLOBAL"))){
								fltFphScndSrvc = priSrvc = "GLOBAL";
							}else if(null != domainTypeKey && null != flyinEgDomainNameList && 
									(Arrays.asList(flyinEgDomainNameList).contains(domainTypeKey) || domainTypeKey.equals("EGYPT"))){
								fltFphScndSrvc = priSrvc = "EGYPT";	
							}
							
							ctCrossJson = "false";
						}else{
							String danataOid = properties.get(ETRCSConstants.DANATA_OFFICE_ID);
							
							
							if(null != supplierOfficeId && supplierOfficeId.equalsIgnoreCase(danataOid)){
								fltFphScndSrvc = "GLOBAL";
								ctCrossJson = "false";
							}else{
								fltFphScndSrvc = "GLOBAL";
							}
						}
					}else{
						fltFphScndSrvc = "GLOBAL";
					}
				}

				if(null != ((MasterJson) obj).getJson_Hotel() && !((MasterJson) obj).getJson_Hotel().isEmpty()){
					MasterJson hotel = (MasterJson) obj;
					
					if(null != hotel.getJson_Hotel().get(0).getSTR_LPO_NO() && 
							(hotel.getJson_Hotel().get(0).getSTR_LPO_NO().startsWith("E") || hotel.getJson_Hotel().get(0).getSTR_LPO_NO().startsWith("C") ||
							 hotel.getJson_Hotel().get(0).getSTR_LPO_NO().startsWith("22") || hotel.getJson_Hotel().get(0).getSTR_LPO_NO().startsWith("33"))){
						if(null != paymentMode && 
								(paymentMode.equalsIgnoreCase("cash") || paymentMode.equalsIgnoreCase("invoice-to-company"))){
							
							String domainType = suppllierIdsMap.get(ETRCSConstants.VAR_DOMAIN_TYPE_PREFIX + companyId);
							if(null != domainType){
								priSrvc = domainType;
							}
						}
					}
					
					String sc = hotel.getJson_Hotel().get(0).getSTR_SUPPLIER_CODE() != null ? hotel.getJson_Hotel().get(0).getSTR_SUPPLIER_CODE().toUpperCase() : "";
					String domainType = suppllierIdsMap.get(sc);
					if(null != domainType){
						String[] dt = domainType.split("\\!");
						if (dt.length > 0) {
							domainType = dt[0];
						}
					}
					
					if(null != domainCT){
						if(null != hotel.getJson_Hotel().get(0).getSTR_PURCHASE_CUR_CODE() && 
								hotel.getJson_Hotel().get(0).getSTR_PURCHASE_CUR_CODE().equalsIgnoreCase("EGP")){
							htlFphScndSrvc = "EGYPT";
						}else{
							htlFphScndSrvc = "GLOBAL";
						}
					}else if(null != domainType && domainType.equals("EGYPT")){
						htlFphScndSrvc = "EGYPT";
					}else{
						htlFphScndSrvc = "GLOBAL";
					}
					
					if(null != hotel.getJson_Hotel().get(0).getSTR_SUPPLIER_CODE() && (hotel.getJson_Hotel().get(0).getSTR_SUPPLIER_CODE().equals("C0012") ||  
							hotel.getJson_Hotel().get(0).getSTR_SUPPLIER_CODE().equals("C0023") || hotel.getJson_Hotel().get(0).getSTR_SUPPLIER_CODE().equals("C0022"))){
						ctCrossJson = "false";
					}
				}
				
				if((null != fltFphScndSrvc && null != htlFphScndSrvc) && !fltFphScndSrvc.equalsIgnoreCase(htlFphScndSrvc)){
					if(null != priSrvc && priSrvc.equalsIgnoreCase("EGYPT")){
						scndSrvc = "GLOBAL";
					}else if(null != priSrvc && priSrvc.equalsIgnoreCase("GLOBAL")){
						scndSrvc = "EGYPT";
					}
				}else{
					

					if (null != fltFphScndSrvc) {
					    scndSrvc = fltFphScndSrvc;
					    ((Map<String, String>) messageBuilder).put(ETRCSConstants.FPH_SECONDARY_DOMAIN, "FLIGHT");
					}else if(null != htlFphScndSrvc){
						scndSrvc = htlFphScndSrvc;
						 ((Map<String, String>) messageBuilder).put(ETRCSConstants.FPH_SECONDARY_DOMAIN, "HOTEL");
					}else{
						scndSrvc = "GLOBAL";
						 ((Map<String, String>) messageBuilder).put(ETRCSConstants.FPH_SECONDARY_DOMAIN, "PACKAGE");
					}
				}
				
				if((null != priSrvc && null != fltFphScndSrvc && null != htlFphScndSrvc) && 
						(!priSrvc.equals(fltFphScndSrvc) && !priSrvc.equals(htlFphScndSrvc))){
					 ((Map<String, String>) messageBuilder1).put(ETRCSConstants.FPH_SECONDARY_DOMAIN, "FLT+HTL");
				}else if((null != priSrvc && null != fltFphScndSrvc) && !priSrvc.equals(fltFphScndSrvc)){
					 ((Map<String, String>) messageBuilder).put(ETRCSConstants.FPH_SECONDARY_DOMAIN, "FLIGHT");
				}else if((null != priSrvc && null != htlFphScndSrvc) && !priSrvc.equals(htlFphScndSrvc)){
					 ((Map<String, String>) messageBuilder).put(ETRCSConstants.FPH_SECONDARY_DOMAIN, "HOTEL");
				}
				
			}else{
				if(null != ((MasterJson) obj).getJson_Ticket() && !((MasterJson) obj).getJson_Ticket().isEmpty()){
					
					String supplierCode = null;
					String supplierOfficeId = null;
					
					MasterJson ticket = (MasterJson) obj;
					
					if(null != ticket.getJson_Ticket().get(0).getSTR_LPO_NO() && 
							(ticket.getJson_Ticket().get(0).getSTR_LPO_NO().startsWith("E") || ticket.getJson_Ticket().get(0).getSTR_LPO_NO().startsWith("C") ||
							 ticket.getJson_Ticket().get(0).getSTR_LPO_NO().startsWith("22") || ticket.getJson_Ticket().get(0).getSTR_LPO_NO().startsWith("33"))){
						if(null != paymentMode && 
								(paymentMode.equalsIgnoreCase("cash") || paymentMode.equalsIgnoreCase("invoice-to-company"))){
							
							String domainType = suppllierIdsMap.get(ETRCSConstants.VAR_DOMAIN_TYPE_PREFIX + companyId);
							if(null != domainType){
								priSrvc = domainTypeKey = domainType;
							}
						}
					}

					String fcScndSrvc1 = null;
					String officeID1 = null;
					String fcScndSrvc2 = null;
					
					for(TicketJsonMaster tkt : ticket.getJson_Ticket()){
						if(null != tkt.getSTR_GDS()){
							supplierCode = tkt.getSTR_GDS();
						}
						
						if(null != tkt.getSTR_TICKETING_AGENCY_OFFICE_ID()){
							supplierOfficeId = tkt.getSTR_TICKETING_AGENCY_OFFICE_ID();
						}
						
						if(null != domainCT){
							if(null != tkt.getSTR_PURCHASE_CUR_CODE() && tkt.getSTR_PURCHASE_CUR_CODE().equalsIgnoreCase("EGP")){
								fcScndSrvc1 = "EGYPT";
							}else{
								fcScndSrvc1 = "GLOBAL";
							}
							ctCrossJson = "false";
						}else if(null != supplierCode && supplierCode.equalsIgnoreCase("AMADEUS") && null != supplierOfficeId){
							if(null == fcScndSrvc1){
								
								String egOfficeID = properties.get(ETRCSConstants.EGYPT_OFFICE_ID);
								String ctOfficeID = properties.get(ETRCSConstants.CLEARTRIP_OFFICE_ID);
								
								String[] egOfficeIDList = egOfficeID.split("\\|");
								String[] ctOfficeIDList = ctOfficeID.split("\\|");
								
								officeID1 = supplierOfficeId;
								if(Arrays.asList(egOfficeIDList).contains(supplierOfficeId)){
									fcScndSrvc1 = "EGYPT";
								}else if(Arrays.asList(ctOfficeIDList).contains(supplierOfficeId)){
									
									if(null != domainTypeKey && null != flyinGlbDomainName && 
											(Arrays.asList(flyinGlbDomainNameList).contains(domainTypeKey) || domainTypeKey.equals("GLOBAL"))){
										fcScndSrvc1 = priSrvc = "GLOBAL";
									}else if(null != domainTypeKey && null != flyinEgDomainNameList && 
											(Arrays.asList(flyinEgDomainNameList).contains(domainTypeKey) || domainTypeKey.equals("EGYPT"))){
										fcScndSrvc1 = priSrvc = "EGYPT";	
									}
									
									ctCrossJson = "false";
								}else{
									String danataOid = properties.get(ETRCSConstants.DANATA_OFFICE_ID);
									if(null != supplierOfficeId && supplierOfficeId.equalsIgnoreCase(danataOid)){
										fcScndSrvc1 = "GLOBAL";
										ctCrossJson = "false";
										
										String domainType = suppllierIdsMap.get(ETRCSConstants.VAR_DOMAIN_TYPE_PREFIX + companyId);
										priSrvc = domainType;
									}else{
										fcScndSrvc1 = "GLOBAL";
									}
								}
							}else if(!officeID1.equalsIgnoreCase(supplierOfficeId) && fcScndSrvc2 == null){
								
								String egOfficeID = properties.get(ETRCSConstants.EGYPT_OFFICE_ID);
								String ctOfficeID = properties.get(ETRCSConstants.CLEARTRIP_OFFICE_ID);
								
								String[] egOfficeIDList = egOfficeID.split("\\|");
								String[] ctOfficeIDList = ctOfficeID.split("\\|");
								
								if(Arrays.asList(egOfficeIDList).contains(supplierOfficeId)){
									fcScndSrvc2 = "EGYPT";
								}else if(Arrays.asList(ctOfficeIDList).contains(supplierOfficeId)){
									
									if(null != domainTypeKey && null != flyinGlbDomainNameList && 
											(Arrays.asList(flyinGlbDomainNameList).contains(domainTypeKey) || domainTypeKey.equals("GLOBAL"))){
										fcScndSrvc2 = priSrvc = "GLOBAL";
									}else if(null != domainTypeKey && null != flyinEgDomainNameList && 
											(Arrays.asList(flyinEgDomainNameList).contains(domainTypeKey) || domainTypeKey.equals("EGYPT"))){
										fcScndSrvc2 = priSrvc = "EGYPT";	
									}
									
									ctCrossJson = "false";
								}else{

									String danataOid = properties.get(ETRCSConstants.DANATA_OFFICE_ID);
									if(null != supplierOfficeId && supplierOfficeId.equalsIgnoreCase(danataOid)){
										fcScndSrvc2 = "GLOBAL";
										ctCrossJson = "false";
									}else{
										fcScndSrvc2 = "GLOBAL";
									}
								}
							}
						}else{
							fcScndSrvc2 = "GLOBAL";
						}
					}
					
					if((null != fcScndSrvc1 && null != fcScndSrvc2) && !fcScndSrvc1.equalsIgnoreCase(fcScndSrvc2)){
						((Map<String, String>) messageBuilder).put(ETRCSConstants.FC_BOOKING_SECONDARY_DOMAIN, "FC_BOOKING");
						if(null != priSrvc && priSrvc.equalsIgnoreCase("EGYPT")){
							scndSrvc = "GLOBAL";
						}else if(null != priSrvc && priSrvc.equalsIgnoreCase("GLOBAL")){
							scndSrvc = "EGYPT";
						}
					}else{
						if(null != fcScndSrvc1)
							scndSrvc = fcScndSrvc1;
						else if(null != fcScndSrvc2)
							scndSrvc = fcScndSrvc2;
					}
					
				}else if(null != ((MasterJson) obj).getJson_Hotel() && !((MasterJson) obj).getJson_Hotel().isEmpty()){
					MasterJson hotel = (MasterJson) obj;
					
					if(null != hotel.getJson_Hotel().get(0).getSTR_LPO_NO() && 
							(hotel.getJson_Hotel().get(0).getSTR_LPO_NO().startsWith("E") || hotel.getJson_Hotel().get(0).getSTR_LPO_NO().startsWith("C") ||
							 hotel.getJson_Hotel().get(0).getSTR_LPO_NO().startsWith("22") || hotel.getJson_Hotel().get(0).getSTR_LPO_NO().startsWith("33"))){
						if(null != paymentMode && 
								(paymentMode.equalsIgnoreCase("cash") || paymentMode.equalsIgnoreCase("invoice-to-company"))){
							
							String domainType = suppllierIdsMap.get(ETRCSConstants.VAR_DOMAIN_TYPE_PREFIX + companyId);
							if(null != domainType){
								priSrvc = domainType;
							}
						}
					}
					
					String sc = hotel.getJson_Hotel().get(0).getSTR_SUPPLIER_CODE() != null ? hotel.getJson_Hotel().get(0).getSTR_SUPPLIER_CODE().toUpperCase() : "";
					String domainType = suppllierIdsMap.get(sc);
					if(null != domainType){
						String[] dt = domainType.split("\\!");
						if (dt.length > 0) {
							domainType = dt[0];
						}
					}
					
					if(null != domainCT){
						if(null != hotel.getJson_Hotel().get(0).getSTR_PURCHASE_CUR_CODE() && 
								hotel.getJson_Hotel().get(0).getSTR_PURCHASE_CUR_CODE().equalsIgnoreCase("EGP")){
							scndSrvc = "EGYPT";
						}else{
							scndSrvc = "GLOBAL";
						}
					}else if(null != domainType && domainType.equals("EGYPT")){
						scndSrvc = "EGYPT";
					}else{
						scndSrvc = "GLOBAL";
					}
					
					if(null != hotel.getJson_Hotel().get(0).getSTR_SUPPLIER_CODE() && (hotel.getJson_Hotel().get(0).getSTR_SUPPLIER_CODE().equals("C0012") ||  
							hotel.getJson_Hotel().get(0).getSTR_SUPPLIER_CODE().equals("C0023") || hotel.getJson_Hotel().get(0).getSTR_SUPPLIER_CODE().equals("C0022"))){
						ctCrossJson = "false";
					}
					
				}else if(null != ((MasterJson) obj).getJson_Car() && !((MasterJson) obj).getJson_Car().isEmpty()){
					MasterJson car = (MasterJson) obj;
					
					if(null != car.getJson_Car().get(0).getSTR_LPO_NO() && 
							(car.getJson_Car().get(0).getSTR_LPO_NO().startsWith("E") || car.getJson_Car().get(0).getSTR_LPO_NO().startsWith("C") ||
							 car.getJson_Car().get(0).getSTR_LPO_NO().startsWith("22") || car.getJson_Car().get(0).getSTR_LPO_NO().startsWith("33"))){
						if(null != paymentMode && 
								(paymentMode.equalsIgnoreCase("cash") || paymentMode.equalsIgnoreCase("invoice-to-company"))){
							
							String domainType = suppllierIdsMap.get(ETRCSConstants.VAR_DOMAIN_TYPE_PREFIX + companyId);
							if(null != domainType){
								priSrvc = domainType;
							}
						}
					}
					
					scndSrvc = "GLOBAL";
					
				}else if(null != ((MasterJson) obj).getJson_Other() && !((MasterJson) obj).getJson_Other().isEmpty()){
					MasterJson oth = (MasterJson) obj;
					
					if(null != oth.getJson_Other().get(0).getSTR_LPO_NO() && 
							(oth.getJson_Other().get(0).getSTR_LPO_NO().startsWith("E") || oth.getJson_Other().get(0).getSTR_LPO_NO().startsWith("C") ||
							 oth.getJson_Other().get(0).getSTR_LPO_NO().startsWith("22") || oth.getJson_Other().get(0).getSTR_LPO_NO().startsWith("33"))){
						if(null != paymentMode && 
								(paymentMode.equalsIgnoreCase("cash") || paymentMode.equalsIgnoreCase("invoice-to-company"))){
							
							String domainType = suppllierIdsMap.get(ETRCSConstants.VAR_DOMAIN_TYPE_PREFIX + companyId);
							if(null != domainType){
								priSrvc = domainType;
							}
						}
					}
					
					scndSrvc = "GLOBAL";
				}
			}
		}
		
		if(obj instanceof MasterJson){
			
			if(null != ((MasterJson) obj).getJson_Ticket() && !((MasterJson) obj).getJson_Ticket().isEmpty()){
				MasterJson ticket =(MasterJson) obj ;
				auth = ticket.getStr_authentication_key();
				
				String tktNo = null;
				String[] ticketNos = null;
				
				for(TicketJsonMaster tkts : ((MasterJson) obj).getJson_Ticket()){
					if(null != tktNo){
						tktNo = tktNo.concat("_" + tkts.getSTR_TICKET_NO());
					}else{
						tktNo = tkts.getSTR_TICKET_NO();
					}
				}
				
				if(null != tktNo && tktNo.contains("_")){
					ticketNos = tktNo.split("_");
					Arrays.sort(ticketNos);
					tktNo = null;
					for (int i = 0; i < ticketNos.length; i++){
						if(tktNo == null){
							tktNo = ticketNos[i];
						}else{
							tktNo = tktNo.concat("_" + ticketNos[i]);
						}
					}
				}

				flyinCode = ticket.getJson_Ticket().get(0).getSTR_LPO_NO()+"_"+tktNo;
				
				if(null != tcsReq &&
						null != tcsReq.getTraacInfoRQ() &&
						null != tcsReq.getTraacInfoRQ().getTraacDetails() && !tcsReq.getTraacInfoRQ().getTraacDetails().isEmpty() &&
						null != tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getFlights() && !tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getFlights().isEmpty()){

					String segNo = tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getFlights().get(0).getSegmentId(); 
					
					if(null != segNo && segNo.contains(",")){
						String[] segments = segNo.split(",");
						Arrays.sort(segments);
						for(int i = 0; i < segments.length; i++){
							if(segNo == null){
								segNo = segments[i];
							}else{
								segNo = segNo.concat("_" + segments[i]);
							}
						}
					}
					
					if(null != segNo){
						flyinCode = flyinCode+"_"+segNo;
					}
				}
				
			}else if(null != ((MasterJson) obj).getJson_Hotel() && !((MasterJson) obj).getJson_Hotel().isEmpty()){
				MasterJson hotel =(MasterJson) obj ;
				auth = hotel.getStr_authentication_key();
				flyinCode = hotel.getJson_Hotel().get(0).getSTR_LPO_NO();
			}else if(null != ((MasterJson) obj).getJson_Car() && !((MasterJson) obj).getJson_Car().isEmpty()){
				MasterJson car =(MasterJson) obj ;
				auth = car.getStr_authentication_key();
				flyinCode = car.getJson_Car().get(0).getSTR_LPO_NO();
			}else if(null != ((MasterJson) obj).getJson_Other() && !((MasterJson) obj).getJson_Other().isEmpty()){
				MasterJson oth =(MasterJson) obj ;
				auth = oth.getStr_authentication_key();
				flyinCode = oth.getJson_Other().get(0).getSTR_LPO_NO();
			}
		}
		if(null != auth)
			auth.setSTR_AUTHENTICATION(sTR_AUTHENTICATION);
		
		final String AB = "01234ABCDE";
		SecureRandom rnd = new SecureRandom();
		int len =10;
		StringBuilder sb = new StringBuilder( len );
		
		for( int i = 0; i < len; i++ ) 
			sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
		
 		auth.setSTR_REFERENCE_KEY(sb.toString());
		
		
 		((Map<String, Object>) messageBuilder).put(ETRCSConstants.VAR_JSON_REQUEST, obj);
 		((Map<String, String>) messageBuilder).put("FileNameFlyinCode", flyinCode);
		
		if(null != ctCrossJson && ctCrossJson.equals("false")){
			isCrossJson = "false";
		}else if(null != priSrvc && null != scndSrvc && (priSrvc.equals(scndSrvc))){
			isCrossJson = "false";
		}else{
			isCrossJson = "true";
		}
		 ((Map<String, String>) messageBuilder).put("CrossJson", isCrossJson);
		 ((Map<String, String>) messageBuilder).put(ETRCSConstants.TRAACS_CROSS_JSON, isCrossJson);
		Map<String, String> bookingDomainTypeMap =(Map<String, String>) ((Event)message).getVariables().get(ETRCSConstants.CON_FAILURE_DOMAIN_TYPE).getValue();
		if(!CollectionUtils.isEmpty(bookingDomainTypeMap)){
			 if(null != tcsReq &&
					null != tcsReq.getTraacInfoRQ() &&
					null != tcsReq.getTraacInfoRQ().getTraacDetails() && !tcsReq.getTraacInfoRQ().getTraacDetails().isEmpty()){
				
				 if(null != tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getMaster()){
					 String domain = bookingDomainTypeMap.get(tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getMaster().getJsonfrom());
					 if(domain != null && domain.equalsIgnoreCase(priSrvc)){
						 ((Map<String, String>) messageBuilder).put("PrimaryService", priSrvc);
						 ((Map<String, String>) messageBuilder).put(ETRCSConstants.TRAACS_PRIMARY_DOMAIN, priSrvc);
					 }else if(domain != null && domain.equalsIgnoreCase(scndSrvc)){
						 ((Map<String, String>) messageBuilder).put("SecondarySerivce", scndSrvc);
						 ((Map<String, String>) messageBuilder).put(ETRCSConstants.TRAACS_SECONDARY_DOMAIN, scndSrvc);
					 }else{
						 ((Map<String, String>) messageBuilder).put("PrimaryService", priSrvc);
						 ((Map<String, String>) messageBuilder).put(ETRCSConstants.TRAACS_PRIMARY_DOMAIN, priSrvc);
						 ((Map<String, String>) messageBuilder).put("SecondarySerivce", scndSrvc);
						 ((Map<String, String>) messageBuilder).put(ETRCSConstants.TRAACS_SECONDARY_DOMAIN, scndSrvc);
					 }
				 }
			 }
		 }else{
			 ((Map<String, String>) messageBuilder).put("PrimaryService", priSrvc);
			 ((Map<String, String>) messageBuilder).put(ETRCSConstants.TRAACS_PRIMARY_DOMAIN, priSrvc);
			 ((Map<String, String>) messageBuilder).put("SecondarySerivce", scndSrvc);
			 ((Map<String, String>) messageBuilder).put(ETRCSConstants.TRAACS_SECONDARY_DOMAIN, scndSrvc);
		 }
		
		return message;
	}

}
