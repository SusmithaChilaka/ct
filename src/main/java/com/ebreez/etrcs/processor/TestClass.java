package com.ebreez.etrcs.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ebreez.etrcs.constants.ETRCSConstants;
import com.ebreez.etrcs.request.vo.TraacsInfo;
import com.ebreez.etrcs.request.vo.TraacsInfoRequest;
import com.ebreez.etrcs.vo.Flight;

public class TestClass {
	
	private static Log logger = LogFactory.getLog(ETRCSConstants.LOGGER_NAME);
	
	public TraacsInfoRequest demoMethod(TraacsInfoRequest traacsRQ) {
		
		List<TraacsInfo> traacInfoList = traacsRQ.getTraacInfoRQ().getTraacDetails();
        for(TraacsInfo aTraacInfo : traacInfoList){
            
            List<Flight> flights = aTraacInfo.getFlights();
            List<Flight> updatedFlights = new ArrayList<Flight>();
            if(flights!= null && !flights.isEmpty()){
                for(Flight aTicket : flights){
                    String ticketNo = aTicket.getTicketNo();
                        if(ticketNo != null && ticketNo.contains(ETRCSConstants.PIPE_CONSTANT)){
                               String[] tickNoList = ticketNo.split(ETRCSConstants.ESCAPE_CONSTANT+ETRCSConstants.PIPE_CONSTANT);
//                               for(String s: tickNoList){
//                                   updatedFlights.add(getModifiedFlightInfo(s, aTicket,tickNoList.length));
//                               }
                               logger.info("Fare combination ticket no is :"+ ticketNo);
                               System.out.println("Entered into the method "+ticketNo);
                           }else{
                               updatedFlights.add(aTicket);
                           }
                    }
                aTraacInfo.setFlights(updatedFlights);
            }
        }
       return traacsRQ;
	}
}