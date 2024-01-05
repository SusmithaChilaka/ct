package com.ebreez.etrcs.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.runtime.api.event.Event;
import org.mule.runtime.api.event.EventContext;
import org.mule.runtime.api.message.Message;
import org.mule.runtime.api.component.execution.ExecutableComponent;


import com.ebreez.etrcs.constants.ETRCSConstants;
import com.ebreez.etrcs.request.vo.TraacsInfo;
import com.ebreez.etrcs.request.vo.TraacsInfoRequest;
import com.ebreez.etrcs.vo.Flight;

public abstract class BuildMulticityNFCRequestProcessor implements ExecutableComponent {
	/** logger used by this class */
    private static Log logger = LogFactory.getLog(ETRCSConstants.LOGGER_NAME);
   
    public Object onCall(EventContext eventContext) throws Exception {
        try {
            Message message = ((Event) eventContext).getMessage();
            Object payload = message.getPayload().getValue();

            if (payload instanceof TraacsInfoRequest) {
                TraacsInfoRequest traacsRQ = (TraacsInfoRequest) payload;

                List<TraacsInfo> traacInfoList = traacsRQ.getTraacInfoRQ().getTraacDetails();
                for (TraacsInfo aTraacInfo : traacInfoList) {

                    List<Flight> flights = aTraacInfo.getFlights();
                    List<Flight> updatedFlights = new ArrayList<>();
                    if (flights != null && !flights.isEmpty()) {
                        for (Flight aTicket : flights) {
                            String ticketNo = aTicket.getTicketNo();

                            if (ticketNo != null && ticketNo.contains(ETRCSConstants.PIPE_CONSTANT)) {
                                String[] tickNoList = ticketNo.split(ETRCSConstants.ESCAPE_CONSTANT + ETRCSConstants.PIPE_CONSTANT);

                                for (String s : tickNoList) {
                                    updatedFlights.add(getModifiedFlightInfo(s, aTicket, tickNoList.length));
                                }

                                logger.info("Fare combination ticket no is: " + ticketNo);
                            } else {
                                updatedFlights.add(aTicket);
                            }
                        }

                        aTraacInfo.setFlights(updatedFlights);
                    }
                }

                return traacsRQ;
            } 
                  else {
                // Handle the case where the payload is not a TraacsInfoRequest
                return null;
            }
        } catch (Exception e) {
            logger.error("An error occurred in the onCall method", e);
            throw e; // Rethrow the exception to handle it at a higher level if needed.
        }
    }
         




	
	private Flight getModifiedFlightInfo(String ticketNo, Flight aTicket, int totalNoofTickets){
		Flight ticket = new Flight();
			
		if(aTicket.getPurchaseCurrPubFare()!=null){
			ticket.setPurchaseCurrPubFare(aTicket.getPurchaseCurrPubFare()/totalNoofTickets);
		}
		if(aTicket.getPurchaseCurrTotMarketFare()!=null){
			ticket.setPurchaseCurrTotMarketFare(aTicket.getPurchaseCurrTotMarketFare()/totalNoofTickets);
		}
		if(aTicket.getPurchaseCurrTotTax()!=null){
			ticket.setPurchaseCurrTotTax(aTicket.getPurchaseCurrTotTax()/totalNoofTickets);
		}
		if(aTicket.getPurchaseCurrStdComm()!=null){
			ticket.setPurchaseCurrStdComm(aTicket.getPurchaseCurrStdComm()/totalNoofTickets);
		}
		if(aTicket.getPurchaseCurrSupplierAmt()!=null){
			ticket.setPurchaseCurrSupplierAmt(aTicket.getPurchaseCurrSupplierAmt()/totalNoofTickets);
		}
		if(aTicket.getPurchaseCurrSupplierNet()!=null){
			ticket.setPurchaseCurrSupplierNet(aTicket.getPurchaseCurrSupplierNet()/totalNoofTickets);
		}
		if(aTicket.getSellingCurrSupplierFee()!=null){
			ticket.setSellingCurrSupplierFee(aTicket.getSellingCurrSupplierFee()/totalNoofTickets);
		}
		if(aTicket.getSellingCurrServiceFee()!=null){
			ticket.setSellingCurrServiceFee(aTicket.getSellingCurrServiceFee()/totalNoofTickets);
		}
		if(aTicket.getSellingCurrDiscount()!= null){
			ticket.setSellingCurrDiscount(aTicket.getSellingCurrDiscount()/totalNoofTickets);
		}
		if(aTicket.getSellingCurrAgencyCharges()!=null){
			ticket.setSellingCurrAgencyCharges(aTicket.getSellingCurrAgencyCharges()/totalNoofTickets);
		}
		if(aTicket.getSellingCurrCcCharges()!=null){
			ticket.setSellingCurrCcCharges(aTicket.getSellingCurrCcCharges()/totalNoofTickets);
		}
		if(aTicket.getSellingCurrPrice()!=null){
			ticket.setSellingCurrPrice(aTicket.getSellingCurrPrice()/totalNoofTickets);
		}
		if(aTicket.getSellingCurrClientNet()!=null){
			ticket.setSellingCurrClientNet(aTicket.getSellingCurrClientNet()/totalNoofTickets);
		}
		
		ticket.setAirlineCCode(aTicket.getAirlineCCode());
		ticket.setAirlineName(aTicket.getAirlineName());
		ticket.setAirlineNCode(aTicket.getAirlineNCode());
		ticket.setAirlineRefNo(aTicket.getAirlineRefNo());
		ticket.setBkngAgencyIATANo(aTicket.getBkngAgencyIATANo());
		ticket.setBkngAgencyIATANo(aTicket.getBkngAgencyIATANo());
		ticket.setBkngAgencyOfficeNo(aTicket.getBkngAgencyOfficeNo());
		ticket.setBkngStaffCCode(aTicket.getBkngStaffCCode());
		ticket.setBkngStaffEmail(aTicket.getBkngStaffEmail());
		ticket.setBkngStaffEmail(aTicket.getBkngStaffEmail());
		ticket.setTicketNo(aTicket.getTicketNo());
		ticket.setLastConjTicket(aTicket.getLastConjTicket());
		ticket.setCorpCCACode(aTicket.getCorpCCACode());
		ticket.setProduct(aTicket.getProduct());
		ticket.setCustomerCorpCCAccCode(aTicket.getCustomerCorpCCAccCode());
		ticket.setCustomerEmpNo(aTicket.getCustomerEmpNo());
		ticket.setCustomerRefNo(aTicket.getCustomerRefNo());
		ticket.setReturnDate(aTicket.getReturnDate());
		ticket.setFareBasis(aTicket.getFareBasis());
		ticket.setSupplierCode(aTicket.getSupplierCode());
		ticket.setGds(aTicket.getGds());
		ticket.setTravelDate(aTicket.getTravelDate());
		ticket.setLpo(aTicket.getLpo());
		ticket.setNoOfPax(aTicket.getNoOfPax());
		ticket.setPnrNo(aTicket.getPnrNo());
		ticket.setAirlinePnr(aTicket.getAirlinePnr());
		ticket.setPnrCurrentOwnerOfficeNo(aTicket.getPnrCurrentOwnerOfficeNo());
		ticket.setPnrCurrentOwnerOfficeNo(aTicket.getPnrCurrentOwnerOfficeNo());
		ticket.setPnrFirstOwnerOfficeNo(aTicket.getPnrFirstOwnerOfficeNo());
		ticket.setPnrFirstOwnerOfficeNo(aTicket.getPnrFirstOwnerOfficeNo());
		ticket.setRefundDate(aTicket.getRefundDate());
		ticket.setRefundStaffCharCode(aTicket.getRefundStaffCharCode());
		ticket.setRefundStaffEmailId(aTicket.getRefundStaffEmailId());
		ticket.setRefundStaffCode(aTicket.getRefundStaffCode());
		ticket.setRefundStatus(aTicket.getRefundStatus());
		ticket.setRegionCode(aTicket.getRegionCode());
		ticket.setTktRemarks(aTicket.getTktRemarks());
		ticket.setReportingDt(aTicket.getReportingDt());
		ticket.setReturnClass(aTicket.getReturnClass());
		ticket.setSector(aTicket.getSector());
		ticket.setStatus(aTicket.getStatus());
		ticket.setTicketingAgencyIATANo(aTicket.getTicketingAgencyIATANo());
		ticket.setTicketingAgencyOfficeNo(aTicket.getTicketingAgencyOfficeNo());
		ticket.setTicketingStaffCCode(aTicket.getTicketingStaffCCode());
		ticket.setTicketingStaffEmail(aTicket.getTicketingStaffEmail());
		ticket.setTicketingStaffCode(aTicket.getTicketingStaffCode());
		ticket.setTicketNo(aTicket.getTicketNo());
		ticket.setTicketType(aTicket.getTicketType());
		ticket.setTourCode(aTicket.getTourCode());
		ticket.setTravelerClass(aTicket.getTravelerClass());
		ticket.setTravelerId(aTicket.getTravelerId());
		ticket.setType(aTicket.getType());
		ticket.setAdditionalPax(aTicket.getAdditionalPax());
		ticket.setPaxName(aTicket.getPaxName());
		ticket.setPurchaseCurrCode(aTicket.getPurchaseCurrCode());
		ticket.setPaxId(aTicket.getPaxId());
		ticket.setPayExRate(aTicket.getPayExRate());
		ticket.setPaxType(aTicket.getPaxType());
		ticket.setTicketIssueDt(aTicket.getTicketIssueDt());
		ticket.setPurchaseCurrInputVAT(aTicket.getPurchaseCurrInputVAT());
		ticket.setSellingCurrOutputVAT(aTicket.getSellingCurrOutputVAT());
		ticket.setTicketNo(ticketNo);
		
		return ticket;
	}

}
