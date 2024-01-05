/**
 * 
 */
package com.ebreez.etrcs.traacs.processor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.runtime.api.component.execution.ExecutableComponent;
import org.mule.runtime.api.event.Event;
import org.mule.runtime.api.event.EventContext;
import org.mule.runtime.api.message.Message;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.ebreez.etrcs.constants.ETRCSConstants;
import com.ebreez.etrcs.request.vo.TraacsInfoRequest;
import com.ebreez.etrcs.traacs.request.vo.MasterJson;
import com.ebreez.etrcs.traacs.vo.CarDetailsJson;
import com.ebreez.etrcs.traacs.vo.CarJsonMaster;
import com.ebreez.etrcs.traacs.vo.HotelJsonMaster;
import com.ebreez.etrcs.traacs.vo.OtherJsonMaster;
import com.ebreez.etrcs.traacs.vo.RoomDetailsJson;
import com.ebreez.etrcs.traacs.vo.TicketJsonMaster;
import com.ebreez.etrcs.vo.Flight;
import com.ebreez.etrcs.vo.Other;


/**
 * @author Rakesh K
 *
 */
public abstract class BuildSecondaryTraacsRequest implements ExecutableComponent {
	
	/** logger used by this class. */
	private static Log logger = LogFactory.getLog(ETRCSConstants.LOGGER_NAME);


	@SuppressWarnings({ "unchecked", "deprecation" })
	public Object onCall(EventContext eventContext) throws Exception {

		Message msg = ((Event) eventContext).getMessage();
		String secServ = ((Event) msg).getVariables().get(ETRCSConstants.TRAACS_SECONDARY_DOMAIN).getValue().toString();
		MasterJson payloadJson = (MasterJson) msg.getPayload().getValue();
		MasterJson traacs = (MasterJson) deepClone(payloadJson);
		String fphScndSrvc = ((Event) msg).getVariables().get(ETRCSConstants.FPH_SECONDARY_DOMAIN).getValue().toString();
		String fcBooking = ((Event) msg).getVariables().get(ETRCSConstants.FC_BOOKING_SECONDARY_DOMAIN).getValue().toString();
		
		if(null != secServ){
			
			Map<String, String> suppllierIdsMap = (Map<String, String>) ((Event) msg).getVariables().get(ETRCSConstants.VAR_SUPPLIER_DATA_MAP).getValue();
			Map<String, String> properties = (Map<String, String>) ((Event) eventContext).getVariables().get(ETRCSConstants.BEAN_ETRCS_APP_PROPERTIES).getValue();
			
			String companyId = null;
			String companyType = null;
			Double vatPercentage = 0.0;
			Double vatPercentageFlt = 0.0;
			Double vatPercentageHtl = 0.0;
			Double paymentExcRate = 1.0;
			TraacsInfoRequest tcsReq = (TraacsInfoRequest)((Event) msg).getVariables().get(ETRCSConstants.VAR_UPDATE_REQUEST).getValue();
			if(null != tcsReq &&
					null != tcsReq.getTraacInfoRQ() &&
					null != tcsReq.getTraacInfoRQ().getTraacDetails() && !tcsReq.getTraacInfoRQ().getTraacDetails().isEmpty() &&
					null != tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getMaster()){
						
				if(null != tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getMaster().getVatPercentage()){
					if(tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getMaster().getVatPercentage().contains("_")){
						String[] str = tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getMaster().getVatPercentage().split("_");
						if(null != str && str.length > 1){
							vatPercentageFlt = Double.valueOf(str[0]);
							vatPercentageHtl = Double.valueOf(str[1]);
						}					
					}else{
						vatPercentage = Double.valueOf(tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getMaster().getVatPercentage());
					}
				}
				
				if(null != tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getMaster().getCompanyId()){
					companyId = tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getMaster().getCompanyId();
					companyType = suppllierIdsMap.get(ETRCSConstants.VAR_COMP_TYPE_PREFIX + companyId);
				}
			}
			
			if(null != traacs && null != traacs.getStr_authentication_key()){
				if(null != secServ && secServ.equalsIgnoreCase("EGYPT")){
					traacs.getStr_authentication_key().setSTR_USER_NAME(properties.get(ETRCSConstants.TRAACS_USERNAME_EGYPT));
					traacs.getStr_authentication_key().setSTR_PASSWORD(properties.get(ETRCSConstants.TRAACS_PASSWORD_EGYPT));
				}else{
					traacs.getStr_authentication_key().setSTR_USER_NAME(properties.get(ETRCSConstants.TRAACS_USERNAME_GLOBAL));
					traacs.getStr_authentication_key().setSTR_PASSWORD(properties.get(ETRCSConstants.TRAACS_PASSWORD_GLOBAL));
				}
			}
			
			if(null != traacs.getJson_master()){
				
				if(null != secServ && secServ.equalsIgnoreCase("EGYPT")){
					
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
				
				String tempNarration = null;
				String[] narration = traacs.getJson_master().getSTR_NARRATION().split("\\|");
				for(int i = 0; i < narration.length; i++){
					if(!narration[i].toLowerCase().trim().contains("coupon") && !narration[i].toLowerCase().trim().contains("creditcardsales") && 
							!narration[i].toLowerCase().trim().contains("banktransfer")){
						if(null != tempNarration){
							tempNarration = tempNarration.concat(" | ").concat(narration[i].trim());
						}else{
							tempNarration = narration[i].trim();
						}
					}
				}
				traacs.getJson_master().setSTR_NARRATION(tempNarration);
				
				String accCode = traacs.getJson_master().getSTR_ACCOUNT_CODE();
				String curr = traacs.getJson_master().getSTR_SELLING_CUR_CODE();
				
				if(null != accCode && null != curr){
					if(accCode.equalsIgnoreCase("STC")){
						if(null != suppllierIdsMap.get(accCode))
							traacs.getJson_master().setSTR_ACCOUNT_CODE(suppllierIdsMap.get(accCode));
					}else{
						if(null != companyType && companyType.equals("3")){
							traacs.getJson_master().setSTR_ACCOUNT_CODE(suppllierIdsMap.get(ETRCSConstants.VAR_SEC_ACCOUNT_CODE_PREFIX + companyId));
						}else if(null != suppllierIdsMap.get(accCode+curr)){
							traacs.getJson_master().setSTR_ACCOUNT_CODE(suppllierIdsMap.get(accCode+curr));
						}
					}
				}
				
				traacs.getJson_master().setSTR_POS_ID(null);
				traacs.getJson_master().setSTR_PAY_ID(null);
				traacs.getJson_master().setSTR_CC_NO(null);
				traacs.getJson_master().setDBL_COUPON_DISCOUNT(null);
			}
			
			if(null != traacs.getJson_Ticket() && !traacs.getJson_Ticket().isEmpty()){
				for(TicketJsonMaster tkt : traacs.getJson_Ticket()){
					
					tkt.setDBL_SELLING_CUR_SERVICE_FEE(null);
					tkt.setDBL_SELLING_CUR_EXTRA_EARNING(null);
					tkt.setDBL_SELLING_CUR_AGENCY_CHARGE(null);
					tkt.setDBL_SELLING_CUR_DISCOUNT(null);
					tkt.setDBL_SELLING_CUR_CREDIT_CARD_CHARGES(null);
					
					Double vatPer = 0.0;
					if(null != tcsReq &&
							null != tcsReq.getTraacInfoRQ() &&
							null != tcsReq.getTraacInfoRQ().getTraacDetails() && !tcsReq.getTraacInfoRQ().getTraacDetails().isEmpty() &&
							null != tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getFlights() && !tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getFlights().isEmpty()){
						
						if(null != tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getFlights()){
							
							Flight flt = tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getFlights().stream().filter(f -> 
									null != f.getTicketNo() && null != tkt.getSTR_TICKET_NO() && f.getTicketNo().equals(tkt.getSTR_TICKET_NO())).findAny().orElse(null);
							
							if(null != flt){
								paymentExcRate = Double.valueOf(flt.getPayExRate());
							}
						}
						
						if(null != tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getFlights().get(0).getVatPercentage()){
							vatPer = tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getFlights().get(0).getVatPercentage();
						}else{
							vatPer = vatPercentageFlt > 0 ? vatPercentageFlt : vatPercentage;						
						}
					}
					
					if(null != traacs.getJson_master() && null != traacs.getJson_master().getSTR_REFUND_DATE()){
						
						Double bf = tkt.getDBL_PURCHASE_CUR_PUBLISHED_FARE() != null ? tkt.getDBL_PURCHASE_CUR_PUBLISHED_FARE() : 0.0;
						Double tx = tkt.getDBL_PURCHASE_CUR_TOTAL_TAX() != null ? tkt.getDBL_PURCHASE_CUR_TOTAL_TAX() : 0.0;
						Double iVat = tkt.getDBL_PURCHASE_CUR_INPUT_VAT() != null ? tkt.getDBL_PURCHASE_CUR_INPUT_VAT() : 0.0;
						
						tkt.setDBL_SELLING_CUR_CLIENT_NET(setScaleNConvertToDouble((bf + tx - iVat) * paymentExcRate));
						
						Double oVat= (((bf + tx - iVat) * paymentExcRate) * vatPer ) / 100;
						tkt.setDBL_SELLING_CUR_OUTPUT_VAT(setScaleNConvertToDouble(oVat));
					}else{
						Double bf = tkt.getDBL_PURCHASE_CUR_PUBLISHED_FARE() != null ? tkt.getDBL_PURCHASE_CUR_PUBLISHED_FARE() : 0.0;
						Double tx = tkt.getDBL_PURCHASE_CUR_TOTAL_TAX() != null ? tkt.getDBL_PURCHASE_CUR_TOTAL_TAX() : 0.0;
						Double iVat = tkt.getDBL_PURCHASE_CUR_INPUT_VAT() != null ? tkt.getDBL_PURCHASE_CUR_INPUT_VAT() : 0.0;
						tkt.setDBL_SELLING_CUR_PRICE(setScaleNConvertToDouble((bf + tx - iVat) * paymentExcRate));
						
						Double oVat= (((bf + tx - iVat) * paymentExcRate) * vatPer ) / 100;
						tkt.setDBL_SELLING_CUR_OUTPUT_VAT(setScaleNConvertToDouble(oVat));
					}
					
					if((null != tkt.getSTR_TICKETING_AGENCY_OFFICE_ID() && tkt.getSTR_TICKETING_AGENCY_OFFICE_ID().startsWith("RUH"))){
						tkt.setSTR_TYPE("GDS");
					}else if((null != tkt.getSTR_TICKETING_AGENCY_OFFICE_ID() && 
							(tkt.getSTR_TICKETING_AGENCY_OFFICE_ID().startsWith("CAI") || tkt.getSTR_TICKETING_AGENCY_OFFICE_ID().startsWith("ALY")))){
						tkt.setSTR_TYPE("GDS");
					}else if(null != tkt.getSTR_TYPE() && tkt.getSTR_TYPE().equalsIgnoreCase("LCC")){
						tkt.setSTR_TYPE(ETRCSConstants.HOTEL_STR_TYPE);
					}
				}
			}
			
			if(null != traacs.getJson_Hotel() && !traacs.getJson_Hotel().isEmpty()){
				for(HotelJsonMaster htl : traacs.getJson_Hotel()){
					
					htl.setDBL_SELLING_CUR_SERVICE_FEE(null);
					htl.setDBL_SELLING_CUR_EXTRA_EARNING(null);
					htl.setDBL_SELLING_CUR_AGENCY_CHARGE(null);
					htl.setDBL_SELLING_CUR_DISCOUNT(null);
					htl.setDBL_SELLING_CUR_CC_CHARGES(null);
					
					Double vatPer = 0.0;
					if(null != tcsReq &&
							null != tcsReq.getTraacInfoRQ() &&
							null != tcsReq.getTraacInfoRQ().getTraacDetails() && !tcsReq.getTraacInfoRQ().getTraacDetails().isEmpty() &&
							null != tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getHotels() && !tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getHotels().isEmpty()){
						
						if(null != tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getHotels().get(0).getPayExRate()){
							paymentExcRate = Double.valueOf(tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getHotels().get(0).getPayExRate());
						}
						
						if(null != tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getHotels().get(0).getVatPercentage()){
							vatPer = tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getHotels().get(0).getVatPercentage();
						}else{
							vatPer = vatPercentageHtl > 0 ? vatPercentageHtl : vatPercentage;
						}
					}
					
					if(null != traacs.getJson_master() && null != traacs.getJson_master().getSTR_REFUND_DATE()){
						
						Double tot = htl.getDBL_PURCHASE_CUR_SUPPLIER_NET() != null ? htl.getDBL_PURCHASE_CUR_SUPPLIER_NET() : 0.0;
						Double vat = htl.getDBL_PURCHASE_CUR_INPUT_VAT() != null ? htl.getDBL_PURCHASE_CUR_INPUT_VAT() : 0.0;
						htl.setDBL_SELLING_CUR_CLIENT_NET(setScaleNConvertToDouble((tot - vat) * paymentExcRate));
						
						Double oVat= (((tot - vat) * paymentExcRate) * vatPer ) / 100;
						htl.setDBL_SELLING_CUR_OUTPUT_VAT(setScaleNConvertToDouble(oVat));
						
					}else{
						Double tot = htl.getDBL_PURCHASE_CUR_SUPPLIER_AMOUNT() != null ? htl.getDBL_PURCHASE_CUR_SUPPLIER_AMOUNT() : 0.0;
						Double vat = htl.getDBL_PURCHASE_CUR_INPUT_VAT() != null ? htl.getDBL_PURCHASE_CUR_INPUT_VAT() : 0.0;
						htl.setDBL_SELLING_CUR_PRICE(setScaleNConvertToDouble((tot - vat) * paymentExcRate));
						
						Double oVat = (((tot - vat) * paymentExcRate) * vatPer ) / 100;
						htl.setDBL_SELLING_CUR_OUTPUT_VAT(setScaleNConvertToDouble(oVat));
					}
					
					if(null != secServ && secServ.equalsIgnoreCase("EGYPT")){
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
					
					if(null != htl.getRoom_Details()){
						for(RoomDetailsJson room : htl.getRoom_Details()){
							
							room.setDBL_SELLING_CUR_SERVICE_FEE(null);
							room.setDBL_SELLING_CUR_EXTRA_EARNING(null);
							room.setDBL_SELLING_CUR_DISCOUNT(null);
							room.setDBL_SELLING_CUR_CC_CHARGES(null);
							
							Double tot = room.getDBL_PURCHASE_CUR_SUPPLIER_AMOUNT() != null ? room.getDBL_PURCHASE_CUR_SUPPLIER_AMOUNT() : 0.0;
							Double vat = room.getDBL_PURCHASE_CUR_INPUT_VAT() != null ? room.getDBL_PURCHASE_CUR_INPUT_VAT() : 0.0;
							room.setDBL_SELLING_CUR_PRICE(setScaleNConvertToDouble((tot - vat) * paymentExcRate));
							
							Double oVat= (((tot - vat) * paymentExcRate) * vatPer ) / 100;
							room.setDBL_SELLING_CUR_OUTPUT_VAT(setScaleNConvertToDouble(oVat));
						}
					}
				}
			}

			if(null != traacs.getJson_Car() && !traacs.getJson_Car().isEmpty()){
				for(CarJsonMaster car : traacs.getJson_Car()){
					
					car.setDBL_SELLING_CUR_SERVICE_FEE(null);
					car.setDBL_SELLING_CUR_EXTRA_EARNING(null);
					car.setDBL_SELLING_CUR_AGENCY_CHARGE(null);
					car.setDBL_SELLING_CUR_DISCOUNT(null);
					car.setDBL_SELLING_CUR_CC_CHARGES(null);
					
					if(null != tcsReq &&
							null != tcsReq.getTraacInfoRQ() &&
							null != tcsReq.getTraacInfoRQ().getTraacDetails() && !tcsReq.getTraacInfoRQ().getTraacDetails().isEmpty() &&
							null != tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getCars() && !tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getCars().isEmpty()){
						
						if(null != tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getCars().get(0).getPayExRate())
							paymentExcRate = Double.valueOf(tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getCars().get(0).getPayExRate());
					}
					
					if(null != traacs.getJson_master() && null != traacs.getJson_master().getSTR_REFUND_DATE()){
						
						Double tot = car.getDBL_PURCHASE_CUR_SUPPLIER_NET() != null ? car.getDBL_PURCHASE_CUR_SUPPLIER_NET() : 0.0;
						Double vat = car.getDBL_PURCHASE_CUR_INPUT_VAT() != null ? car.getDBL_PURCHASE_CUR_INPUT_VAT() : 0.0;
						car.setDBL_SELLING_CUR_CLIENT_NET(setScaleNConvertToDouble((tot - vat) * paymentExcRate));
						
						Double oVat= ((tot * paymentExcRate) * vatPercentage ) / 100;
						car.setDBL_SELLING_CUR_OUTPUT_VAT(setScaleNConvertToDouble(oVat));
						
					}else{
						Double tot = car.getDBL_PURCHASE_CUR_SUPPLIER_AMOUNT() != null ? car.getDBL_PURCHASE_CUR_SUPPLIER_AMOUNT() : 0.0;
						Double vat = car.getDBL_PURCHASE_CUR_INPUT_VAT() != null ? car.getDBL_PURCHASE_CUR_INPUT_VAT() : 0.0;
						car.setDBL_SELLING_CUR_PRICE(setScaleNConvertToDouble((tot - vat) * paymentExcRate));
						
						Double oVat= (((tot - vat) * paymentExcRate) * vatPercentage ) / 100;
						car.setDBL_SELLING_CUR_OUTPUT_VAT(setScaleNConvertToDouble(oVat));
					}
					
					if(null != car.getCar_Details()){
						for(CarDetailsJson carDet : car.getCar_Details()){
							
							carDet.setDBL_SELLING_CUR_SERVICE_FEE(null);
							carDet.setDBL_SELLING_CUR_EXTRA_EARNING(null);
							carDet.setDBL_SELLING_CUR_DISCOUNT(null);
							carDet.setDBL_SELLING_CUR_CC_CHARGES(null);
							
							Double tot = carDet.getDBL_PURCHASE_CUR_SUPPLIER_AMOUNT() != null ? carDet.getDBL_PURCHASE_CUR_SUPPLIER_AMOUNT() : 0.0;
							Double vat = carDet.getDBL_PURCHASE_CUR_INPUT_VAT() != null ? carDet.getDBL_PURCHASE_CUR_INPUT_VAT() : 0.0;
							carDet.setDBL_SELLING_CUR_PRICE(setScaleNConvertToDouble((tot - vat) * paymentExcRate));
							
							Double oVat= (((tot - vat) * paymentExcRate) * vatPercentage ) / 100;
							carDet.setDBL_SELLING_CUR_OUTPUT_VAT(setScaleNConvertToDouble(oVat));
						}
					}
				}
			}

			if(null != traacs.getJson_Other() && !traacs.getJson_Other().isEmpty()){
				for(OtherJsonMaster oth : traacs.getJson_Other()){
					Double oVat = 0.0;
					Double suppToAddonExcRate = 1.0;
					oth.setDBL_SELLING_CUR_SERVICE_FEE(null);
					oth.setDBL_SELLING_CUR_EXTRA_EARNING(null);
					oth.setDBL_SELLING_CUR_AGENCY_CHARGE(null);
					oth.setDBL_SELLING_CUR_DISCOUNT(null);

					if(null != tcsReq &&
							null != tcsReq.getTraacInfoRQ() &&
							null != tcsReq.getTraacInfoRQ().getTraacDetails() && !tcsReq.getTraacInfoRQ().getTraacDetails().isEmpty() &&
							null != tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getOthers() && !tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getOthers().isEmpty()){
						
						for(Other appOth : tcsReq.getTraacInfoRQ().getTraacDetails().get(0).getOthers()){
							
							if((null != appOth.getSupplierConfNo() && null != oth.getSTR_SUPPLIER_CONF_NUMBER()) &&
									(appOth.getSupplierConfNo().equals(oth.getSTR_SUPPLIER_CONF_NUMBER()))){
								
								if(null != appOth.getPayExRate())
									suppToAddonExcRate = Double.valueOf(appOth.getPayExRate());
								
								if(null != appOth.getVatPercentage())
									vatPercentage = Double.valueOf(appOth.getVatPercentage());
								
								if(null != appOth.getSellingCurrOutputVAT())
									oVat = appOth.getSellingCurrOutputVAT();
								
								if(null != traacs.getJson_master() && null != traacs.getJson_master().getSTR_REFUND_DATE()){
									Double tot = oth.getDBL_PURCHASE_CUR_SUPPLIER_NET() != null ? oth.getDBL_PURCHASE_CUR_SUPPLIER_NET() : 0.0;
									Double vat = oth.getDBL_PURCHASE_CUR_INPUT_VAT() != null ? oth.getDBL_PURCHASE_CUR_INPUT_VAT() : 0.0;
									
									if(null != appOth.getProduct() && appOth.getProduct().equalsIgnoreCase("PACKAGE")){
										oth.setDBL_SELLING_CUR_CLIENT_NET(setScaleNConvertToDouble((tot - vat) * suppToAddonExcRate));
										
										if(vatPercentage > Double.valueOf(0)){
											oVat = ((setScaleNConvertToDouble((tot - vat) * suppToAddonExcRate)) * vatPercentage ) / 100;
											oth.setDBL_SELLING_CUR_OUTPUT_VAT(setScaleNConvertToDouble(oVat));
										}else{
											oth.setDBL_SELLING_CUR_OUTPUT_VAT(0.0);
										}
									}else{
										//For Add-On suppToAddonExcRate
										oth.setDBL_SELLING_CUR_CLIENT_NET(setScaleNConvertToDouble(((tot - vat) / suppToAddonExcRate) * paymentExcRate));
										
										if(oVat > Double.valueOf(0)){
											oVat = ((setScaleNConvertToDouble(((tot - vat) / suppToAddonExcRate) * paymentExcRate)) * vatPercentage ) / 100;
											oth.setDBL_SELLING_CUR_OUTPUT_VAT(setScaleNConvertToDouble(oVat));
										}else{
											oth.setDBL_SELLING_CUR_OUTPUT_VAT(0.0);
										}
									}
								}else{
									Double tot = oth.getDBL_PURCHASE_CUR_SUPPLIER_AMOUNT() != null ? oth.getDBL_PURCHASE_CUR_SUPPLIER_AMOUNT() : 0.0;
									Double vat = oth.getDBL_PURCHASE_CUR_INPUT_VAT() != null ? oth.getDBL_PURCHASE_CUR_INPUT_VAT() : 0.0;
									
									if(null != appOth.getProduct() && appOth.getProduct().equalsIgnoreCase("PACKAGE")){
										oth.setDBL_SELLING_CUR_PRICE(setScaleNConvertToDouble((tot - vat) * suppToAddonExcRate));
										
										if(vatPercentage > Double.valueOf(0)){
											oVat = (setScaleNConvertToDouble((tot - vat) * suppToAddonExcRate) * vatPercentage ) / 100;
											oth.setDBL_SELLING_CUR_OUTPUT_VAT(setScaleNConvertToDouble(oVat));
										}else{
											oth.setDBL_SELLING_CUR_OUTPUT_VAT(0.0);
										}
									}else{
										oth.setDBL_SELLING_CUR_PRICE(setScaleNConvertToDouble(((tot - vat) / suppToAddonExcRate) * paymentExcRate));
										
										if(oVat > Double.valueOf(0)){
											oVat = (setScaleNConvertToDouble(((tot - vat) / suppToAddonExcRate) * paymentExcRate) * vatPercentage ) / 100;
											oth.setDBL_SELLING_CUR_OUTPUT_VAT(setScaleNConvertToDouble(oVat));
										}else{
											oth.setDBL_SELLING_CUR_OUTPUT_VAT(0.0);
										}
									}
								}
								
								if(null != secServ && secServ.equalsIgnoreCase("EGYPT")){
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
				}
			}
			
			if(null != fphScndSrvc){
				if(fphScndSrvc.equals("HOTEL")){
					traacs.setJson_Ticket(null);
				}else if(fphScndSrvc.equals("FLIGHT")){
					traacs.setJson_Hotel(null);
				} 
			}
			
			java.util.List<String> removeObj = new ArrayList<String>();
			if(null != fcBooking){
				if(null != traacs.getJson_Ticket() && !traacs.getJson_Ticket().isEmpty()){
					for(TicketJsonMaster tkt : traacs.getJson_Ticket()){
						String officeID = properties.get(ETRCSConstants.EGYPT_OFFICE_ID);
						String[] officeIDList = officeID.split("\\|");
						if(secServ.equalsIgnoreCase("EGYPT") && 
								!(null != tkt.getSTR_TICKETING_AGENCY_OFFICE_ID() && Arrays.asList(officeIDList).contains(tkt.getSTR_TICKETING_AGENCY_OFFICE_ID()))){
							
							removeObj.add(tkt.getSTR_TICKET_NO());
							
						}else if(secServ.equalsIgnoreCase("GLOBAL") && 
								(null != tkt.getSTR_TICKETING_AGENCY_OFFICE_ID() && Arrays.asList(officeIDList).contains(tkt.getSTR_TICKETING_AGENCY_OFFICE_ID()))){
							
							removeObj.add(tkt.getSTR_TICKET_NO());
						}
					}
					
					for(String obj:removeObj){
						java.util.List<TicketJsonMaster> tempList = traacs.getJson_Ticket().stream().filter(TicketJsonMaster -> TicketJsonMaster.getSTR_TICKET_NO().equals(obj)).collect(Collectors.toList());
						if(!CollectionUtils.isEmpty(tempList)){
							TicketJsonMaster ticketJsonMaster = tempList.get(0);
								traacs.getJson_Ticket().remove(ticketJsonMaster);
						}
					}
				}
			}
			
			traacs.setJson_Sharing(null);
			
		}
		
		return traacs;
	}
	
	/** Convert Double to Bigdecimal
	 * Set Scale in integer value
	 * Convert Bigdecimal back to Double again
	 * @param doubleVal
	 * @param scalValue
	 * @return
	 */
	public static Double setScaleNConvertToDouble(Double doubleVal){
		try {
			Integer scalValue = 2;
			if(doubleVal != null && scalValue != null){
				return new BigDecimal(doubleVal).setScale(scalValue,RoundingMode.HALF_UP).doubleValue();
			}
		}catch (Exception e){
			logger.error("Exception occured in StandaloneReqBuilder setScaleNConvertToDouble due to" + e, e);
		}
		return doubleVal;
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
