package com.ebreez.etrcs.traacs.mappers;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.mule.runtime.api.event.Event;
import org.mule.runtime.api.message.Message;
import org.mule.runtime.api.metadata.TypedValue;
import org.springframework.util.StringUtils;

import com.ebreez.etrcs.constants.ETRCSConstants;
import com.ebreez.etrcs.request.vo.Crt;
import com.ebreez.etrcs.request.vo.HCP;
import com.ebreez.etrcs.request.vo.HotelStaticDataRequest;
import com.ebreez.etrcs.request.vo.Source;
import com.ebreez.etrcs.request.vo.TraacsInfo;
import com.ebreez.etrcs.request.vo.TraacsInfoRequest;
import com.ebreez.etrcs.response.vo.HotelStaticDataResponse;
import com.ebreez.etrcs.traacs.request.vo.MasterJson;
import com.ebreez.etrcs.traacs.util.MapperUtil;
import com.ebreez.etrcs.traacs.vo.Authentication;
import com.ebreez.etrcs.traacs.vo.CarDetailsJson;
import com.ebreez.etrcs.traacs.vo.CarJsonMaster;
import com.ebreez.etrcs.traacs.vo.HotelJsonMaster;
import com.ebreez.etrcs.traacs.vo.MasterDetailsJson;
import com.ebreez.etrcs.traacs.vo.OtherJsonMaster;
import com.ebreez.etrcs.traacs.vo.RoomDetailsJson;
import com.ebreez.etrcs.traacs.vo.SharingJsonMaster;
import com.ebreez.etrcs.traacs.vo.TicketJsonMaster;
import com.ebreez.etrcs.util.StandAloneRequestBuilderUtil;
import com.ebreez.etrcs.vo.Car;
import com.ebreez.etrcs.vo.CarDetails;
import com.ebreez.etrcs.vo.Flight;
import com.ebreez.etrcs.vo.Hotel;
import com.ebreez.etrcs.vo.Other;
import com.ebreez.etrcs.vo.RoomDetails;
import com.ebreez.etrcs.vo.SalesMaster;
import com.ebreez.etrcs.vo.Sharing;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class TraacsMasterRequestMapper extends TraacsBaseMapper {
	
	
	private static Log logger = LogFactory.getLog(ETRCSConstants.LOGGER_NAME);

	Map<String, Map<String, List<Flight>>> vmpdFlights;
	Map<String, List<Flight>> etFlights;
	Map<String, String> suppllierIdsMap;
	String fopCode;
	Integer reqCount = null;
	
	

	@SuppressWarnings({ "unchecked", "unused" })
	public Object map(Object... fromObjects) {

		
		
		// this does the initialization of action, status and autoInvoice
		super.map(fromObjects);
		
		MasterJson masterJson = new MasterJson();
		Authentication authInfo = (Authentication) fromObjects[0];
		Map<String, String> appProps = (Map<String, String>) fromObjects[1];
		Message msg = (Message) fromObjects[2];
		String product = (String) fromObjects[3];
		suppllierIdsMap = (Map<String, String>) ((Event) msg).getVariables().get(ETRCSConstants.VAR_SUPPLIER_DATA_MAP).getValue();
		
		if(null != ((Event) msg).getVariables().get(ETRCSConstants.VAR_REQUEST_COUNT)){
			List<Map<String,String>> countFromDB = (List<Map<String,String>>)((Event) msg).getVariables().get(ETRCSConstants.VAR_REQUEST_COUNT).getValue();
			String REQUEST_COUNT = "REQUEST_COUNT";
			for(Map<String,String> c : countFromDB){
				if(null != c.get(REQUEST_COUNT)){
					reqCount = Integer.parseInt(c.get(REQUEST_COUNT));
					break;
				}
			}
		}
		
		TraacsInfoRequest tcsReq = (TraacsInfoRequest)((Event) msg).getVariables().get(ETRCSConstants.VAR_UPDATE_REQUEST).getValue();
		MasterDetailsJson masterDetails = new MasterDetailsJson();
		List<SharingJsonMaster> sharingDetails = new ArrayList<SharingJsonMaster>();
		List<OtherJsonMaster> otherDetails = new ArrayList<OtherJsonMaster>();
		List<TraacsInfo> traacInfoList = tcsReq.getTraacInfoRQ().getTraacDetails();
		
		List<TicketJsonMaster> tickets = new ArrayList<TicketJsonMaster>();
		vmpdFlights = new HashMap<String, Map<String, List<Flight>>>();
		etFlights = new HashMap<String, List<Flight>>();
		
		Map<String,String> suppllierIdsMap = (Map<String, String>) ((Event) msg).getVariables().get( ETRCSConstants.VAR_SUPPLIER_DATA_MAP ).getValue();
		List<HotelJsonMaster> hotels = new ArrayList<HotelJsonMaster>();
		
		List<CarJsonMaster> cars = new ArrayList<CarJsonMaster>();
		
		 ((Map<String,TypedValue<?>>) msg).remove(ETRCSConstants.REFUND_PAX_ID);
		 ((Map<String,TypedValue<?>>) msg).remove(ETRCSConstants.REFUND_SEGMENT_ID);
		
		String lpo = null;
		if(null != tcsReq && null != tcsReq.getTraacInfoRQ() && null != tcsReq.getTraacInfoRQ().getTraacDetails() &&
				!tcsReq.getTraacInfoRQ().getTraacDetails().isEmpty() && null != tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getMaster()){
			lpo = tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getMaster().getFlyinCode();
		}
			
		logger.info("TraacsMasterRequestMapper for Booking : " + lpo + " START");
		
		for (TraacsInfo traacInfo : traacInfoList) {

			SalesMaster master = traacInfo.getMaster();
			List<Sharing> sharing = traacInfo.getSharing();
			List<Other> other = traacInfo.getOthers();

			boolean vmpdTicketsFlag = false;
			
			Double couponDisc = 0.0;
			Double vatPercentage = 0.0;
			if(null != master && null != master.getCouponDiscount()){
				couponDisc = master.getCouponDiscount();
			}
			
			if(product.equalsIgnoreCase("fltHtl") || product.equalsIgnoreCase("package") || product.equalsIgnoreCase("activity")){
				
				populateMasterData(master, masterDetails);
				populateSharingData(sharing, sharingDetails);
				populateOtherData(other, otherDetails, msg, master);

				if ((traacInfo.getFlights() != null && !traacInfo.getFlights().isEmpty())) {
					
					// Check whether the tickets type is VMPD and split the the tickets
					splitTicketsBasedOnType(traacInfo);

					Double totSellPrice = 0.0;
					if (vmpdFlights.containsKey(master.getFlyinCode())) {
						for (List<Flight> vmpdTickets : vmpdFlights.get(master.getFlyinCode()).values()) {
							for (Flight aTicket : vmpdTickets) {
								totSellPrice = totSellPrice + (aTicket.getSellingCurrPrice() != null ? aTicket.getSellingCurrPrice() : 0.0);
							}
						}
					}
					
					if (etFlights.get(master.getFlyinCode()) != null && !etFlights.get(master.getFlyinCode()).isEmpty()) {
						for (Flight flight : etFlights.get(master.getFlyinCode())) {
							totSellPrice = totSellPrice + (flight.getSellingCurrPrice() != null ? flight.getSellingCurrPrice() : 0.0);
						}
					}

					// Build the JSON for VMPD tickets by applying logic
					if (vmpdFlights.containsKey(master.getFlyinCode())) {
						for (List<Flight> vmpdTickets : vmpdFlights.get(master.getFlyinCode()).values()) {
							vmpdTicketsFlag = vmpdTickets.size() > 0;
							if (vmpdTicketsFlag) {
								
								TicketJsonMaster tempVMPDTicketDetails = new TicketJsonMaster();
								Flight flight = null;
								if (flight == null) {
									for(Flight f : vmpdTickets){
										if(null != f.getPaxType() && f.getPaxType().equalsIgnoreCase("ADT")){
											flight = f;
											break;
										}else if(null != f.getRefundStatus() && f.getRefundStatus().equalsIgnoreCase("REFUND")){
											flight = f;
											break;
										}
										
										if (flight == null) {
											if(null != f.getTicketType() && f.getTicketType().equalsIgnoreCase("VMPD")){
												flight = f;
												break;
											}
										}
									}
								}
								logger.info("TraacsMasterRequestMapper VMPD Flight for Booking : " + flight.getLpo() + " START");
								computePricesForVMPD(vmpdTickets, tempVMPDTicketDetails, master, totSellPrice);
								populateVMPDTicketFares(tempVMPDTicketDetails, master);
								populateFlightCommonData(flight, tempVMPDTicketDetails); 
								String additionalPax = computeAdditionalPaxInfo(vmpdTickets, flight, master.getFlyinCode());
								if (additionalPax.trim().length() > 0) {
									tempVMPDTicketDetails.setSTR_ADDITIONAL_PAX(additionalPax);
								}else{
									tempVMPDTicketDetails.setSTR_ADDITIONAL_PAX(flight.getAdditionalPax());
								}
								
								tickets.add(tempVMPDTicketDetails);
								masterJson.setStr_authentication_key(authInfo);
								masterJson.setJson_Ticket(tickets);
							}
						}
					}
					
					// Build the JSONs for ET tickets
					if (etFlights.get(master.getFlyinCode()) != null && !etFlights.get(master.getFlyinCode()).isEmpty()) {
						for (Flight flight : etFlights.get(master.getFlyinCode())) {
							
							logger.info("TraacsMasterRequestMapper ET Flight for Booking : " + flight.getLpo() + " START");
							
							TicketJsonMaster ticketDetails = new TicketJsonMaster();
							//new added
							Message.Builder messageBuilder2 = Message.builder(msg); 
							
							populateFlightCommonData(flight, ticketDetails);
							
							if( null != ((Event) msg).getVariables().get(ETRCSConstants.REFUND_PAX_ID) ){
								 ((Map<String, String>) messageBuilder2).put( ETRCSConstants.REFUND_PAX_ID, ((Event) msg).getVariables().get(ETRCSConstants.REFUND_PAX_ID)+","+flight.getPaxId() );
							} else {
								 ((Map<String, String>) messageBuilder2).put( ETRCSConstants.REFUND_PAX_ID, flight.getPaxId() );
							}
							
							 ((Map<String, String>) messageBuilder2).put( ETRCSConstants.REFUND_SEGMENT_ID, flight.getSegmentId());
							
							ticketDetails.setSTR_LPO_NO(flight.getLpo());
							ticketDetails.setSTR_PRODUCT(flight.getProduct());
							ticketDetails.setSTR_TICKET_ISSUE_DATE(StandAloneRequestBuilderUtil.dateFormatter(flight.getTicketIssueDt()));
							ticketDetails.setSTR_REPORTING_DATE(StandAloneRequestBuilderUtil.dateFormatter(flight.getReportingDt()));
							ticketDetails.setSTR_ADDITIONAL_PAX(flight.getAdditionalPax());
							ticketDetails.setSTR_AIRLINE_PNR(flight.getAirlinePnr());
							ticketDetails.setINT_NO_OF_PAX(flight.getNoOfPax());
							ticketDetails.setSTR_PURCHASE_CUR_CODE(flight.getPurchaseCurrCode());
							ticketDetails.setSTR_REGION_CODE(flight.getRegionCode());
							
							ticketDetails.setDBL_PURCHASE_CUR_PUBLISHED_FARE(flight.getPurchaseCurrPubFare());
							ticketDetails.setDBL_PURCHASE_CUR_TOTAL_MARKET_FARE(flight.getPurchaseCurrPubFare());
							ticketDetails.setDBL_PURCHASE_CUR_TOTAL_TAX(flight.getPurchaseCurrTotTax());
							ticketDetails.setDBL_PURCHASE_CUR_STD_COMMISION(flight.getPurchaseCurrStdComm());
							ticketDetails.setDBL_PURCHASE_CUR_SUPPLIER_AMOUNT(flight.getPurchaseCurrSupplierAmt());
							
							Double negMkp = 0.0;
							if(null != master && (null != master.getAccountCode() && !master.getAccountCode().equalsIgnoreCase("C0006")) || (null == master.getAccountCode())){
								if(null != flight.getSellingCurrServiceFee() && flight.getSellingCurrServiceFee() < 0){
									negMkp = negMkp + flight.getSellingCurrServiceFee();
								}else{
									ticketDetails.setDBL_SELLING_CUR_SERVICE_FEE(flight.getSellingCurrServiceFee());
								}
								
								if(null != flight.getSellingCurrExtraEarning() && flight.getSellingCurrExtraEarning() < 0){
									negMkp = negMkp + flight.getSellingCurrExtraEarning();
								}else{
									ticketDetails.setDBL_SELLING_CUR_EXTRA_EARNING(flight.getSellingCurrExtraEarning());
								}
							}
						
							Double disc = flight.getSellingCurrDiscount() != null ? flight.getSellingCurrDiscount() : 0.0;
							ticketDetails.setDBL_SELLING_CUR_DISCOUNT(setScaleNConvertToDouble(disc + Math.abs(negMkp)));
							
							if(null == master.getRefundDate()){
								Double sellingCurPrice = flight.getSellingCurrPrice() != null ? flight.getSellingCurrPrice() : 0.0;
								ticketDetails.setDBL_SELLING_CUR_PRICE(sellingCurPrice);
								Double cd = (sellingCurPrice * couponDisc) / totSellPrice;
								ticketDetails.setDBL_SELLING_CUR_OUTPUT_VAT(calculateVAT((sellingCurPrice - cd), vatPercentage));
							}
							ticketDetails.setDBL_PURCHASE_CUR_INPUT_VAT(flight.getPurchaseCurrInputVAT());
							
							if(null != master.getRefundDate()){
								ticketDetails.setSTR_REFUND_DATE(StandAloneRequestBuilderUtil.dateFormatter(flight.getRefundDate()));
								ticketDetails.setSTR_REFUND_STAFF_EMAIL_ID(flight.getRefundStaffEmailId());
								ticketDetails.setSTR_REFUND_STATUS(flight.getRefundStatus());
								ticketDetails.setDBL_PURCHASE_CUR_AIRLINE_CHARGE(flight.getPurchaseCurrAirlineCharges()); 
								ticketDetails.setDBL_PURCHASE_CUR_SUPPLIER_NET(flight.getPurchaseCurrSupplierNet());
								ticketDetails.setDBL_SELLING_CUR_AGENCY_CHARGE(flight.getSellingCurrAgencyCharges());
								ticketDetails.setDBL_SELLING_CUR_CLIENT_NET(flight.getSellingCurrClientNet());
								ticketDetails.setDBL_SELLING_CUR_OUTPUT_VAT(flight.getSellingCurrOutputVAT());
							}
							
							ticketDetails.setDBL_SELLING_CUR_CREDIT_CARD_CHARGES(flight.getSellingCurrCcCharges());
							
							tickets.add(ticketDetails);
							
							logger.info("TraacsMasterRequestMapper ET Flight for Booking : " + flight.getLpo() + " END");
							
						}
					}
					masterJson.setStr_authentication_key(authInfo);
					masterJson.setJson_master(masterDetails);
					masterJson.setJson_Sharing(sharingDetails);
					masterJson.setJson_Other(otherDetails);
					masterJson.setJson_Ticket(tickets);
				}
				
				if((traacInfo.getHotels() != null && !traacInfo.getHotels().isEmpty())){
					
					String hotelName = "";
					List<HotelStaticDataResponse> response =  getHotelStaticData(master.getHotelUniqueId(),"en_GB",msg,traacInfo.getHotels().get(0).getLpo());
					if(response != null && response.size() > 0){
						hotelName = response.get(0).getHotel().getBasicInfo().getName();
					}
					
					for(Hotel hotel : traacInfo.getHotels()){
						
						logger.info("TraacsMasterRequestMapper Hotel for Booking : " + hotel.getLpo() + " START");
						
						HotelJsonMaster hotelDetails = new HotelJsonMaster();
						
						hotelDetails.setSTR_VOUCHER_NO(hotel.getVoucherNo());
						hotelDetails.setSTR_ISSUE_DATE(StandAloneRequestBuilderUtil.dateFormatter(hotel.getIssueDt()));

						if((null != hotel.getSupplierCode() && hotel.getSupplierCode().equals("2400002")) && null != hotel.getSupplierConfirmationNo()){
							String[] rNum = hotel.getSupplierConfirmationNo().split(ETRCSConstants.HYPEN_CONSTANT);
							if(rNum.length == 2){
								String supplier = ETRCSConstants.HTL_BDS;
								hotelDetails.setSTR_SUPPLIER_CODE(suppllierIdsMap.get(supplier.toUpperCase()));
							}else{
								hotelDetails.setSTR_SUPPLIER_CODE(hotel.getSupplierCode());
							}
						}else{
							hotelDetails.setSTR_SUPPLIER_CODE(hotel.getSupplierCode());							
						}
						
						hotelDetails.setSTR_COUNTRY(hotel.getCountry());
						hotelDetails.setSTR_CITY(hotel.getCity());
						
						if(!StringUtils.isEmpty(hotelName)){
							logger.info("Hotel Name from HotelStaticData service :" + hotelName );
							hotelDetails.setSTR_HOTEL_NAME(hotelName);
						}else{
							logger.info("Empty Response from HotelStaticData service, hence we are setting Hotel Name from JSON :" + hotelName );
							hotelDetails.setSTR_HOTEL_NAME(hotel.getHotelName());
						}
						hotelDetails.setSTR_PRODUCT(hotel.getProduct());
						hotelDetails.setSTR_TYPE(hotel.getType());
						hotelDetails.setSTR_CHECK_IN_DATE(StandAloneRequestBuilderUtil.dateFormatter(hotel.getCheckInDate()));
						hotelDetails.setSTR_CHECK_OUT_DATE(StandAloneRequestBuilderUtil.dateFormatter(hotel.getCheckOutDate()));
						hotelDetails.setSTR_PNR_NO(hotel.getPnr());
						hotelDetails.setINT_NO_OF_NIGHTS(hotel.getNoOfNights());
						hotelDetails.setINT_NO_OF_ROOMS(hotel.getNoOfRooms());
						hotelDetails.setSTR_BOOKING_DETAILS(hotel.getBookingDetails());
						hotelDetails.setSTR_STAFF(hotel.getStaff());
						
						if(hotel.getMealPlan() != null){
							hotelDetails.setSTR_MEALS_PLAN(hotel.getMealPlan());
						}else{
							hotelDetails.setSTR_MEALS_PLAN("NONE");
						}
											
						if(hotel.getStaffEmail() != null){
							hotelDetails.setSTR_STAFF_EMAIL_ID(hotel.getStaffEmail());
						}else{
							hotelDetails.setSTR_STAFF_EMAIL_ID("website@flyin.com");
						}
						hotelDetails.setSTR_GUESTS(hotel.getGuests());
						hotelDetails.setSTR_ADDITIONAL_GUESTS(hotel.getAdditionalGuests());
						hotelDetails.setSTR_HOTEL_CONF_NUMBER(hotel.getHotelConfirmationNo());
						hotelDetails.setSTR_ORIGIN_COUNTRY(hotel.getOriginCountry());
						hotelDetails.setSTR_REMARKS(hotel.getRemarks());
						hotelDetails.setSTR_ROOM_TYPE(hotel.getRoomType());
						hotelDetails.setSTR_LPO_NO(master.getFlyinCode());
						hotelDetails.setSTR_CUSTOMER_REF_NO(hotel.getCustomerRefNo());
						
						if(hotel.getSupplierConfirmationNo()!= null && hotel.getSupplierConfirmationNo().contains(ETRCSConstants.COMMA_SEPARATOR)){
							String[] reservationNoList = hotel.getSupplierConfirmationNo().split(ETRCSConstants.COMMA_SEPARATOR);
							String reservationNo = reservationNoList[0];
							
							for(int i = 1; i < reservationNoList.length; i++){
								String s = reservationNoList[i];
								if( s.contains(ETRCSConstants.HYPEN_CONSTANT) ){
									reservationNo = reservationNo + ETRCSConstants.COMMA_SEPARATOR + s.substring(s.indexOf(ETRCSConstants.HYPEN_CONSTANT)+1 ); 
								}else{
									reservationNo = reservationNo + ETRCSConstants.COMMA_SEPARATOR + s;
								}
							}
							hotelDetails.setSTR_SUPPLIER_CONF_NUMBER( reservationNo );
						}else{
							hotelDetails.setSTR_SUPPLIER_CONF_NUMBER(hotel.getSupplierConfirmationNo());
						}
						
						if(null != hotel.getSupplierCode() && hotel.getSupplierCode().equals("C0012")){
							if(null != hotelDetails.getSTR_SUPPLIER_CONF_NUMBER() && hotelDetails.getSTR_SUPPLIER_CONF_NUMBER().contains("CHMM")){
								hotelDetails.setSTR_SUPPLIER_CODE(suppllierIdsMap.get("CT-DIRECT"));
							}else{
								hotelDetails.setSTR_SUPPLIER_CODE(suppllierIdsMap.get("CT-EXPEDIA"));
							}
						}
						
						hotelDetails.setSTR_PURCHASE_CUR_CODE(hotel.getPurchaseCurrCode());
						hotelDetails.setDBL_PURCHASE_CUR_TOTAL_FARE(hotel.getPurchaseCurrTotalFare());
						hotelDetails.setDBL_PURCHASE_CUR_INPUT_VAT(hotel.getPurchaseCurrInputVAT());
						hotelDetails.setDBL_PURCHASE_CUR_TOTAL_TAX(hotel.getPurchaseCurrTotTax());
						hotelDetails.setDBL_PURCHASE_CUR_SUPPLIER_AMOUNT(setScaleNConvertToDouble(hotel.getPurchaseCurrSupplierAmt()));
						
						Double negMkp = 0.0;
						if(null != master && (null != master.getAccountCode() && !master.getAccountCode().equalsIgnoreCase("C0006")) || (null == master.getAccountCode())){
							if(null != hotel.getSellingCurrServiceFee() && hotel.getSellingCurrServiceFee() < 0){
								negMkp = negMkp + hotel.getSellingCurrServiceFee();
							}else{
								hotelDetails.setDBL_SELLING_CUR_SERVICE_FEE(hotel.getSellingCurrServiceFee());
							}
							
							if(null != hotel.getSellingCurrExtraEarning() && hotel.getSellingCurrExtraEarning() < 0){
								negMkp = negMkp + hotel.getSellingCurrExtraEarning();
							}else{
								hotelDetails.setDBL_SELLING_CUR_EXTRA_EARNING(hotel.getSellingCurrExtraEarning());
							}
						}
						Double disc = hotel.getSellingCurrDiscount() != null ? hotel.getSellingCurrDiscount() : 0.0;
						hotelDetails.setDBL_SELLING_CUR_DISCOUNT(disc + Math.abs(negMkp));
						
						hotelDetails.setDBL_SELLING_CUR_PRICE(hotel.getSellingCurrPrice());
						hotelDetails.setDBL_SELLING_CUR_OUTPUT_VAT(hotel.getSellingCurrOutputVAT());
						
						List<RoomDetailsJson> json_details = new ArrayList<RoomDetailsJson>();
						if(hotel.getRoomDetails() != null){
						  for(RoomDetails aRoom: hotel.getRoomDetails()){
							  
							  RoomDetailsJson room = new RoomDetailsJson();
							  
							  room.setINT_ADULT(aRoom.getNoOfAdults());
							  room.setINT_CHILD(aRoom.getNoOfChilds());
							  room.setINT_NO_OF_ROOMS(1);
							  room.setINT_NO_OF_NIGHTS(hotel.getNoOfNights());
							  room.setSTR_GUESTS(aRoom.getGuests());
							  room.setSTR_ADDITIONAL_GUESTS(aRoom.getAdditionalGuests());
							  room.setSTR_ROOM_TYPE(aRoom.getRoomType());
							  
							  if(aRoom.getCheckInDt() != null && aRoom.getCheckInDt().contains("-")){
								  room.setSTR_CHECK_IN_DATE(StandAloneRequestBuilderUtil.dateFormatter(aRoom.getCheckInDt()));
							  }
							  else{
								  room.setSTR_CHECK_IN_DATE(aRoom.getCheckInDt());
							  }
							  
							  if(aRoom.getCheckOutDt() != null && aRoom.getCheckOutDt().contains("-")){
								  room.setSTR_CHECK_OUT_DATE(StandAloneRequestBuilderUtil.dateFormatter(aRoom.getCheckOutDt()));
							  }
							  else{
								  room.setSTR_CHECK_OUT_DATE(StandAloneRequestBuilderUtil.dateFormatter(aRoom.getCheckOutDt()));
							  }
							  
							  if(aRoom.getMealPlan() != null){
								  room.setSTR_MEALS_PLAN(aRoom.getMealPlan());
							  }else{
								  room.setSTR_MEALS_PLAN("ROOM ONLY");
							  }
							  
							  room.setDBL_PURCHASE_CUR_PRICE(aRoom.getPurchaseCurrPrice());
							  room.setDBL_PURCHASE_CUR_RATE_PER_NIGHT( aRoom.getPurchaseCurrRatePerNight() );
							  room.setDBL_PURCHASE_CUR_INPUT_VAT(aRoom.getPurchaseCurrInputVAT());
							  room.setDBL_PURCHASE_CUR_TAX_PER_NIGHT( aRoom.getPurchaseCurrTaxPerNight() );
							  room.setDBL_PURCHASE_CUR_SUPPLIER_AMOUNT(setScaleNConvertToDouble(aRoom.getPurchaseCurrSupplierAmt()));
							  
							  Double negMkpRoom = 0.0;
							  if(null != master && (null != master.getAccountCode() && !master.getAccountCode().equalsIgnoreCase("C0006")) || (null == master.getAccountCode())){
								  if(null != aRoom.getSellingCurrServiceFee() && aRoom.getSellingCurrServiceFee() < 0){
									  negMkpRoom = negMkpRoom + aRoom.getSellingCurrServiceFee();
								  }else{
									  room.setDBL_SELLING_CUR_SERVICE_FEE( aRoom.getSellingCurrServiceFee() );
								  }
								  
								  if(null != aRoom.getSellingCurrExtraEarning() && aRoom.getSellingCurrExtraEarning() < 0){
									  negMkpRoom = negMkpRoom + aRoom.getSellingCurrExtraEarning();
								  }else{
									  room.setDBL_SELLING_CUR_EXTRA_EARNING(aRoom.getSellingCurrExtraEarning());  
								  }
							  }
							  Double discRoom = aRoom.getSellingCurrDiscount() != null ? aRoom.getSellingCurrDiscount() : 0.0;
							  room.setDBL_SELLING_CUR_DISCOUNT(discRoom + Math.abs(negMkpRoom));
							  
							  room.setDBL_SELLING_CUR_PRICE(aRoom.getSellingCurrPrice());
							  room.setDBL_SELLING_CUR_OUTPUT_VAT(aRoom.getSellingCurrOutputVAT());
							  room.setDBL_SELLING_CUR_CC_CHARGES(aRoom.getSellingCurrCcCharges());
							  
							  json_details.add(room);
						  }
						  hotelDetails.setRoom_Details(json_details);
						}
						
						if ( null != master.getRefundDate() ) {
							hotelDetails.setSTR_REFUND_STAFF_EMAIL_ID(hotel.getRefundStaffEmailId());
							hotelDetails.setSTR_REFUND_STATUS(hotel.getRefundStatus());
							hotelDetails.setDBL_SELLING_CUR_CLIENT_NET(hotel.getSellingCurrClientNet());
							hotelDetails.setDBL_PURCHASE_CUR_SUPPLIER_CHARGE(hotel.getPurchaseCurrSupplierCharges());
							hotelDetails.setDBL_SELLING_CUR_AGENCY_CHARGE(hotel.getSellingCurrAgencyCharges());
							hotelDetails.setSTR_REFUND_DATE(hotel.getRefundDate());
							hotelDetails.setDBL_PURCHASE_CUR_SUPPLIER_NET(hotel.getPurchaseCurrSupplierNet());
						}
						
						hotelDetails.setDBL_SELLING_CUR_CC_CHARGES(hotel.getSellingCurrCcCharges());
						hotels.add(hotelDetails);			
						logger.info("TraacsMasterRequestMapper Hotel for Booking : " + hotel.getLpo() + " END");
					}
					masterJson.setStr_authentication_key(authInfo);
					masterJson.setJson_master(masterDetails);
					masterJson.setJson_Sharing(sharingDetails);
					masterJson.setJson_Other(otherDetails);
					masterJson.setJson_Hotel(hotels);
				}
			}

			//Flight Mapper
			if ((traacInfo.getFlights() != null && !traacInfo.getFlights().isEmpty()) && product.equalsIgnoreCase("flight")) {
				
				populateMasterData(master, masterDetails);
				populateSharingData(sharing, sharingDetails);
				populateOtherData(other, otherDetails, msg, master);
				
				// Check whether the tickets type is VMPD and split the the tickets
				splitTicketsBasedOnType(traacInfo);
				
				Double totSellPrice = 0.0;
				if (vmpdFlights.containsKey(master.getFlyinCode())) {
					for (List<Flight> vmpdTickets : vmpdFlights.get(master.getFlyinCode()).values()) {
						for (Flight aTicket : vmpdTickets) {
							totSellPrice = totSellPrice + (aTicket.getSellingCurrPrice() != null ? aTicket.getSellingCurrPrice() : 0.0);
						}
					}
				}
				
				if (etFlights.get(master.getFlyinCode()) != null && !etFlights.get(master.getFlyinCode()).isEmpty()) {
					for (Flight flight : etFlights.get(master.getFlyinCode())) {
						totSellPrice = totSellPrice + (flight.getSellingCurrPrice() != null ? flight.getSellingCurrPrice() : 0.0);
					}
				}

				// Build the JSON for VMPD tickets by applying logic
				if (vmpdFlights.containsKey(master.getFlyinCode())) {
					for (List<Flight> vmpdTickets : vmpdFlights.get(master.getFlyinCode()).values()) {
						vmpdTicketsFlag = vmpdTickets.size() > 0;
						if (vmpdTicketsFlag) {
							
							TicketJsonMaster tempVMPDTicketDetails = new TicketJsonMaster();
							Flight flight = null;
							
							for(Flight f : vmpdTickets){
								if((null != f.getPaxType() && f.getPaxType().equalsIgnoreCase("ADT"))){
									flight = f;
									break;
								}else if(null != f.getRefundStatus() && f.getRefundStatus().equalsIgnoreCase("REFUND")){
									flight = f;
									break;
								}
								
								if (flight == null) {
									if(null != f.getTicketType() && f.getTicketType().equalsIgnoreCase("VMPD")){
										flight = f;
										break;
									}
								}
							}
							
							logger.info("TraacsMasterRequestMapper VMPD Flight for Booking : " + flight.getLpo() + " START");
							
							computePricesForVMPD(vmpdTickets, tempVMPDTicketDetails, master, totSellPrice);
							populateVMPDTicketFares(tempVMPDTicketDetails, master);
							populateFlightCommonData(flight, tempVMPDTicketDetails); 
							String additionalPax = computeAdditionalPaxInfo(vmpdTickets, flight, master.getFlyinCode());
							if (additionalPax.trim().length() > 0) {
								tempVMPDTicketDetails.setSTR_ADDITIONAL_PAX(additionalPax);
							}else{
								tempVMPDTicketDetails.setSTR_ADDITIONAL_PAX(flight.getAdditionalPax());
							}
							
							tickets.add(tempVMPDTicketDetails);
							masterJson.setStr_authentication_key(authInfo);
							masterJson.setJson_Ticket(tickets);
						}
					}
				}
				
				// Build the JSONs for ET tickets
				if (etFlights.get(master.getFlyinCode()) != null && !etFlights.get(master.getFlyinCode()).isEmpty()) {
					for (Flight flight : etFlights.get(master.getFlyinCode())) {
						
						logger.info("TraacsMasterRequestMapper ET Flight for Booking : " + flight.getLpo() + " START");
						
						if(null != flight.getVatPercentage() && flight.getVatPercentage() > 0){
							vatPercentage = flight.getVatPercentage();
						}else if(null != master.getVatPercentage()){
							if(master.getVatPercentage().contains("_")){
								String[] vat = master.getVatPercentage().split("_");
								vatPercentage = Double.valueOf(vat[0]);
							}else{
								vatPercentage = Double.valueOf(master.getVatPercentage());									
							}
						}
						
						TicketJsonMaster ticketDetails = new TicketJsonMaster();
						
						populateFlightCommonData(flight, ticketDetails);
						Message.Builder messageBuilder = Message.builder(msg);
						
						if( null != ((Event) msg).getVariables().get(ETRCSConstants.REFUND_PAX_ID) ){
							//msg.setInvocationProperty( ETRCSConstants.REFUND_PAX_ID, ((Event) msg).getVariables().get(ETRCSConstants.REFUND_PAX_ID)+","+flight.getPaxId() );
							
							 ((Map<String, String>) messageBuilder).put( ETRCSConstants.REFUND_PAX_ID, ((Event) msg).getVariables().get(ETRCSConstants.REFUND_PAX_ID)+","+flight.getPaxId() );
						} else {
							//msg.setInvocationProperty( ETRCSConstants.REFUND_PAX_ID, flight.getPaxId() );
							
							 ((Map<String, String>) messageBuilder).put( ETRCSConstants.REFUND_PAX_ID, flight.getPaxId() );
						}
						
						//msg.setInvocationProperty( ETRCSConstants.REFUND_SEGMENT_ID, flight.getSegmentId());
						
						((Map<String, String>) messageBuilder).put( ETRCSConstants.REFUND_SEGMENT_ID, flight.getSegmentId());
						
						ticketDetails.setSTR_LPO_NO(flight.getLpo());
						ticketDetails.setSTR_PRODUCT(flight.getProduct());
						ticketDetails.setSTR_TICKET_ISSUE_DATE(StandAloneRequestBuilderUtil.dateFormatter(flight.getTicketIssueDt()));
						ticketDetails.setSTR_REPORTING_DATE(StandAloneRequestBuilderUtil.dateFormatter(flight.getReportingDt()));
						ticketDetails.setSTR_ADDITIONAL_PAX(flight.getAdditionalPax());
						ticketDetails.setSTR_AIRLINE_PNR(flight.getAirlinePnr());
						ticketDetails.setINT_NO_OF_PAX(flight.getNoOfPax());
						ticketDetails.setSTR_PURCHASE_CUR_CODE(flight.getPurchaseCurrCode());
						ticketDetails.setSTR_REGION_CODE(flight.getRegionCode());
						
						ticketDetails.setDBL_PURCHASE_CUR_PUBLISHED_FARE(flight.getPurchaseCurrPubFare());
						ticketDetails.setDBL_PURCHASE_CUR_TOTAL_MARKET_FARE(flight.getPurchaseCurrPubFare());
						ticketDetails.setDBL_PURCHASE_CUR_TOTAL_TAX(flight.getPurchaseCurrTotTax());
						ticketDetails.setDBL_PURCHASE_CUR_STD_COMMISION(flight.getPurchaseCurrStdComm());
						ticketDetails.setDBL_PURCHASE_CUR_SUPPLIER_AMOUNT(setScaleNConvertToDouble(flight.getPurchaseCurrSupplierAmt()));
						
						Double negMkp = 0.0;
						if(null != master && (null != master.getAccountCode() && !master.getAccountCode().equalsIgnoreCase("C0006")) || (null == master.getAccountCode())){
							if(null != flight.getSellingCurrServiceFee() && flight.getSellingCurrServiceFee() < 0){
								negMkp = negMkp + flight.getSellingCurrServiceFee();
							}else{
								ticketDetails.setDBL_SELLING_CUR_SERVICE_FEE(flight.getSellingCurrServiceFee());
							}
							
							if(null != flight.getSellingCurrExtraEarning() && flight.getSellingCurrExtraEarning() < 0){
								negMkp = negMkp + flight.getSellingCurrExtraEarning();
							}else{
								ticketDetails.setDBL_SELLING_CUR_EXTRA_EARNING(flight.getSellingCurrExtraEarning());
							}
						}
						
						Double disc = flight.getSellingCurrDiscount() != null ? flight.getSellingCurrDiscount() : 0.0;
						ticketDetails.setDBL_SELLING_CUR_DISCOUNT(disc + Math.abs(negMkp));
						
						ticketDetails.setDBL_PURCHASE_CUR_INPUT_VAT(flight.getPurchaseCurrInputVAT());
						
						if(null == master.getRefundDate()){
							Double sellingCurPrice = flight.getSellingCurrPrice() != null ? flight.getSellingCurrPrice() : 0.0;
							ticketDetails.setDBL_SELLING_CUR_PRICE(sellingCurPrice);
							Double cd = (sellingCurPrice * couponDisc) / totSellPrice;
							ticketDetails.setDBL_SELLING_CUR_OUTPUT_VAT(calculateVAT((sellingCurPrice - cd), vatPercentage));
						}
						
						if(null != master.getRefundDate()){
							ticketDetails.setSTR_REFUND_DATE(StandAloneRequestBuilderUtil.dateFormatter(flight.getRefundDate()));
							ticketDetails.setSTR_REFUND_STAFF_EMAIL_ID(flight.getRefundStaffEmailId());
							ticketDetails.setSTR_REFUND_STATUS(flight.getRefundStatus());
							ticketDetails.setDBL_PURCHASE_CUR_AIRLINE_CHARGE(flight.getPurchaseCurrAirlineCharges()); 
							ticketDetails.setDBL_PURCHASE_CUR_SUPPLIER_NET(flight.getPurchaseCurrSupplierNet());
							ticketDetails.setDBL_SELLING_CUR_AGENCY_CHARGE(flight.getSellingCurrAgencyCharges());
							ticketDetails.setDBL_SELLING_CUR_CLIENT_NET(flight.getSellingCurrClientNet());
							ticketDetails.setDBL_SELLING_CUR_OUTPUT_VAT(flight.getSellingCurrOutputVAT());
						}
						
						ticketDetails.setDBL_SELLING_CUR_CREDIT_CARD_CHARGES(flight.getSellingCurrCcCharges());
						tickets.add(ticketDetails);
						logger.info("TraacsMasterRequestMapper ET Flight for Booking : " + flight.getLpo() + " END");
					}
				}
				masterJson.setStr_authentication_key(authInfo);
				masterJson.setJson_master(masterDetails);
				masterJson.setJson_Sharing(sharingDetails);
				masterJson.setJson_Other(otherDetails);
				masterJson.setJson_Ticket(tickets);
			}
			
			//Hotel Mapper
			if((traacInfo.getHotels() != null && !traacInfo.getHotels().isEmpty()) && product.equalsIgnoreCase("hotel")){
				
				populateMasterData(master, masterDetails);
				populateSharingData(sharing, sharingDetails);
				populateOtherData(other, otherDetails, msg, master);
				
				String hotelName = "";
				List<HotelStaticDataResponse> response =  getHotelStaticData(master.getHotelUniqueId(),"en_GB",msg,traacInfo.getHotels().get(0).getLpo());
				if(response != null && response.size() > 0 && null != response.get(0).getHotel() && null != response.get(0).getHotel().getBasicInfo()){
					hotelName = response.get(0).getHotel().getBasicInfo().getName();
				}
				
				for(Hotel hotel : traacInfo.getHotels()){
					
					logger.info("TraacsMasterRequestMapper Hotel for Booking : " + hotel.getLpo() + " START");
					HotelJsonMaster hotelDetails = new HotelJsonMaster();
					hotelDetails.setSTR_VOUCHER_NO(hotel.getVoucherNo());
					hotelDetails.setSTR_ISSUE_DATE(StandAloneRequestBuilderUtil.dateFormatter(hotel.getIssueDt()));

					if((null != hotel.getSupplierCode() && hotel.getSupplierCode().equals("2400002")) && null != hotel.getSupplierConfirmationNo()){
						String[] rNum = hotel.getSupplierConfirmationNo().split(ETRCSConstants.HYPEN_CONSTANT);
						if(rNum.length == 2){
							String supplier = ETRCSConstants.HTL_BDS;
							hotelDetails.setSTR_SUPPLIER_CODE(suppllierIdsMap.get(supplier.toUpperCase()));
						}else{
							hotelDetails.setSTR_SUPPLIER_CODE(hotel.getSupplierCode());
						}
					}else{
						hotelDetails.setSTR_SUPPLIER_CODE(hotel.getSupplierCode());							
					}
					
					hotelDetails.setSTR_COUNTRY(hotel.getCountry());
					hotelDetails.setSTR_CITY(hotel.getCity());
					
					if(!StringUtils.isEmpty(hotelName)){
						logger.info("Hotel Name from HotelStaticData service :" + hotelName );
						hotelDetails.setSTR_HOTEL_NAME(hotelName);
					}else{
						logger.info("Empty Response from HotelStaticData service, hence we are setting Hotel Name from JSON :" + hotelName );
						hotelDetails.setSTR_HOTEL_NAME(hotel.getHotelName());
					}
					hotelDetails.setSTR_PRODUCT(hotel.getProduct());
					hotelDetails.setSTR_TYPE(hotel.getType());
					hotelDetails.setSTR_CHECK_IN_DATE(StandAloneRequestBuilderUtil.dateFormatter(hotel.getCheckInDate()));
					hotelDetails.setSTR_CHECK_OUT_DATE(StandAloneRequestBuilderUtil.dateFormatter(hotel.getCheckOutDate()));
					hotelDetails.setSTR_PNR_NO(hotel.getPnr());
					hotelDetails.setINT_NO_OF_NIGHTS(hotel.getNoOfNights());
					hotelDetails.setINT_NO_OF_ROOMS(hotel.getNoOfRooms());
					hotelDetails.setSTR_BOOKING_DETAILS(hotel.getBookingDetails());
					hotelDetails.setSTR_STAFF(hotel.getStaff());
					
					if(hotel.getMealPlan() != null){
						hotelDetails.setSTR_MEALS_PLAN(hotel.getMealPlan());
					}else{
						hotelDetails.setSTR_MEALS_PLAN("NONE");
					}
										
					if(hotel.getStaffEmail() != null){
						hotelDetails.setSTR_STAFF_EMAIL_ID(hotel.getStaffEmail());
					}else{
						hotelDetails.setSTR_STAFF_EMAIL_ID("website@flyin.com");
					}
					hotelDetails.setSTR_GUESTS(hotel.getGuests());
					hotelDetails.setSTR_ADDITIONAL_GUESTS(hotel.getAdditionalGuests());
					hotelDetails.setSTR_HOTEL_CONF_NUMBER(hotel.getHotelConfirmationNo());
					hotelDetails.setSTR_ORIGIN_COUNTRY(hotel.getOriginCountry());
					hotelDetails.setSTR_REMARKS(hotel.getRemarks());
					hotelDetails.setSTR_ROOM_TYPE(hotel.getRoomType());
					hotelDetails.setSTR_LPO_NO(master.getFlyinCode());
					hotelDetails.setSTR_CUSTOMER_REF_NO(hotel.getCustomerRefNo());
					
					if(hotel.getSupplierConfirmationNo()!= null && hotel.getSupplierConfirmationNo().contains(ETRCSConstants.COMMA_SEPARATOR)){
						String[] reservationNoList = hotel.getSupplierConfirmationNo().split(ETRCSConstants.COMMA_SEPARATOR);
						String reservationNo = reservationNoList[0];
						
						for(int i = 1; i < reservationNoList.length; i++){
							String s = reservationNoList[i];
							if( s.contains(ETRCSConstants.HYPEN_CONSTANT) ){
								reservationNo = reservationNo + ETRCSConstants.COMMA_SEPARATOR + s.substring(s.indexOf(ETRCSConstants.HYPEN_CONSTANT)+1 ); 
							}else{
								reservationNo = reservationNo + ETRCSConstants.COMMA_SEPARATOR + s;
							}
						}
						hotelDetails.setSTR_SUPPLIER_CONF_NUMBER( reservationNo );
					}else{
						hotelDetails.setSTR_SUPPLIER_CONF_NUMBER(hotel.getSupplierConfirmationNo());
					}
					
					if(null != hotel.getSupplierCode() && hotel.getSupplierCode().equals("C0012")){
						if(null != hotelDetails.getSTR_SUPPLIER_CONF_NUMBER() && hotelDetails.getSTR_SUPPLIER_CONF_NUMBER().contains("CHMM")){
							hotelDetails.setSTR_SUPPLIER_CODE(suppllierIdsMap.get("CT-DIRECT"));
						}else{
							hotelDetails.setSTR_SUPPLIER_CODE(suppllierIdsMap.get("CT-EXPEDIA"));
						}
					}
					
					hotelDetails.setSTR_PURCHASE_CUR_CODE(hotel.getPurchaseCurrCode());
					hotelDetails.setDBL_PURCHASE_CUR_TOTAL_FARE(hotel.getPurchaseCurrTotalFare());
					hotelDetails.setDBL_PURCHASE_CUR_INPUT_VAT(hotel.getPurchaseCurrInputVAT());
					hotelDetails.setDBL_PURCHASE_CUR_TOTAL_TAX(hotel.getPurchaseCurrTotTax());
					hotelDetails.setDBL_PURCHASE_CUR_SUPPLIER_AMOUNT(setScaleNConvertToDouble(hotel.getPurchaseCurrSupplierAmt()));
					
					Double negMkp = 0.0;
					if(null != master && (null != master.getAccountCode() && !master.getAccountCode().equalsIgnoreCase("C0006")) || (null == master.getAccountCode())){
						if(null != hotel.getSellingCurrServiceFee() && hotel.getSellingCurrServiceFee() < 0){
							negMkp = negMkp + hotel.getSellingCurrServiceFee();
						}else{
							hotelDetails.setDBL_SELLING_CUR_SERVICE_FEE(hotel.getSellingCurrServiceFee());
						}
						
						if(null != hotel.getSellingCurrExtraEarning() && hotel.getSellingCurrExtraEarning() < 0){
							negMkp = negMkp + hotel.getSellingCurrExtraEarning();
						}else{
							hotelDetails.setDBL_SELLING_CUR_EXTRA_EARNING(hotel.getSellingCurrExtraEarning());
						}
					}
					Double disc = hotel.getSellingCurrDiscount() != null ? hotel.getSellingCurrDiscount() : 0.0;
					hotelDetails.setDBL_SELLING_CUR_DISCOUNT(disc + Math.abs(negMkp));
					
					hotelDetails.setDBL_SELLING_CUR_PRICE(hotel.getSellingCurrPrice());
					hotelDetails.setDBL_SELLING_CUR_OUTPUT_VAT(hotel.getSellingCurrOutputVAT());
					
					List<RoomDetailsJson> json_details = new ArrayList<RoomDetailsJson>();
					if(hotel.getRoomDetails() != null){
					  for(RoomDetails aRoom: hotel.getRoomDetails()){
						  
						  RoomDetailsJson room = new RoomDetailsJson();
						  
						  room.setINT_ADULT(aRoom.getNoOfAdults());
						  room.setINT_CHILD(aRoom.getNoOfChilds());
						  room.setINT_NO_OF_ROOMS(1);
						  room.setINT_NO_OF_NIGHTS(hotel.getNoOfNights());
						  room.setSTR_GUESTS(aRoom.getGuests());
						  room.setSTR_ADDITIONAL_GUESTS(aRoom.getAdditionalGuests());
						  room.setSTR_ROOM_TYPE(aRoom.getRoomType());
						  
						  if(aRoom.getCheckInDt() != null && aRoom.getCheckInDt().contains("-")){
							  room.setSTR_CHECK_IN_DATE(StandAloneRequestBuilderUtil.dateFormatter(aRoom.getCheckInDt()));
						  }
						  else{
							  room.setSTR_CHECK_IN_DATE(aRoom.getCheckInDt());
						  }
						  
						  if(aRoom.getCheckOutDt() != null && aRoom.getCheckOutDt().contains("-")){
							  room.setSTR_CHECK_OUT_DATE(StandAloneRequestBuilderUtil.dateFormatter(aRoom.getCheckOutDt()));
						  }
						  else{
							  room.setSTR_CHECK_OUT_DATE(StandAloneRequestBuilderUtil.dateFormatter(aRoom.getCheckOutDt()));
						  }
						  
						  if(aRoom.getMealPlan() != null){
							  room.setSTR_MEALS_PLAN(aRoom.getMealPlan());
						  }else{
							  room.setSTR_MEALS_PLAN("ROOM ONLY");
						  }
						  
						  room.setDBL_PURCHASE_CUR_PRICE(aRoom.getPurchaseCurrPrice());
						  room.setDBL_PURCHASE_CUR_RATE_PER_NIGHT( aRoom.getPurchaseCurrRatePerNight() );
						  room.setDBL_PURCHASE_CUR_INPUT_VAT(aRoom.getPurchaseCurrInputVAT());
						  room.setDBL_PURCHASE_CUR_TAX_PER_NIGHT( aRoom.getPurchaseCurrTaxPerNight() );
						  room.setDBL_PURCHASE_CUR_SUPPLIER_AMOUNT(setScaleNConvertToDouble(aRoom.getPurchaseCurrSupplierAmt()));
						  
						  Double negMkpRoom = 0.0;
						  if(null != master && (null != master.getAccountCode() && !master.getAccountCode().equalsIgnoreCase("C0006")) || (null == master.getAccountCode())){
							  if(null != aRoom.getSellingCurrServiceFee() && aRoom.getSellingCurrServiceFee() < 0){
								  negMkpRoom = negMkpRoom + aRoom.getSellingCurrServiceFee();
							  }else{
								  room.setDBL_SELLING_CUR_SERVICE_FEE( aRoom.getSellingCurrServiceFee() );
							  }
							  
							  if(null != aRoom.getSellingCurrExtraEarning() && aRoom.getSellingCurrExtraEarning() < 0){
								  negMkpRoom = negMkpRoom + aRoom.getSellingCurrExtraEarning();
							  }else{
								  room.setDBL_SELLING_CUR_EXTRA_EARNING(aRoom.getSellingCurrExtraEarning());  
							  }
						  }
						  Double discRoom = aRoom.getSellingCurrDiscount() != null ? aRoom.getSellingCurrDiscount() : 0.0;
						  room.setDBL_SELLING_CUR_DISCOUNT(discRoom + Math.abs(negMkpRoom));
						  
						  room.setDBL_SELLING_CUR_PRICE(aRoom.getSellingCurrPrice());
						  room.setDBL_SELLING_CUR_OUTPUT_VAT(aRoom.getSellingCurrOutputVAT());
						  room.setDBL_SELLING_CUR_CC_CHARGES(aRoom.getSellingCurrCcCharges());
						  
						  json_details.add(room);
					  }
					  hotelDetails.setRoom_Details(json_details);
					}
					
					if ( null != master.getRefundDate() ) {
						hotelDetails.setSTR_REFUND_STAFF_EMAIL_ID(hotel.getRefundStaffEmailId());
						hotelDetails.setSTR_REFUND_STATUS(hotel.getRefundStatus());
						hotelDetails.setDBL_SELLING_CUR_CLIENT_NET(hotel.getSellingCurrClientNet());
						hotelDetails.setDBL_PURCHASE_CUR_SUPPLIER_CHARGE(hotel.getPurchaseCurrSupplierCharges());
						hotelDetails.setDBL_SELLING_CUR_AGENCY_CHARGE(hotel.getSellingCurrAgencyCharges());
						hotelDetails.setSTR_REFUND_DATE(hotel.getRefundDate());
						hotelDetails.setDBL_PURCHASE_CUR_SUPPLIER_NET(hotel.getPurchaseCurrSupplierNet());
					}
					
					hotelDetails.setDBL_SELLING_CUR_CC_CHARGES(hotel.getSellingCurrCcCharges());
					hotels.add(hotelDetails);			
					logger.info("TraacsMasterRequestMapper Hotel for Booking : " + hotel.getLpo() + " END");
				}
				masterJson.setStr_authentication_key(authInfo);
				masterJson.setJson_master(masterDetails);
				masterJson.setJson_Sharing(sharingDetails);
				masterJson.setJson_Other(otherDetails);
				masterJson.setJson_Hotel(hotels);
			}
			
			//Car / Transfer Mapper
			if ((traacInfo.getCars() != null && !traacInfo.getCars().isEmpty()) && product.equalsIgnoreCase("car")) {
				
				populateMasterData(master, masterDetails);
				populateSharingData(sharing, sharingDetails);
				populateOtherData(other, otherDetails, msg, master);
				
				String companyId = master.getCompanyId();
				for(Car car : traacInfo.getCars()){
					CarJsonMaster carDetails = new CarJsonMaster();
					
					carDetails.setSTR_VOUCHER_NO(car.getVoucherNo());
					if(car.getIssueDt() != null && car.getIssueDt().contains("-")){
						carDetails.setSTR_ISSUE_DATE(StandAloneRequestBuilderUtil.dateFormatter(car.getIssueDt()));
					}else{
						carDetails.setSTR_ISSUE_DATE(StandAloneRequestBuilderUtil.dateFormatter(car.getIssueDt()));
					}
					
					carDetails.setSTR_SUPPLIER_CODE(car.getSupplierCode());
					carDetails.setSTR_RENTAL_COMPANY_CODE(car.getRentalCompanyCode());
					carDetails.setSTR_RENTING_STATION(car.getRentalStation());
					carDetails.setSTR_CAR_CONF_NO(car.getCarConfNo());
					carDetails.setSTR_DROP_STATION(car.getDropStation());					
					carDetails.setSTR_PRODUCT(car.getProduct());
					carDetails.setSTR_TYPE(car.getType());
					carDetails.setSTR_FROM_DATE(StandAloneRequestBuilderUtil.dateFormatter(car.getFromDate()));
					carDetails.setSTR_TO_DATE(StandAloneRequestBuilderUtil.dateFormatter(car.getToDate()));
					carDetails.setSTR_PNR_NO(car.getPnr());
					carDetails.setINT_NO_OF_CARS(car.getNoOfCars());
					carDetails.setINT_NO_OF_DAYS(car.getNoOfDays());
					carDetails.setSTR_BOOKING_DETAILS(car.getBookingDetails());
					carDetails.setDBL_PURCHASE_CUR_TOTAL_FARE(car.getPurchaseCurrTotalFare());
					carDetails.setDBL_PURCHASE_CUR_TOTAL_MARKET_FARE(car.getPurchaseCurrTotMarketFare());
					
					Double negMkp = 0.0;
					if(null != master && (null != master.getAccountCode() && !master.getAccountCode().equalsIgnoreCase("C0006")) || (null == master.getAccountCode())){
						if(null != car.getSellingCurrServiceFee() && car.getSellingCurrServiceFee() < 0){
							negMkp = negMkp + car.getSellingCurrServiceFee();
						}else{
							carDetails.setDBL_SELLING_CUR_SERVICE_FEE(car.getSellingCurrServiceFee());
						}
						
						if(null != car.getSellingCurrExtraEarning() && car.getSellingCurrExtraEarning() < 0){
							negMkp = negMkp + car.getSellingCurrExtraEarning();
						}else{
							carDetails.setDBL_SELLING_CUR_EXTRA_EARNING(car.getSellingCurrExtraEarning());
						}
					}
					
					Double disc = car.getSellingCurrDiscount() != null ? car.getSellingCurrDiscount() : 0.0;
					carDetails.setDBL_SELLING_CUR_DISCOUNT(setScaleNConvertToDouble(disc + Math.abs(negMkp)));
					
					carDetails.setDBL_PURCHASE_CUR_INPUT_VAT(car.getPurchaseCurrInputVAT());					
					carDetails.setDBL_SELLING_CUR_PRICE(car.getSellingCurrPrice());
					carDetails.setSTR_CATEGORY(car.getCategory());
					carDetails.setSTR_VEHICLE(car.getVehicle());
					carDetails.setSTR_VEHICLE(car.getVehicle());
					carDetails.setSTR_MODEL(car.getModel());
					carDetails.setSTR_TRANSMISSION(car.getTransmission());
					carDetails.setDBL_PURCHASE_CUR_TOTAL_TAX(car.getPurchaseCurrTotTax());
					carDetails.setDBL_PURCHASE_CUR_STD_COMMISION(car.getPurchaseCurrStdComm());					
					carDetails.setSTR_STAFF(car.getStaff());
										
					if(car.getStaffEmail() != null){
						carDetails.setSTR_STAFF_EMAIL_ID(car.getStaffEmail());
					}else{
						carDetails.setSTR_STAFF_EMAIL_ID("website@flyin.com");
					}
					carDetails.setSTR_PAX_NAME(car.getPaxName());
					carDetails.setSTR_ADDITIONAL_PAX(car.getAdditionalPax());
					carDetails.setSTR_REMARKS(car.getRemarks());
					if(car.getSupplierConfNo()!= null && car.getSupplierConfNo().contains(ETRCSConstants.COMMA_SEPARATOR)){
						String[] reservationNoList =car.getSupplierConfNo().split(ETRCSConstants.COMMA_SEPARATOR);
						String reservationNo =reservationNoList[0];
						
						for(int i = 1; i < reservationNoList.length; i++){
							String s = reservationNoList[i];
							if( s.contains(ETRCSConstants.HYPEN_CONSTANT) ){
								reservationNo = reservationNo + ETRCSConstants.COMMA_SEPARATOR + s.substring(s.indexOf(ETRCSConstants.HYPEN_CONSTANT)+1 ); 
							}else{
								reservationNo = reservationNo + ETRCSConstants.COMMA_SEPARATOR + s;
							}
						}
						carDetails.setSTR_SUPPLIER_CONF_NUMBER( reservationNo );
					}else{
						carDetails.setSTR_SUPPLIER_CONF_NUMBER(car.getSupplierConfNo());
					}
					carDetails.setSTR_LPO_NO(master.getFlyinCode());
					carDetails.setSTR_CUSTOMER_REF_NO(car.getCustomerRefNo());
					carDetails.setSTR_PURCHASE_CUR_CODE( car.getPurchaseCurrCode() );
					carDetails.setDBL_SELLING_CUR_CLIENT_NET( car.getSellingCurrClientNet() );
					carDetails.setDBL_SELLING_CUR_AGENCY_CHARGE( car.getSellingCurrAgencyCharges() );
					carDetails.setDBL_PURCHASE_CUR_INPUT_VAT( car.getPurchaseCurrInputVAT() );
					carDetails.setDBL_SELLING_CUR_OUTPUT_VAT( car.getSellingCurrOutputVAT() );
					
					List<CarDetailsJson> json_details = new ArrayList<CarDetailsJson>();
					if(car.getCarDetails() != null){
					  for(CarDetails aCar: car.getCarDetails()){
						  CarDetailsJson carDet = new CarDetailsJson();
						  carDet.setINT_NO_OF_CARS(aCar.getNoOfCars());
						  carDet.setINT_NO_OF_DAYS(aCar.getNoOfDays());
						  
						  if(aCar.getFromDate() != null && aCar.getFromDate().contains("-")){
							  carDet.setSTR_FROM_DATE(StandAloneRequestBuilderUtil.dateFormatter(aCar.getFromDate()));
						  }
						  else{
							  carDet.setSTR_FROM_DATE(StandAloneRequestBuilderUtil.dateFormatter(aCar.getFromDate()));
						  }
						  
						  if(aCar.getToDate() != null && aCar.getToDate().contains("-")){
							  carDet.setSTR_TO_DATE(StandAloneRequestBuilderUtil.dateFormatter(aCar.getToDate()));
						  }
						  else{
							  carDet.setSTR_TO_DATE(StandAloneRequestBuilderUtil.dateFormatter(aCar.getToDate()));
						  }
						  
						  carDet.setSTR_CATEGORY(aCar.getCategory());
						  carDet.setSTR_VEHICLE(aCar.getVehicle());
						  carDet.setSTR_MODEL(aCar.getModel());
						  carDet.setSTR_TRANSMISSION(aCar.getTransmission());
						  carDet.setSTR_PAX_NAME(aCar.getPaxName());
						  carDet.setSTR_RENTING_PERIOD(aCar.getRentingPeriod());
						  carDet.setDBL_PURCHASE_CUR_RATE_PER_DAY(aCar.getPurchaseCurrRatePerDay());
						  carDet.setDBL_PURCHASE_CUR_INPUT_VAT( aCar.getPurchaseCurrInputVAT() );
						  carDet.setDBL_PURCHASE_CUR_SUPPLIER_AMOUNT(setScaleNConvertToDouble(aCar.getPurchaseCurrSupplierAmt()));
							  
						  Double serviceFee = ( aCar.getSellingCurrServiceFee() != null ) ? aCar.getSellingCurrServiceFee() : new Double( 0.0 );
						  Double taxPerDay = ( aCar.getSellingCurrTaxPerDay() != null ) ? aCar.getSellingCurrTaxPerDay() : new Double( 0.0 );
						  Double totalFare = ( aCar.getSellingCurrPrice() != null ) ? aCar.getSellingCurrPrice() : new Double( 0.0 );
						  Double ratePerDay = ( aCar.getSellingCurrRatePerDay() != null ) ? aCar.getSellingCurrRatePerDay() : new Double( 0.0 );
						  
						  carDet.setDBL_SELLING_CUR_SUPPLIER_AMOUNT( ratePerDay );
						  
						  Double negMkpDet = 0.0;
						  if(null != master && (null != master.getAccountCode() && !master.getAccountCode().equalsIgnoreCase("C0006")) || (null == master.getAccountCode())){
							  if(null != serviceFee && serviceFee < 0){
								  negMkpDet = negMkpDet + serviceFee;
							  }else{
								  carDet.setDBL_SELLING_CUR_SERVICE_FEE( serviceFee );
							  }
							  
							  if(null != aCar.getSellingCurrExtraEarning() && aCar.getSellingCurrExtraEarning() < 0){
								  negMkpDet = negMkpDet + aCar.getSellingCurrExtraEarning();
							  }else{
								  carDet.setDBL_SELLING_CUR_EXTRA_EARNING( aCar.getSellingCurrExtraEarning() );
							  }
						  }
						  
						  Double discDet = aCar.getSellingCurrDiscount() != null ? aCar.getSellingCurrDiscount() : 0.0;
						  carDet.setDBL_SELLING_CUR_DISCOUNT(setScaleNConvertToDouble(discDet + Math.abs(negMkpDet)));
						  
						  carDet.setDBL_SELLING_CUR_PRICE( totalFare );
						  carDet.setDBL_PURCHASE_CUR_TAX_PER_DAY( aCar.getPurchaseCurrTaxPerDay() );
						  carDet.setDBL_SELLING_CUR_RATE_PER_DAY( ratePerDay );
						  carDet.setDBL_SELLING_CUR_TAX_PER_DAY( taxPerDay );
						  
						  carDet.setDBL_SELLING_CUR_CC_CHARGES(aCar.getSellingCurrCcCharges());
						
						  carDetails.setSTR_PURCHASE_CUR_CODE( car.getPurchaseCurrCode() );
						  
						  json_details.add(carDet);
					  }
					  carDetails.setCar_Details(json_details);
					}
					
					if ( null != master.getRefundDate() ) {
						carDetails.setSTR_REFUND_STAFF_EMAIL_ID(car.getRefundEmailId());
						carDetails.setSTR_REFUND_DATE(car.getRefundDate());
						carDetails.setSTR_REFUND_STATUS(car.getRefundStatus());
						carDetails.setDBL_PURCHASE_CUR_SUPPLIER_NET(car.getPurchaseCurrSupplierNet());
					} else {
						carDetails.setDBL_PURCHASE_CUR_SUPPLIER_AMOUNT(setScaleNConvertToDouble(car.getPurchaseCurrSupplierAmt()));
					}
					
					carDetails.setDBL_SELLING_CUR_CC_CHARGES(car.getSellingCurrCcCharges());
				
					cars.add(carDetails);				
				}
				masterJson.setStr_authentication_key(authInfo);
				masterJson.setJson_master(masterDetails);
				masterJson.setJson_Sharing(sharingDetails);
				masterJson.setJson_Other(otherDetails);
				masterJson.setJson_Car(cars);
			}
			
			//Other
			if ((other != null && !other.isEmpty()) && product.equalsIgnoreCase("other")) {
				populateMasterData(master, masterDetails);
				populateOtherData(other, otherDetails, msg, master);
				masterJson.setJson_master(masterDetails);
				masterJson.setJson_Other(otherDetails);
				masterJson.setStr_authentication_key(authInfo);
			}else if(product.equalsIgnoreCase("package")){
				masterJson.setJson_master(masterDetails);
				masterJson.setJson_Sharing(sharingDetails);
				masterJson.setJson_Other(otherDetails);
				masterJson.setStr_authentication_key(authInfo);
			}else if(product.equalsIgnoreCase("activity")){
				masterJson.setJson_master(masterDetails);
				masterJson.setJson_Sharing(sharingDetails);
				masterJson.setJson_Other(otherDetails);
				masterJson.setStr_authentication_key(authInfo);
			}
			
		}

		reqCount = null;
		//msg.removeProperty(ETRCSConstants.VAR_REQUEST_COUNT,PropertyScope.INVOCATION);
		
		( (Map<String,TypedValue<?>>) msg).remove(ETRCSConstants.VAR_REQUEST_COUNT);
		
		logger.info("TraacsMasterRequestMapper for Booking : " + lpo + " END");
		return masterJson;
	}

	/**
	 * The purpose of this method is to compute different fare/prices data if
	 * all the tickets are for VMPD. This method uses the input flight details
	 * list and stores the computed prices/fare information to input
	 * TicketJsonMaster.
	 * 
	 * @param allFlights
	 *            - List having input Flight objects
	 * @param vmpdTicketDetails
	 *            - the JSON structure equivalent which will be updated for
	 *            transformed prices
	 */
	private void computePricesForVMPD(Collection<Flight> allFlights, TicketJsonMaster vmpdTicketDetails, SalesMaster master, Double totSellPrice) {

		// Declare all price/fare/tax variables
		Double purchaseCurPublishedFare, purchaseCurTotalMarketFare, purchaseCurTotalTax, purchaseCurStdCommision,
				sellingCurServiceFee, sellingCurPrice, purchaseCurSupplierAmount, sellingCurDiscount, sellingCurExtraEarning, 
				totalPrice, baseFare, purchaseCurTotalInputVat, purchaseCurAirlinePenalty, sellingCurAgencyCharges = null;

		// Init them to a value of 0.0d
		purchaseCurPublishedFare = purchaseCurTotalMarketFare = purchaseCurTotalTax = purchaseCurStdCommision = sellingCurServiceFee = 
				sellingCurPrice = purchaseCurSupplierAmount = sellingCurDiscount = sellingCurExtraEarning = totalPrice = baseFare = 
				purchaseCurTotalInputVat = purchaseCurAirlinePenalty = sellingCurAgencyCharges = new Double(0.0);

		Double couponDisc = 0.0;
		if(null != master && null != master.getCouponDiscount()){
			couponDisc = master.getCouponDiscount();
		}
		
		Boolean isRefund = Boolean.FALSE;
		Double outVatPercent = 0.0;
		for (Flight aTicket : allFlights) {

			if(null != aTicket.getVatPercentage() && aTicket.getVatPercentage() > 0){
				outVatPercent = aTicket.getVatPercentage();
			}else if(null != master.getVatPercentage()){
				if(master.getVatPercentage().contains("_")){
					String[] vat = master.getVatPercentage().split("_");
					outVatPercent = Double.valueOf(vat[0]);
				}else{
					outVatPercent = Double.valueOf(master.getVatPercentage());									
				}
			}
			
			// sum up all fares/taxes/prices from all the input Flight elements
			Double tax = new Double(0.0);
			Double marketFare = new Double(0.0);
			if (aTicket.getPurchaseCurrPubFare() != null) {
				purchaseCurPublishedFare = purchaseCurPublishedFare + aTicket.getPurchaseCurrPubFare();
			}
			if (aTicket.getPurchaseCurrTotMarketFare() != null) {
				marketFare = aTicket.getPurchaseCurrTotMarketFare();
				purchaseCurTotalMarketFare = purchaseCurTotalMarketFare + marketFare;
			}
			if (aTicket.getPurchaseCurrInputVAT() != null) {
				purchaseCurTotalInputVat = purchaseCurTotalInputVat + aTicket.getPurchaseCurrInputVAT();
			}
			if (aTicket.getPurchaseCurrTotTax() != null) {
				tax = aTicket.getPurchaseCurrTotTax();
				purchaseCurTotalTax = purchaseCurTotalTax + tax;
			}
			if (aTicket.getPurchaseCurrStdComm() != null) {
				purchaseCurStdCommision = purchaseCurStdCommision + aTicket.getPurchaseCurrStdComm();
			}
			if (aTicket.getSellingCurrServiceFee() != null) {
				sellingCurServiceFee = sellingCurServiceFee + aTicket.getSellingCurrServiceFee();
			}
			if (aTicket.getPurchaseCurrAirlineCharges() != null) {
				purchaseCurAirlinePenalty = purchaseCurAirlinePenalty + aTicket.getPurchaseCurrAirlineCharges();
			}
			if (aTicket.getSellingCurrAgencyCharges() != null) {
				sellingCurAgencyCharges = sellingCurAgencyCharges + aTicket.getSellingCurrAgencyCharges();
			}
			if (aTicket.getSellingCurrDiscount() != null) {
				sellingCurDiscount = sellingCurDiscount + aTicket.getSellingCurrDiscount();
			}
			if (aTicket.getSellingCurrExtraEarning() != null) {
				sellingCurExtraEarning = sellingCurExtraEarning + aTicket.getSellingCurrExtraEarning();
			}
			if (aTicket.getSellingCurrPrice() != null) {
				totalPrice = aTicket.getSellingCurrPrice();
				sellingCurPrice = sellingCurPrice + totalPrice;
			}else if (aTicket.getSellingCurrClientNet() != null) {
				totalPrice = aTicket.getSellingCurrClientNet();
				sellingCurPrice = sellingCurPrice + totalPrice;
			}
			if (aTicket.getSellingCurrPubFare() != null) {
				baseFare = baseFare + aTicket.getSellingCurrPubFare();
			}
			purchaseCurSupplierAmount = purchaseCurSupplierAmount + marketFare + tax - purchaseCurStdCommision - purchaseCurAirlinePenalty;
			
			if (null != aTicket.getRefundStatus()){
				isRefund = Boolean.TRUE;
			}
		}
		vmpdTicketDetails.setDBL_PURCHASE_CUR_PUBLISHED_FARE(setScaleNConvertToDouble(purchaseCurPublishedFare));
		vmpdTicketDetails.setDBL_PURCHASE_CUR_TOTAL_MARKET_FARE(setScaleNConvertToDouble(purchaseCurTotalMarketFare));
		vmpdTicketDetails.setDBL_PURCHASE_CUR_TOTAL_TAX(setScaleNConvertToDouble((purchaseCurTotalTax)));
		vmpdTicketDetails.setDBL_PURCHASE_CUR_INPUT_VAT(setScaleNConvertToDouble(purchaseCurTotalInputVat));
		vmpdTicketDetails.setDBL_PURCHASE_CUR_STD_COMMISION(setScaleNConvertToDouble(purchaseCurStdCommision));
		
		Double negMkp = 0.0;
		if(null != master && (null != master.getAccountCode() && !master.getAccountCode().equalsIgnoreCase("C0006")) || (null == master.getAccountCode())){
			if(null != sellingCurServiceFee && sellingCurServiceFee < 0){
				negMkp = negMkp + sellingCurServiceFee;
			}else{
				vmpdTicketDetails.setDBL_SELLING_CUR_SERVICE_FEE(sellingCurServiceFee);
			}
			
			if(null != sellingCurExtraEarning && sellingCurExtraEarning < 0){
				negMkp = negMkp + sellingCurExtraEarning;
			}else{
				vmpdTicketDetails.setDBL_SELLING_CUR_EXTRA_EARNING(sellingCurExtraEarning);
			}
		}
		
		Double disc = sellingCurDiscount != null ? sellingCurDiscount : 0.0;
		vmpdTicketDetails.setDBL_SELLING_CUR_DISCOUNT(setScaleNConvertToDouble(disc + Math.abs(negMkp)));
		
		if(isRefund){
			vmpdTicketDetails.setDBL_PURCHASE_CUR_SUPPLIER_NET(setScaleNConvertToDouble(purchaseCurSupplierAmount));
			vmpdTicketDetails.setDBL_PURCHASE_CUR_AIRLINE_CHARGE(setScaleNConvertToDouble(purchaseCurAirlinePenalty));
			vmpdTicketDetails.setDBL_SELLING_CUR_AGENCY_CHARGE(setScaleNConvertToDouble(sellingCurAgencyCharges));
			vmpdTicketDetails.setDBL_SELLING_CUR_CLIENT_NET(setScaleNConvertToDouble(sellingCurPrice));
			vmpdTicketDetails.setDBL_SELLING_CUR_OUTPUT_VAT(calculateVAT((sellingCurPrice - couponDisc),outVatPercent));
		}else{
			vmpdTicketDetails.setDBL_PURCHASE_CUR_SUPPLIER_AMOUNT(setScaleNConvertToDouble(purchaseCurSupplierAmount));
			vmpdTicketDetails.setDBL_SELLING_CUR_PRICE(setScaleNConvertToDouble(sellingCurPrice));
			Double cd = (sellingCurPrice * couponDisc) / totSellPrice;
			vmpdTicketDetails.setDBL_SELLING_CUR_OUTPUT_VAT(calculateVAT((sellingCurPrice - cd),outVatPercent));
		}
	}

	private TicketJsonMaster populateVMPDTicketFares(TicketJsonMaster tempVMPDTicketDetails, SalesMaster master) {
		
		tempVMPDTicketDetails.setDBL_SELLING_CUR_PUBLISHED_FARE(tempVMPDTicketDetails.getDBL_SELLING_CUR_PUBLISHED_FARE());
		tempVMPDTicketDetails.setDBL_SELLING_CUR_TOTAL_TAX(tempVMPDTicketDetails.getDBL_SELLING_CUR_TOTAL_TAX());
		tempVMPDTicketDetails.setDBL_SELLING_CUR_STD_COMMISION(tempVMPDTicketDetails.getDBL_SELLING_CUR_STD_COMMISION());
		
		Double negMkp = 0.0;
		if(null != master && (null != master.getAccountCode() && !master.getAccountCode().equalsIgnoreCase("C0006")) || (null == master.getAccountCode())){
			if(null != tempVMPDTicketDetails.getDBL_SELLING_CUR_SERVICE_FEE() && tempVMPDTicketDetails.getDBL_SELLING_CUR_SERVICE_FEE() < 0){
				negMkp = negMkp + tempVMPDTicketDetails.getDBL_SELLING_CUR_SERVICE_FEE();
			}else{
				tempVMPDTicketDetails.setDBL_SELLING_CUR_SERVICE_FEE(tempVMPDTicketDetails.getDBL_SELLING_CUR_SERVICE_FEE());
			}
			
			if(null != tempVMPDTicketDetails.getDBL_SELLING_CUR_EXTRA_EARNING() && tempVMPDTicketDetails.getDBL_SELLING_CUR_EXTRA_EARNING() < 0){
				negMkp = negMkp + tempVMPDTicketDetails.getDBL_SELLING_CUR_EXTRA_EARNING();
			}else{
				tempVMPDTicketDetails.setDBL_SELLING_CUR_EXTRA_EARNING(tempVMPDTicketDetails.getDBL_SELLING_CUR_EXTRA_EARNING());
			}
		}
		
		Double disc = tempVMPDTicketDetails.getDBL_SELLING_CUR_DISCOUNT() != null ? tempVMPDTicketDetails.getDBL_SELLING_CUR_DISCOUNT() : 0.0;
		tempVMPDTicketDetails.setDBL_SELLING_CUR_DISCOUNT(setScaleNConvertToDouble(disc + Math.abs(negMkp)));
		
		tempVMPDTicketDetails.setDBL_SELLING_CUR_PRICE(tempVMPDTicketDetails.getDBL_SELLING_CUR_PRICE());
		tempVMPDTicketDetails.setDBL_SELLING_CUR_SUPPLIER_AMOUNT(tempVMPDTicketDetails.getDBL_SELLING_CUR_SUPPLIER_AMOUNT());
		tempVMPDTicketDetails.setDBL_SELLING_CUR_TOTAL_MARKET_FARE(tempVMPDTicketDetails.getDBL_SELLING_CUR_TOTAL_MARKET_FARE());
		tempVMPDTicketDetails.setDBL_BASE_CUR_PUBLISHED_FARE(tempVMPDTicketDetails.getDBL_BASE_CUR_PUBLISHED_FARE());
		tempVMPDTicketDetails.setDBL_BASE_CUR_TOTAL_MARKET_FARE(tempVMPDTicketDetails.getDBL_BASE_CUR_TOTAL_MARKET_FARE());
		tempVMPDTicketDetails.setDBL_BASE_CUR_TOTAL_TAX(tempVMPDTicketDetails.getDBL_BASE_CUR_TOTAL_TAX());
		tempVMPDTicketDetails.setDBL_BASE_CUR_PRICE(tempVMPDTicketDetails.getDBL_BASE_CUR_PRICE());
		tempVMPDTicketDetails.setDBL_BASE_CUR_STD_COMMISION(tempVMPDTicketDetails.getDBL_BASE_CUR_STD_COMMISION());
		tempVMPDTicketDetails.setDBL_BASE_CUR_SUPPLIER_AMOUNT(tempVMPDTicketDetails.getDBL_BASE_CUR_SUPPLIER_AMOUNT());
		tempVMPDTicketDetails.setDBL_BASE_CUR_SERVICE_FEE(tempVMPDTicketDetails.getDBL_BASE_CUR_SERVICE_FEE());
		tempVMPDTicketDetails.setDBL_BASE_CUR_EXTRA_EARNING(tempVMPDTicketDetails.getDBL_BASE_CUR_EXTRA_EARNING());
		tempVMPDTicketDetails.setDBL_BASE_CUR_DISCOUNT(tempVMPDTicketDetails.getDBL_BASE_CUR_DISCOUNT());

		return tempVMPDTicketDetails;
	}

	/**
	 * This method computes additional pax information. The rules are: a) From
	 * all tickets (flights node), skip first ticket b) For each ticket (from
	 * 2nd ticket onwards), get the passenger name and other details (like
	 * below): Passenger Name(Ticket Number, Traveler ID, LPO No., Fare, Tax,
	 * Service Fee, Selling Price); c) Build a semicolon separated string and
	 * use it as additional Pax info.
	 * 
	 * @param allFlights
	 * @param flyinCode
	 * @return - Returns a String for additional pax information
	 */
	private String computeAdditionalPaxInfo(Collection<Flight> allFlights, Flight flight, String flyinCode) {

		StringBuilder remainingPaxInfo = new StringBuilder();

		if (allFlights.size() > 1) {

			for (Flight aTicket : allFlights) {
				
				if((null != flight && null != aTicket && null != aTicket.getPaxName() && null != flight.getPaxName()) && 
						(!aTicket.getPaxName().trim().equalsIgnoreCase(flight.getPaxName().trim()))){
					StringBuilder aPassengerInfo = new StringBuilder();
	
					Double serviceFee = (aTicket.getSellingCurrServiceFee() != null) ? aTicket.getSellingCurrServiceFee() : new Double(0.0);
					Double marketFare = (aTicket.getPurchaseCurrTotMarketFare() != null) ? aTicket.getPurchaseCurrTotMarketFare() : new Double(0.0);
					Double totalTax = (aTicket.getPurchaseCurrTotTax() != null) ? aTicket.getPurchaseCurrTotTax() : new Double(0.0);
					Double totalIVat = (aTicket.getPurchaseCurrInputVAT() != null) ? aTicket.getPurchaseCurrInputVAT() : new Double(0.0);
	
					aPassengerInfo.append(ETRCSConstants.SEMI_COLON_CONSTANT);
					aPassengerInfo.append(aTicket.getPaxName());
					aPassengerInfo.append("(");
					aPassengerInfo.append(flyinCode + ETRCSConstants.COMMA_SEPARATOR);
					aPassengerInfo.append(aTicket.getAirlinePnr() + ETRCSConstants.COMMA_SEPARATOR);
					aPassengerInfo.append(MapperUtil.getDoubleAsCheckedString(marketFare * 1) + ETRCSConstants.COMMA_SEPARATOR);
					aPassengerInfo.append(MapperUtil.getDoubleAsCheckedString((totalTax - totalIVat) * 1) + ETRCSConstants.COMMA_SEPARATOR);
					aPassengerInfo.append(MapperUtil.getDoubleAsCheckedString((serviceFee) * 1) + ETRCSConstants.COMMA_SEPARATOR);
					aPassengerInfo.append(MapperUtil.getDoubleAsCheckedString((marketFare + (totalTax - totalIVat) + serviceFee) * 1));
					aPassengerInfo.append(")");
	
					remainingPaxInfo.append(aPassengerInfo.toString());
				}
			}
		}

		return remainingPaxInfo.toString();
	}

	/**
	 * The purpose of this method is to split the tickets into ET and VMPD.
	 * 
	 * @param allFlights
	 * @return -
	 */
	private void splitTicketsBasedOnType(TraacsInfo traacInfo) {
		List<Flight> allFlights = traacInfo.getFlights();
		String flyinCode = traacInfo.getMaster().getFlyinCode();
		List<Flight> bookWiseFlight = null;
		StringBuilder sector = null;
		StringBuilder fareBasis = null;
		
		for (Flight aFlight : allFlights) {
			if (aFlight != null && null != aFlight.getTicketType() && aFlight.getTicketType().equalsIgnoreCase(ETRCSConstants.CON_TYPE_VMPD)) {
				List<Flight> pnrWiseFlight = null;
				Map<String, List<Flight>> bookFlightMap = null;
				if (vmpdFlights.containsKey(flyinCode)) {
					bookFlightMap = vmpdFlights.get(flyinCode);
				} else {
					bookFlightMap = new HashMap<String, List<Flight>>();
				}
				if (!bookFlightMap.containsKey(aFlight.getTicketNo())) {
					pnrWiseFlight = new ArrayList<Flight>();
				} else {
					pnrWiseFlight = bookFlightMap.get(aFlight.getTicketNo());
				}
				pnrWiseFlight.add(aFlight);
				bookFlightMap.put(aFlight.getTicketNo(), pnrWiseFlight);
				vmpdFlights.put(flyinCode, bookFlightMap);
			} else {

				
				if (!etFlights.containsKey(flyinCode)) {
					bookWiseFlight = new ArrayList<Flight>();
				} else {
					bookWiseFlight = etFlights.get(flyinCode);
				}
				if(null != aFlight.getType() && aFlight.getType().equals("LCC") && !bookWiseFlight.isEmpty() && aFlight.getTicketNo().equals(bookWiseFlight.get(0).getTicketNo())){
					Flight fs = bookWiseFlight.get(0);
					fs.setReturnDate(aFlight.getReturnDate());
					fs.setReturnClass(aFlight.getReturnClass());
					
					if(sector == null){
					   	sector = new StringBuilder();
					   	if(null != fs.getSector())
					   		sector.append(fs.getSector());
					}
					if(null != aFlight.getSector())
						sector.append("/"+aFlight.getSector());			   
					fs.setSector(sector.toString());
					
					if(fareBasis == null){
						fareBasis = new StringBuilder();
						if(null != fs.getFareBasis())
							fareBasis.append(fs.getFareBasis());
					}			   
					if(null != aFlight.getFareBasis())
						fareBasis.append("/"+aFlight.getFareBasis());
					fs.setFareBasis(fareBasis.toString());
					
					Double pubFare = fs.getPurchaseCurrPubFare() != null ? fs.getPurchaseCurrPubFare() : 0.0;
					pubFare = pubFare + ( aFlight.getPurchaseCurrPubFare() != null ? aFlight.getPurchaseCurrPubFare() : 0.0 );
					fs.setPurchaseCurrPubFare( pubFare );
					
					Double marketFare = fs.getPurchaseCurrTotMarketFare() != null ? fs.getPurchaseCurrTotMarketFare() : 0.0;
					marketFare = marketFare + (aFlight.getPurchaseCurrTotMarketFare() != null ? aFlight.getPurchaseCurrTotMarketFare() : 0.0);
					fs.setPurchaseCurrTotMarketFare( marketFare );
					
					Double totTax = fs.getPurchaseCurrTotTax() != null ? fs.getPurchaseCurrTotTax() : 0.0;
					totTax = totTax + (aFlight.getPurchaseCurrTotTax() != null ? aFlight.getPurchaseCurrTotTax() : 0.0);
					fs.setPurchaseCurrTotTax( totTax );
					
					Double stdComm = fs.getPurchaseCurrStdComm() != null ? fs.getPurchaseCurrStdComm() : 0.0;
					stdComm = stdComm + (aFlight.getPurchaseCurrStdComm() != null ? aFlight.getPurchaseCurrStdComm() : 0.0);
					fs.setPurchaseCurrStdComm( stdComm );
					
					Double suppAmt = fs.getPurchaseCurrSupplierAmt() != null ? fs.getPurchaseCurrSupplierAmt() : 0.0;
					suppAmt = suppAmt + (aFlight.getPurchaseCurrSupplierAmt() != null ? aFlight.getPurchaseCurrSupplierAmt() : 0.0);
					fs.setPurchaseCurrSupplierAmt( suppAmt );
					
					Double servFee = fs.getSellingCurrServiceFee() != null ? fs.getSellingCurrServiceFee() : 0.0;
					servFee = servFee + (aFlight.getSellingCurrServiceFee() != null ? aFlight.getSellingCurrServiceFee() : 0.0);
					fs.setSellingCurrServiceFee( servFee );
					
					Double disc = fs.getSellingCurrDiscount() != null ? fs.getSellingCurrDiscount() : 0.0;
					disc = disc + (aFlight.getSellingCurrDiscount() != null ? aFlight.getSellingCurrDiscount() : 0.0);
					fs.setSellingCurrDiscount( disc );
					
					Double totFare = fs.getSellingCurrPrice() != null ? fs.getSellingCurrPrice() : 0.0;
					totFare = totFare + (aFlight.getSellingCurrPrice() != null ? aFlight.getSellingCurrPrice() : 0.0);
					fs.setSellingCurrPrice( totFare );
					
					bookWiseFlight.clear();
					bookWiseFlight.add(fs);
				}else{
					bookWiseFlight.add(aFlight);
				}
				etFlights.put(flyinCode, bookWiseFlight);
			}
		}

	}
	
	/**
	 * This method will get hotel static data for hotel uniqueid
	 * 
	 * @param uniqueIds
	 * @return
	 */
	public List<HotelStaticDataResponse> getHotelStaticData(String uniqueIds, String langCode, Message msg, String lpo){
		List<HotelStaticDataResponse> hotelStaticDataResponseList = null;
		try {
			
			logger.info("TraacsMasterRequestMapper getHotelStaticData() method start for Booking ID : " + lpo + ", and Hotel Unique ID : " + uniqueIds );		
			
			ObjectMapper mapper = new ObjectMapper();
			HotelStaticDataRequest hotelStaticDataRequest= new HotelStaticDataRequest();
			HCP hcp= new HCP();
			Source source= new Source();
			
			source.setAccessToken("09823jklsfljjsfd");
			source.setClientId("21321312JW");
			source.setClientSecret("21321312JDSLKJDOIW");
			source.setDevice("FLYIN");
			source.setMandatorId("13");
			source.setTimeStamp(convertDateToFormat(new Date()));
			Crt crt= new Crt();
			crt.setHuid(uniqueIds);
			if(langCode == null){
				crt.setLanguage("en_GB");
				crt.setWithDistance(false);
			}else{
				crt.setLanguage(langCode);
				crt.setWithDistance(true);
			}			
			crt.setPt("B|F");
			hcp.setSource(source);
			hcp.setCrt(crt);
			hotelStaticDataRequest.setHcp(hcp);
			
			String request = mapper.writeValueAsString(hotelStaticDataRequest);
			logger.info("HotelStatic Data Request: " + request );
			
			DefaultHttpClient httpClient=new DefaultHttpClient();
			StringEntity requestEntity = new StringEntity(request,StandardCharsets.UTF_8);
			requestEntity.setContentType("application/json");
			
			String htlStaticUrl = null;
			if(null != ((Event) msg).getVariables().get(ETRCSConstants.HOTEL_STATIC_DATA_URL)){
				htlStaticUrl = ((Event) msg).getVariables().get(ETRCSConstants.HOTEL_STATIC_DATA_URL).getValue().toString();
			}else{
				htlStaticUrl = "http://10.62.147.63:4000/hotelContent";
			}
			
			HttpPost postMethod = new HttpPost(htlStaticUrl);
			postMethod.setEntity(requestEntity);
			
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			
			int timeout = Integer.valueOf(10);
		    HttpParams httpParams = httpClient.getParams();
		    httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout * 1000);
		    httpParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout * 1000);
			
			String responseBody = httpClient.execute(postMethod,responseHandler);			
			
			mapper.setVisibilityChecker(mapper.getSerializationConfig().getDefaultVisibilityChecker()
	                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
	                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
	                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
	                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));		
			
			hotelStaticDataResponseList =  mapper.readValue(responseBody, 
					TypeFactory.defaultInstance().constructCollectionType(List.class,  
							HotelStaticDataResponse.class));
			
			logger.info("Hotel Static URL : " + htlStaticUrl);
			
			if(!hotelStaticDataResponseList.isEmpty() && null != hotelStaticDataResponseList.get(0).getHotel() && 
					null != hotelStaticDataResponseList.get(0).getHotel().getBasicInfo()){
				logger.info("Hotel Static Response Data : " + hotelStaticDataResponseList.get(0).getHotel().getBasicInfo().getName());
			}

		} catch (IOException e) {
			logger.error("Exception in TraacsMasterRequestMapper getHotelStaticData() method for Booking ID : " + lpo + ", for Unique ID : " + uniqueIds + " : " + e.getMessage());
		}
		
		logger.info("TraacsMasterRequestMapper getHotelStaticData() method end for Booking ID : " + lpo + ", and Hotel Unique ID : " + uniqueIds );
		return hotelStaticDataResponseList;
	}
	
	/**
	 * Convert Date To OTA Format
	 * 
	 * @param date
	 * @return
	 */
	public static String convertDateToFormat(Date date) {
		try {
			if (date != null) {
				String lasmod = new SimpleDateFormat("yyyy-MM-dd").format(date);
				lasmod = lasmod + "T00:00:00";
				return lasmod;
			}
		} catch (Exception e) {
			logger.error("Exception in TraacsMasterRequestMapper convertDateToFormat() method : " + e.getMessage());
		}
		return null;
	}
	
	
	private void populateMasterData(SalesMaster master, MasterDetailsJson masterDetails) {
		
		logger.info("TraacsMasterRequestMapper populateMasterData() method for Booking : " + master.getFlyinCode() + " START");
		
		String companyId = master.getCompanyId();
		masterDetails.setSTR_ACTION(master.getAction());		
		
		if(null != master.getCostCenterCode()){
			masterDetails.setSTR_COST_CENTRE_CODE(master.getCostCenterCode());
		}else{
			masterDetails.setSTR_COST_CENTRE_CODE(suppllierIdsMap.get(ETRCSConstants.VAR_COST_CENTER_PREFIX + companyId));
		}
		
		if(null != master.getDepartmentCode()){
			masterDetails.setSTR_DEPARTMENT_CODE(master.getDepartmentCode());
		}else{
			masterDetails.setSTR_DEPARTMENT_CODE(suppllierIdsMap.get(ETRCSConstants.VAR_DEPT_PREFIX + companyId));
		}
		
		masterDetails.setSTR_REFERENCE(master.getReference());
		masterDetails.setSTR_NARRATION(master.getNarration());
		masterDetails.setSTR_SELLING_CUR_CODE(master.getSellingCurrCode());

		if(null != master.getAccountCode()){
			masterDetails.setSTR_ACCOUNT_CODE(master.getAccountCode());
		}else{
			if(null != master.getSellingCurrCode() && master.getSellingCurrCode().equals("EGP")){
				masterDetails.setSTR_ACCOUNT_CODE("CASHCNTRL2");
			}else if(null != master.getSellingCurrCode() && master.getSellingCurrCode().equals("SAR")){
				masterDetails.setSTR_ACCOUNT_CODE("CASHCNTRL");
			}
		}
		
		masterDetails.setSTR_PAY_ID(master.getPayId());
		masterDetails.setSTR_CC_NO(master.getCcNo());
		masterDetails.setSTR_POS_ID(master.getPosId());
		masterDetails.setDBL_COUPON_DISCOUNT(setScaleNConvertToDouble(master.getCouponDiscount()));
		
		if(null != master.getInvoiceDate())
			masterDetails.setSTR_INVOICE_DATE(StandAloneRequestBuilderUtil.dateFormatter(master.getInvoiceDate()));
		
		if(null != master.getRefundDate())
			masterDetails.setSTR_REFUND_DATE(StandAloneRequestBuilderUtil.dateFormatter(master.getRefundDate()));
		
		logger.info("TraacsMasterRequestMapper populateMasterData() method for Booking : " + master.getFlyinCode() + " END");
	}

	private void populateFlightCommonData(Flight flight, TicketJsonMaster ticketDetails) {
		
		/*if((null != flight.getType() && flight.getType().equalsIgnoreCase("GDS")) && 
				(null != flight.getGds() && flight.getGds().equalsIgnoreCase("CLEARTRIP")) &&
				(null != reqCount && reqCount >= 1)){
			
			ticketDetails.setSTR_TYPE(ETRCSConstants.HOTEL_STR_TYPE);
			ticketDetails.setSTR_GDS(ETRCSConstants.CON_ONLINE);
		}else{
			ticketDetails.setSTR_TYPE(flight.getType());
			ticketDetails.setSTR_GDS(flight.getGds());
		}*/
		
		if(null != flight.getType() && flight.getType().toUpperCase().equals("DIRECT")){
			ticketDetails.setSTR_TYPE("DIRECT");
			ticketDetails.setSTR_GDS("DIRECT");
		}else{
			ticketDetails.setSTR_TYPE(flight.getType());
			ticketDetails.setSTR_GDS(flight.getGds());	
		}
		
		if(null != flight.getGds() && flight.getGds().equalsIgnoreCase("CLEARTRIP")){
			ticketDetails.setSTR_GDS("GALILEO");
		}
		
		// Implemented void functionality
		if (flight.getStatus() != null && flight.getStatus().equalsIgnoreCase(ETRCSConstants.CON_VOID)) {
			ticketDetails.setSTR_REFUND_STAFF_EMAIL_ID(flight.getRefundStaffEmailId());
			ticketDetails.setSTR_REFUND_STATUS(ETRCSConstants.CON_VOID);
			if (flight.getRefundDate() != null) {
				if (flight.getRefundDate().contains("/")) {
					ticketDetails.setSTR_REFUND_DATE(StandAloneRequestBuilderUtil.dateFormatter(flight.getRefundDate()));
				} else {
					ticketDetails.setSTR_REFUND_DATE(
							StandAloneRequestBuilderUtil.dateFormatter(flight.getRefundDate()));
				}
			}
		}

		String supplierAcntCode = null;
		if(null != flight.getGds() && flight.getGds().toUpperCase().equals("AMADEUS")){
			if(!StringUtils.isEmpty(flight.getTicketingAgencyOfficeNo())){
				supplierAcntCode = suppllierIdsMap
						.get(ETRCSConstants.CON_SUPPLIER_CODE_PREFIX + flight.getTicketingAgencyOfficeNo());
			}else if (!StringUtils.isEmpty(flight.getBkngAgencyOfficeNo())) {
				supplierAcntCode = suppllierIdsMap
						.get(ETRCSConstants.CON_SUPPLIER_CODE_PREFIX + flight.getBkngAgencyOfficeNo());
			}
		}else if(null != flight.getGds() && (flight.getGds().equalsIgnoreCase("CLEARTRIP") ||
											 (flight.getGds().equals("ONLINE") && null != flight.getSupplierCode() && flight.getSupplierCode().equals("C0012")))){
			supplierAcntCode = suppllierIdsMap.get("CLEARTRIP");
		}else if(null != flight.getType() && flight.getType().toUpperCase().equals("DIRECT")){
			supplierAcntCode = suppllierIdsMap.get("SVDIRECT");
		}else{
			String supplier = flight.getAirlineName() != null ? flight.getAirlineName() : "";
			if(null != supplier && supplier.toUpperCase().contains("FLYNAS")){
				supplier = "FLYNAS";
			}else if(null != supplier && supplier.toUpperCase().contains("GDSINVENTORY")){
				supplier = "GDSINVENTORY";
			}else if(null != supplier && supplier.toUpperCase().contains("SABRE")){
				supplier = "SABRE";
			}else if(null != supplier && supplier.toUpperCase().contains("JAZEERA")){
				supplier = "JAZEERA";
			}else if(null != supplier && supplier.toUpperCase().contains("AIRCAIRO")){
				supplier = "AIRCAIRO";
			}else if(null != supplier && supplier.toUpperCase().contains("HITCHHICKER")){
				supplier = "HITCHHICKER";
			}else if(null != supplier && supplier.toUpperCase().contains("GALILEO")){
				supplier = "GALILEO";
			}else if(null != supplier && supplier.toUpperCase().contains("AIRARABIA")){
				supplier = "AIRARABIA";
			}else if(null != supplier && supplier.toUpperCase().contains("FLYDUBAI")){
				supplier = "FLYDUBAI";
			}else if(null != supplier && supplier.toUpperCase().contains("PYTON")){
				supplier = "PYTON";
			}
			
			if(null != suppllierIdsMap.get(supplier.toUpperCase())){
				supplierAcntCode = suppllierIdsMap.get(supplier.toUpperCase());
			}else{
				supplierAcntCode = flight.getSupplierCode();
			}
		}
		
		ticketDetails.setSTR_SUPPLIER_CODE(supplierAcntCode);
		ticketDetails.setSTR_TICKET_TYPE(flight.getTicketType());

		if(null != flight.getAirlineNCode()){
			if(flight.getAirlineNCode().length() == 1){
				ticketDetails.setSTR_AIRLINE_NUMERIC_CODE("00"+flight.getAirlineNCode());
			}else if(flight.getAirlineNCode().length() == 2){
				ticketDetails.setSTR_AIRLINE_NUMERIC_CODE("0"+flight.getAirlineNCode());
			}else{
				ticketDetails.setSTR_AIRLINE_NUMERIC_CODE(flight.getAirlineNCode());
			}
		}
		
		ticketDetails.setSTR_AIRLINE_NAME(flight.getAirlineName());
		ticketDetails.setSTR_AIRLINE_CHARACTER_CODE(flight.getAirlineCCode());
		ticketDetails.setSTR_AIRLINE_REF_NO(flight.getAirlineRefNo());
		ticketDetails.setSTR_TICKET_NO(flight.getTicketNo());
		ticketDetails.setSTR_PAX_NAME(flight.getPaxName());
		ticketDetails.setSTR_PAX_TYPE(flight.getPaxType());		
		ticketDetails.setSTR_TICKET_REMARK(null);
		ticketDetails.setSTR_SECTOR(flight.getSector());

		if(null != flight.getTravelerClass()){
			ticketDetails.setCHR_TRAVELER_CLASS(flight.getTravelerClass());
		}else{
			ticketDetails.setCHR_TRAVELER_CLASS(populateRBD(flight.getFlightClass()));
		}
		
		if(null != flight.getReturnDate()){
			if(null != flight.getReturnClass()){
				ticketDetails.setCHR_RETURN_CLASS(flight.getReturnClass());
			}else{
				ticketDetails.setCHR_RETURN_CLASS(populateRBD(flight.getFlightClass()));
			}			
		}

		if (flight.getBkngStaffEmail() != null) {
			ticketDetails.setSTR_BOOKING_STAFF_EMAIL_ID(flight.getBkngStaffEmail());
		} else {
			ticketDetails.setSTR_BOOKING_STAFF_EMAIL_ID(ETRCSConstants.CON_B2C_STAFF_EMAIL);
		}
		if (flight.getTicketingStaffEmail() != null) {
			ticketDetails.setSTR_TICKETING_STAFF_EMAIL_ID(flight.getTicketingStaffEmail());
		} else {
			ticketDetails.setSTR_TICKETING_STAFF_EMAIL_ID(ETRCSConstants.CON_B2C_STAFF_EMAIL);
		}
		
		if(!StringUtils.isEmpty(flight.getPnrNo()))
			ticketDetails.setSTR_PNR_NO(flight.getPnrNo());
		else if(null != flight.getAirlinePnr())
			ticketDetails.setSTR_PNR_NO(flight.getAirlinePnr());
		
		ticketDetails.setSTR_LAST_CONJ_TICKET(flight.getLastConjTicket());
		ticketDetails.setSTR_TOUR_CODE(flight.getTourCode());
		ticketDetails.setSTR_FARE_BASIS(flight.getFareBasis());
		ticketDetails.setSTR_REGION_CODE(flight.getRegionCode());

		if (flight.getTravelDate() != null && flight.getTravelDate().contains(ETRCSConstants.HYPEN_CONSTANT)) {
			ticketDetails.setSTR_TRAVEL_DATE(StandAloneRequestBuilderUtil.dateFormatter(flight.getTravelDate()));
		} else {
			ticketDetails.setSTR_TRAVEL_DATE(StandAloneRequestBuilderUtil.dateFormatter(flight.getTravelDate()));
		}
		if (flight.getReturnDate() != null && flight.getReturnDate().contains(ETRCSConstants.HYPEN_CONSTANT)) {
			ticketDetails.setSTR_RETURN_DATE(StandAloneRequestBuilderUtil.dateFormatter(flight.getReturnDate()));
		} else {
			ticketDetails.setSTR_RETURN_DATE(StandAloneRequestBuilderUtil.dateFormatter(flight.getReturnDate()));
		}
		
		ticketDetails.setSTR_LAST_CONJ_TICKET(flight.getLastConjTicket());
		
		if(null != flight.getBkngAgencyIATANo()){
			ticketDetails.setSTR_BOOKING_AGENCY_IATA_NO(flight.getBkngAgencyIATANo());
		}else{
			ticketDetails.setSTR_BOOKING_AGENCY_IATA_NO(suppllierIdsMap.get(ETRCSConstants.CON_IATA_CODE_PREFIX + flight.getBkngAgencyOfficeNo()));
		}
		
		if(null != flight.getTicketingAgencyIATANo()){
			ticketDetails.setSTR_TICKETING_AGENCY_IATA_NO(flight.getTicketingAgencyIATANo());
		}else{
			ticketDetails.setSTR_TICKETING_AGENCY_IATA_NO(suppllierIdsMap.get(ETRCSConstants.CON_IATA_CODE_PREFIX + flight.getTicketingAgencyOfficeNo()));
		}
		
		if(null != flight.getType() && flight.getType().toUpperCase().equals("DIRECT")){
			ticketDetails.setSTR_BOOKING_AGENCY_OFFICE_ID("SVDIRECT");
			ticketDetails.setSTR_TICKETING_AGENCY_OFFICE_ID("SVDIRECT");
			ticketDetails.setSTR_PNR_FIRST_OWNER_OFFICE_ID("SVDIRECT");
			ticketDetails.setSTR_PNR_CURRENT_OWNER_OFFICE_ID("SVDIRECT");
		}else{
			ticketDetails.setSTR_BOOKING_AGENCY_OFFICE_ID(flight.getBkngAgencyOfficeNo());
			ticketDetails.setSTR_TICKETING_AGENCY_OFFICE_ID(flight.getTicketingAgencyOfficeNo());
			ticketDetails.setSTR_PNR_FIRST_OWNER_OFFICE_ID(flight.getPnrFirstOwnerOfficeNo());
			ticketDetails.setSTR_PNR_CURRENT_OWNER_OFFICE_ID(flight.getPnrCurrentOwnerOfficeNo());			
		}
		
		ticketDetails.setSTR_TICKET_ISSUE_DATE(StandAloneRequestBuilderUtil.dateFormatter(flight.getTicketIssueDt()));
		ticketDetails.setSTR_REPORTING_DATE(StandAloneRequestBuilderUtil.dateFormatter(flight.getReportingDt()));
		ticketDetails.setSTR_LPO_NO(flight.getLpo());
		ticketDetails.setSTR_AIRLINE_PNR(flight.getAirlinePnr());
		ticketDetails.setINT_NO_OF_PAX(flight.getNoOfPax());
		ticketDetails.setSTR_PRODUCT(flight.getProduct());
		ticketDetails.setSTR_PURCHASE_CUR_CODE(flight.getPurchaseCurrCode());

	}
	
	private void populateSharingData(List<Sharing> sharing, List<SharingJsonMaster> sharingDetail) {
		if(null != sharing){
			for(Sharing sh : sharing){
				
				SharingJsonMaster sharingDetails = new SharingJsonMaster(); 
				
				sharingDetails.setSTR_DATE(StandAloneRequestBuilderUtil.dateFormatter(sh.getDate()));
				sharingDetails.setSTR_REFERENCE(sh.getReference());
				sharingDetails.setSTR_SELLING_CUR_CODE(sh.getSellingCurrCode());
				sharingDetails.setSTR_ACCOUNT_CODE(sh.getAccountCode());
				sharingDetails.setDBL_SELLING_CUR_AMOUNT(sh.getSellingCurAmt());
				sharingDetails.setSTR_CC_NO(sh.getCcNo());
				sharingDetails.setSTR_PAY_ID(sh.getPayId());
				sharingDetails.setSTR_POS_ID(sh.getPosId());
				
				sharingDetail.add(sharingDetails);
			}
		}
	}
	
	private void populateOtherData(List<Other> other, List<OtherJsonMaster> otherDetails, Message msg, SalesMaster master) {
		
		 

		
		if(null != other){
			for(Other oth : other){
				if(null != oth){
					OtherJsonMaster othDtl = new OtherJsonMaster();
					Message.Builder messageBuilder2 = Message.builder(msg); 
					
					if( null != ((Event)msg).getVariables().get(ETRCSConstants.REFUND_PAX_ID).getValue() ){
						((Map<String, String>) messageBuilder2).put( ETRCSConstants.REFUND_PAX_ID, ((Event)msg).getVariables().get(ETRCSConstants.REFUND_PAX_ID)+","+oth.getSelcPaxOrUnitID().toString() );
					} else {
						((Map<String, String>) messageBuilder2).put( ETRCSConstants.REFUND_PAX_ID, oth.getSelcPaxOrUnitID() );
					}

					//Mandatory Fields
					othDtl.setSTR_TYPE(oth.getType());
					othDtl.setSTR_SERVICE(oth.getService());
					othDtl.setSTR_PARTICULARS(oth.getParticulars());
					othDtl.setSTR_PAX_NAME(oth.getPaxName());
					othDtl.setINT_NO_OF_PAX(oth.getNoOfPax());
					othDtl.setSTR_ADDITIONAL_PAX(oth.getAdditionalPax());
					othDtl.setSTR_SUPPLIER_CODE(oth.getSupplierCode());
					othDtl.setSTR_COUNTRY(oth.getCountry());
					othDtl.setSTR_CITY(oth.getCity());
					othDtl.setSTR_LPO_NO(oth.getLpo());
					othDtl.setSTR_ISSUE_DATE(StandAloneRequestBuilderUtil.dateFormatter(oth.getIssueDate()));
					othDtl.setSTR_FROM_DATE(StandAloneRequestBuilderUtil.dateFormatter(oth.getFromDate()));
					othDtl.setSTR_TO_DATE(StandAloneRequestBuilderUtil.dateFormatter(oth.getToDate()));
					othDtl.setSTR_STAFF_EMAIL_ID(oth.getStaffEmail());
					othDtl.setSTR_SUPPLIER_CONF_NUMBER(oth.getSupplierConfNo());
					othDtl.setSTR_PRODUCT(oth.getProduct());
					othDtl.setSTR_PURCHASE_CUR_CODE(oth.getPurchaseCurrCode());
					othDtl.setDBL_PURCHASE_CUR_TOTAL_FARE(oth.getPurchaseCurrTotalFare());
					othDtl.setDBL_PURCHASE_CUR_TOTAL_TAX(oth.getPurchaseCurrTotTax());
					othDtl.setDBL_PURCHASE_CUR_SUP_COMMISION(oth.getPurchaseCurrSuppComm());
					othDtl.setDBL_PURCHASE_CUR_SUPPLIER_FEE(oth.getPurchaseCurrServiceFee());
					
					if(null != oth.getRefundStatus() && oth.getRefundStatus().equalsIgnoreCase("REFUND")){
						othDtl.setSTR_VOUCHER_NO(oth.getVoucherNo());
						othDtl.setSTR_REFUND_DATE(oth.getRefundDate());
						othDtl.setSTR_REFUND_STAFF_EMAIL_ID(oth.getRefundEmailId());
						othDtl.setSTR_REFUND_STATUS(oth.getRefundStatus());
						othDtl.setDBL_SELLING_CUR_AGENCY_CHARGE(oth.getSellingCurrAgencyCharges());
						othDtl.setDBL_PURCHASE_CUR_SUPPLIER_CHARGE(oth.getPurchaseCurrSuppCharges());
						othDtl.setDBL_PURCHASE_CUR_SUPPLIER_NET(oth.getPurchaseCurrSuppNet());
						othDtl.setDBL_SELLING_CUR_CLIENT_NET(oth.getSellingCurrClientNet());
					}else{
						othDtl.setDBL_PURCHASE_CUR_SUPPLIER_AMOUNT(setScaleNConvertToDouble(oth.getPurchaseCurrSupplierAmt()));
						othDtl.setDBL_SELLING_CUR_PRICE(oth.getSellingCurrPrice());
					}
					
					Double negMkp = 0.0;
					if(null != master && (null != master.getAccountCode() && !master.getAccountCode().equalsIgnoreCase("C0006")) || (null == master.getAccountCode())){
						if(null != oth.getSellingCurrServiceFee() && oth.getSellingCurrServiceFee() < 0){
							negMkp = negMkp + oth.getSellingCurrServiceFee();
						}else{
							othDtl.setDBL_SELLING_CUR_SERVICE_FEE(oth.getSellingCurrServiceFee());
						}
						
						if(null != oth.getSellingCurrExtraEarning() && oth.getSellingCurrExtraEarning() < 0){
							negMkp = negMkp + oth.getSellingCurrExtraEarning();
						}else{
							othDtl.setDBL_SELLING_CUR_EXTRA_EARNING(oth.getSellingCurrExtraEarning());
						}
					}
				
					Double disc = oth.getSellingCurrDiscount() != null ? oth.getSellingCurrDiscount() : 0.0;
					othDtl.setDBL_SELLING_CUR_DISCOUNT(setScaleNConvertToDouble(disc + Math.abs(negMkp)));
					
					othDtl.setDBL_SELLING_CUR_CC_CHARGES(oth.getSellingCurrCcCharges());
					othDtl.setDBL_PURCHASE_CUR_INPUT_VAT(oth.getPurchaseCurrInputVAT());
					othDtl.setDBL_SELLING_CUR_OUTPUT_VAT(oth.getSellingCurrOutputVAT());
					
					otherDetails.add(othDtl);
				}
			}
		}
	}

	private String populateRBD(String flightClass) {
		String rbd = null;
		if(null != flightClass){
			if(flightClass.equalsIgnoreCase("Economy")){
				rbd = "Y";
			}else if(flightClass.equalsIgnoreCase("Business")){
				rbd = "J";
			}else if(flightClass.equalsIgnoreCase("First")){
				rbd = "F";
			}
		}
		return rbd;
	}
	
	/** Convert Double to Bigdecimal
	 * Set Scale in integer value
	 * Convert Bigdecimal back to Double again
	 * @param doubleVal
	 * @param scalValue
	 * @return
	 */
	public static Double setScaleNConvertToDouble(Double doubleVal){
		Integer scalValue = 2;
		BigDecimal price = BigDecimal.ZERO;
		try{
			BigDecimal val = BigDecimal.ZERO;
			if(null != doubleVal){
				val = new BigDecimal(doubleVal);
			}
			if(null != doubleVal){
				price = (val).setScale(scalValue, RoundingMode.HALF_UP);
			}
		}catch (Exception e){
			logger.error("Exception occured in StandaloneReqBuilder setScaleNConvertToDouble due to" + e, e);
		}
		return price.doubleValue();
	}
	
	/** Calculate Output VAT
	 * On VAT Percentage
	 * @param applicablePrice
	 * @param percentage
	 * @return
	 */
	public static Double calculateVAT(Double applicablePrice, Double percentage){
		Double oVat = 0.0;
		Integer scalValue = 2;
		try{
			
			BigDecimal price = BigDecimal.ZERO;
			if(null != applicablePrice){
				price = new BigDecimal(applicablePrice);
			}
			BigDecimal perc = BigDecimal.ZERO;
			if(null != percentage){
				perc = new BigDecimal(percentage);
			}
			
			//oVat = ((price.multiply(perc)).divide(new BigDecimal(100), scalValue, RoundingMode.HALF_UP)).doubleValue();			
			double vat = price.multiply(perc).setScale(6, RoundingMode.HALF_UP).doubleValue();
			oVat = new BigDecimal(vat).divide(new BigDecimal(100), scalValue, RoundingMode.HALF_UP).doubleValue();
			
		} catch (Exception e) {
			logger.error("Exception occured in TraacsMasterRequestMapper calculateOutputVAT due to" + e, e);
		}

		return oVat;
	}

}
