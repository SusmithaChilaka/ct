package com.ebreez.etrcs.traacs.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mule.runtime.api.event.Event;
import org.mule.runtime.api.message.Message;


import com.ebreez.etrcs.constants.ETRCSConstants;
import com.ebreez.etrcs.request.vo.TraacsInfo;
import com.ebreez.etrcs.request.vo.TraacsInfoRequest;
import com.ebreez.etrcs.traacs.request.vo.FlightJson;
import com.ebreez.etrcs.traacs.request.vo.HotelJson;
import com.ebreez.etrcs.traacs.vo.Authentication;
import com.ebreez.etrcs.traacs.vo.HotelJsonMaster;
import com.ebreez.etrcs.traacs.vo.RoomDetailsJson;
import com.ebreez.etrcs.traacs.vo.TicketJsonMaster;
import com.ebreez.etrcs.util.StandAloneRequestBuilderUtil;
import com.ebreez.etrcs.vo.Flight;
import com.ebreez.etrcs.vo.Hotel;
import com.ebreez.etrcs.vo.RoomDetails;
import com.ebreez.etrcs.vo.SalesMaster;

public class JsonMapper {
	
	public static List<FlightJson> buildFlightJson(Authentication auth,Map<String,String> properties, Message message) {
		TraacsInfoRequest tcsReq = (TraacsInfoRequest)((Event) message).getVariables().get(ETRCSConstants.VAR_UPDATE_REQUEST).getValue();
		List<TraacsInfo> traacInfoList = tcsReq.getTraacInfoRQ().getTraacDetails();
		List<FlightJson> tickets = new ArrayList<FlightJson>();

		for(TraacsInfo traacInfo:traacInfoList){
			SalesMaster master = traacInfo.getMaster();
			if(traacInfo.getFlights() != null && !traacInfo.getFlights().isEmpty()){
				for(Flight flight : traacInfo.getFlights()){
					FlightJson aJson = new FlightJson();
					TicketJsonMaster ticketDetails = new TicketJsonMaster();
					ticketDetails.setSTR_TYPE(flight.getType());
					ticketDetails.setSTR_TICKET_ISSUE_DATE(flight.getTicketIssueDt());
					ticketDetails.setSTR_SUPPLIER_CODE(flight.getSupplierCode());
					ticketDetails.setSTR_TICKET_TYPE(flight.getTicketType());
					ticketDetails.setINT_NO_OF_PAX(flight.getNoOfPax());	
					ticketDetails.setSTR_LPO_NO(flight.getLpo());
					ticketDetails.setSTR_AIRLINE_NUMERIC_CODE(flight.getAirlineNCode());
					ticketDetails.setSTR_AIRLINE_NAME(flight.getAirlineName());
					ticketDetails.setSTR_AIRLINE_CHARACTER_CODE(flight.getAirlineCCode());
					ticketDetails.setSTR_AIRLINE_REF_NO(flight.getAirlineRefNo());
					if(master.getInvoiceDate() != null && master.getInvoiceDate().contains("/")){
						ticketDetails.setSTR_TICKET_ISSUE_DATE(master.getInvoiceDate());
						ticketDetails.setSTR_REPORTING_DATE(master.getInvoiceDate());
					}else{
						ticketDetails.setSTR_TICKET_ISSUE_DATE(StandAloneRequestBuilderUtil.dateFormatter(master.getInvoiceDate()));
						ticketDetails.setSTR_REPORTING_DATE(StandAloneRequestBuilderUtil.dateFormatter(master.getInvoiceDate()));

					}
					
					ticketDetails.setSTR_TRAVELER_ID(flight.getTravelerId());
					ticketDetails.setSTR_TICKET_NO(flight.getTicketNo());
					ticketDetails.setSTR_PAX_NAME(flight.getPaxName());
					ticketDetails.setSTR_ADDITIONAL_PAX(flight.getAdditionalPax());
					ticketDetails.setSTR_PAX_TYPE(flight.getPaxType());
					ticketDetails.setSTR_SECTOR(flight.getSector());
					ticketDetails.setCHR_TRAVELER_CLASS(flight.getTravelerClass());
					ticketDetails.setCHR_RETURN_CLASS(flight.getReturnClass());
					ticketDetails.setSTR_GDS(flight.getGds());
					if(flight.getBkngStaffEmail() != null){
						ticketDetails.setSTR_BOOKING_STAFF_EMAIL_ID(flight.getBkngStaffEmail());
					}else{
						ticketDetails.setSTR_BOOKING_STAFF_EMAIL_ID("website@flyin.com");
					}
					if(flight.getTicketingStaffEmail() != null){
						ticketDetails.setSTR_TICKETING_STAFF_EMAIL_ID(flight.getTicketingStaffEmail());
					}else{
						ticketDetails.setSTR_TICKETING_STAFF_EMAIL_ID("website@flyin.com");
					}
					ticketDetails.setSTR_PNR_NO(flight.getPnrNo());
					ticketDetails.setSTR_LAST_CONJ_TICKET(flight.getLastConjTicket());
					ticketDetails.setSTR_TOUR_CODE(flight.getTourCode());
					ticketDetails.setSTR_FARE_BASIS(flight.getFareBasis());
					ticketDetails.setSTR_REGION_CODE(flight.getRegionCode());
					ticketDetails.setSTR_TICKET_REMARK(flight.getTktRemarks());
					if(flight.getTravelDate().contains("-")){
						ticketDetails.setSTR_TRAVEL_DATE(StandAloneRequestBuilderUtil.dateFormatter(flight.getTravelDate()));
					}else{
						ticketDetails.setSTR_TRAVEL_DATE(flight.getTravelDate());
					}
					if(flight.getReturnDate().contains("-")){
						ticketDetails.setSTR_RETURN_DATE(StandAloneRequestBuilderUtil.dateFormatter(flight.getReturnDate()));
					}else{
						ticketDetails.setSTR_RETURN_DATE(flight.getReturnDate());
					}
					ticketDetails.setSTR_LAST_CONJ_TICKET(flight.getLastConjTicket());
					ticketDetails.setSTR_PRODUCT(ETRCSConstants.CON_FLIGHT_PRODUCT);
					ticketDetails.setDBL_PURCHASE_ROE(flight.getPurchaseRoe());
					ticketDetails.setSTR_PURCHASE_CUR_CODE(flight.getPurchaseCurrCode());
					ticketDetails.setDBL_SELLING_CUR_PUBLISHED_FARE(multiplyValues(flight.getSellingCurrPubFare(),flight.getPurchaseRoe()));
					ticketDetails.setDBL_SELLING_CUR_TOTAL_MARKET_FARE(multiplyValues(flight.getSellingCurrTotMarketFare(),flight.getPurchaseRoe()));
					ticketDetails.setDBL_SELLING_CUR_TOTAL_TAX(multiplyValues(flight.getSellingCurrTotTax(),flight.getPurchaseRoe()));
					ticketDetails.setDBL_SELLING_CUR_STD_COMMISION(multiplyValues(flight.getSellingCurrStdComm(),flight.getPurchaseRoe()));
					ticketDetails.setDBL_SELLING_CUR_SUPPLIER_AMOUNT(multiplyValues(flight.getSellingCurrSupplierAmt(),flight.getPurchaseRoe()));
					ticketDetails.setDBL_SELLING_CUR_SERVICE_FEE(multiplyValues(flight.getSellingCurrServiceFee(),flight.getPurchaseRoe()));
					ticketDetails.setDBL_SELLING_CUR_EXTRA_EARNING(multiplyValues(flight.getSellingCurrExtraEarning(),flight.getPurchaseRoe()));
					ticketDetails.setDBL_SELLING_CUR_DISCOUNT(multiplyValues(flight.getSellingCurrDiscount(),flight.getPurchaseRoe()));
					ticketDetails.setDBL_SELLING_CUR_PRICE(flight.getSellingCurrSupplierNet());
					ticketDetails.setDBL_BASE_CUR_PUBLISHED_FARE(flight.getBaseCurrPubFare());
					ticketDetails.setDBL_BASE_CUR_TOTAL_MARKET_FARE(flight.getBaseCurrTotMarketFare());
					ticketDetails.setDBL_BASE_CUR_TOTAL_TAX(flight.getBaseCurrTotTax());
					ticketDetails.setDBL_BASE_CUR_STD_COMMISION(flight.getBaseCurrStdComm());
					ticketDetails.setDBL_BASE_CUR_SUPPLIER_AMOUNT(flight.getBaseCurrSupplierAmt());
					ticketDetails.setDBL_BASE_CUR_SERVICE_FEE(flight.getBaseCurrServiceFee());
					ticketDetails.setDBL_BASE_CUR_EXTRA_EARNING(flight.getBaseCurrExtraEarning());
					ticketDetails.setDBL_BASE_CUR_DISCOUNT(flight.getBaseCurrDiscount());
					ticketDetails.setDBL_BASE_CUR_PRICE(flight.getBaseCurrTotMarketFare());
					ticketDetails.setDBL_SELLING_CUR_PRICE(flight.getSellingCurrPrice());
					
					aJson.setJson_master(ticketDetails);
					aJson.setStr_authentication_key(auth);
					tickets.add(aJson);
				}
			}
		}
		return tickets;
	}
	
	public static List<HotelJson> buildHotelJson(Authentication auth,Map<String,String> properties, Message message) {
		TraacsInfoRequest tcsReq =  (TraacsInfoRequest)((Event) message).getVariables().get(ETRCSConstants.VAR_UPDATE_REQUEST).getValue();
		String autoInvoice = properties.get(ETRCSConstants.TRAACS_AUTOINVOICE);
		
		List<TraacsInfo> traacInfoList = tcsReq.getTraacInfoRQ().getTraacDetails();
		List<HotelJson> hotels = new ArrayList<HotelJson>();
		for(TraacsInfo traacInfo:traacInfoList){
			if(traacInfo.getHotels() != null && !traacInfo.getHotels().isEmpty()){
				for(Hotel hotel : traacInfo.getHotels()){
					HotelJson aJson = new HotelJson();
					HotelJsonMaster hotelDetails = new HotelJsonMaster();
					
					hotelDetails.setSTR_ISSUE_DATE(autoInvoice);
					hotelDetails.setSTR_VOUCHER_NO(hotel.getVoucherNo());
					
					if(hotel.getIssueDt() != null && hotel.getIssueDt().contains("-")){
						hotelDetails.setSTR_ISSUE_DATE(StandAloneRequestBuilderUtil.dateFormatter(hotel.getIssueDt()));
					}else{
						hotelDetails.setSTR_ISSUE_DATE(hotel.getIssueDt());
					}
					hotelDetails.setSTR_SUPPLIER_CODE(hotel.getSupplierCode());
					hotelDetails.setSTR_COUNTRY(hotel.getCountry());
					hotelDetails.setSTR_CITY(hotel.getCity());
					hotelDetails.setSTR_HOTEL_NAME(hotel.getHotelName());
					hotelDetails.setSTR_MEALS_PLAN(hotel.getMealPlan());
					hotelDetails.setSTR_STAFF(hotel.getStaff());
					
					if(hotel.getStaffEmail() != null){
						hotelDetails.setSTR_STAFF_EMAIL_ID(hotel.getStaffEmail());
					}else{
						hotelDetails.setSTR_STAFF_EMAIL_ID("website@flyin.com");
					}
					hotelDetails.setSTR_GUESTS(hotel.getGuests());
					hotelDetails.setSTR_ADDITIONAL_GUESTS(hotel.getAdditionalGuests());
					hotelDetails.setSTR_SUPPLIER_CONF_NUMBER(hotel.getSupplierConfirmationNo());
					hotelDetails.setSTR_HOTEL_CONF_NUMBER(hotel.getHotelConfirmationNo());
					hotelDetails.setSTR_ORIGIN_COUNTRY(hotel.getOriginCountry());
					hotelDetails.setSTR_REMARKS(hotel.getRemarks());
					hotelDetails.setSTR_ROOM_TYPE(hotel.getRoomType());
					hotelDetails.setSTR_PNR_NO(hotel.getHotelConfirmationNo());
					hotelDetails.setSTR_LPO_NO(hotel.getLpo());
					hotelDetails.setSTR_CUSTOMER_REF_NO(hotel.getCustomerRefNo());
					hotelDetails.setSTR_PRODUCT(ETRCSConstants.CON_HOTEL_PRODUCT);
					
					List<RoomDetailsJson> json_details = new ArrayList<RoomDetailsJson>();
					if(hotel.getRoomDetails() != null){
					  for(RoomDetails aRoom: hotel.getRoomDetails()){
						  RoomDetailsJson room = new RoomDetailsJson();
						  room.setINT_ADULT(aRoom.getNoOfAdults());
						  room.setINT_CHILD(aRoom.getNoOfChilds());
						  room.setINT_NO_OF_ROOMS(hotel.getNoOfRooms());
						  room.setINT_NO_OF_NIGHTS(hotel.getNoOfNights());
						  if(aRoom.getCheckInDt() != null && aRoom.getCheckInDt().contains("-")){
							  room.setSTR_CHECK_IN_DATE(StandAloneRequestBuilderUtil.dateFormatter(aRoom.getCheckInDt()));
						  }else{
							  room.setSTR_CHECK_IN_DATE(aRoom.getCheckInDt());
						  }
						  if(aRoom.getCheckOutDt() != null && aRoom.getCheckOutDt().contains("-")){
							  room.setSTR_CHECK_OUT_DATE(StandAloneRequestBuilderUtil.dateFormatter(aRoom.getCheckOutDt()));
						  }else{
							  room.setSTR_CHECK_OUT_DATE(aRoom.getCheckOutDt());
						  }
						  room.setSTR_GUESTS(aRoom.getGuests());
						  room.setSTR_MEALS_PLAN(aRoom.getMealPlan());
						  room.setSTR_ROOM_TYPE(aRoom.getRoomType());
						  room.setSTR_TRAVELER_ID(aRoom.getTravelerId());
						 
						  room.setDBL_SELLING_CUR_SUPPLIER_AMOUNT(multiplyValues(aRoom.getSellingCurrSupplierAmt(),hotel.getPurchaseRoe()));
						  room.setDBL_SELLING_CUR_SERVICE_FEE(multiplyValues(aRoom.getSellingCurrServiceFee(),hotel.getPurchaseRoe()));
						  room.setDBL_SELLING_CUR_EXTRA_EARNING(multiplyValues(aRoom.getSellingCurrExtraEarning(),hotel.getPurchaseRoe()));
						  room.setDBL_SELLING_CUR_DISCOUNT(multiplyValues(aRoom.getSellingCurrDiscount(),hotel.getPurchaseRoe()));
						  room.setDBL_SELLING_CUR_PRICE(multiplyValues(aRoom.getSellingCurrPrice(),hotel.getPurchaseRoe()));
						  room.setDBL_SELLING_CUR_RATE_PER_NIGHT(multiplyValues(aRoom.getSellingCurrRatePerNight(),hotel.getPurchaseRoe()));
						  room.setDBL_SELLING_CUR_TAX_PER_NIGHT(multiplyValues(aRoom.getSellingCurrTaxPerNight(),hotel.getPurchaseRoe()));
						  room.setDBL_SELLING_CUR_SUPPLIER_AMOUNT(multiplyValues(aRoom.getSellingCurrSupplierAmt(),hotel.getPurchaseRoe()));
						  room.setDBL_BASE_CUR_SUPPLIER_AMOUNT(aRoom.getBaseCurrSupplierAmt());
						  room.setDBL_BASE_CUR_SERVICE_FEE(aRoom.getBaseCurrServiceFee());
						  room.setDBL_BASE_CUR_EXTRA_EARNING(aRoom.getBaseCurrExtraEarning());
						  room.setDBL_BASE_CUR_DISCOUNT(aRoom.getBaseCurrDiscount());
						  room.setDBL_BASE_CUR_PRICE(aRoom.getBaseCurrPrice());
						  room.setDBL_BASE_CUR_RATE_PER_NIGHT(aRoom.getBaseCurrRatePerNight());
						  room.setDBL_BASE_CUR_TAX_PER_NIGHT(aRoom.getBaseCurrTaxPerNight());
						  room.setDBL_BASE_CUR_SUPPLIER_AMOUNT(aRoom.getBaseCurrSupplierAmt());
						  
						  json_details.add(room);
					  }
					}
				
					aJson.setJson_master(hotelDetails);
					aJson.setJson_details(json_details);
					aJson.setStr_authentication_key(auth);
					hotels.add(aJson);
				}
			}
			
		}
		return hotels;
	}
	
	public static Double multiplyValues(Double in1, Double in2) {
		Double value = null;
		if(in1 != null && in2 != null){
			value = in1 * in2;
		}
		return value;
	}

}
