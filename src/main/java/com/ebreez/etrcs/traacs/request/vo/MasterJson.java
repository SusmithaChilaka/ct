/**
 * 
 */
package com.ebreez.etrcs.traacs.request.vo;

import java.io.Serializable;
import java.util.List;



import com.ebreez.etrcs.traacs.vo.Authentication;
import com.ebreez.etrcs.traacs.vo.CarJsonMaster;
import com.ebreez.etrcs.traacs.vo.HotelJsonMaster;
import com.ebreez.etrcs.traacs.vo.MasterDetailsJson;
import com.ebreez.etrcs.traacs.vo.OtherJsonMaster;
import com.ebreez.etrcs.traacs.vo.SharingJsonMaster;
import com.ebreez.etrcs.traacs.vo.TicketJsonMaster;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author Rakesh K
 *
 */

@JsonAutoDetect(
	    fieldVisibility = JsonAutoDetect.Visibility.ANY,
	    getterVisibility = JsonAutoDetect.Visibility.NONE,
	    setterVisibility = JsonAutoDetect.Visibility.NONE
	)
	@JsonSerialize
	@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.ALWAYS)
public class MasterJson implements Serializable {
	
	/**	 */
	private static final long serialVersionUID = 2342346544L;

	@JsonProperty("str_authentication_key")
	private Authentication str_authentication_key;

	@JsonProperty("json_master")
	private MasterDetailsJson json_master;

	@JsonProperty("json_Ticket")
	private List<TicketJsonMaster> json_Ticket;

	@JsonProperty("json_Hotel")
	private List<HotelJsonMaster> json_Hotel;

	@JsonProperty("json_Car")
	private List<CarJsonMaster> json_Car;

	@JsonProperty("json_Other")
	private List<OtherJsonMaster> json_Other;

	@JsonProperty("json_Sharing")
	private List<SharingJsonMaster> json_Sharing;
	

	public Authentication getStr_authentication_key() {
		return str_authentication_key;
	}

	public void setStr_authentication_key(Authentication str_authentication_key) {
		this.str_authentication_key = str_authentication_key;
	}

	public MasterDetailsJson getJson_master() {
		return json_master;
	}

	public void setJson_master(MasterDetailsJson json_master) {
		this.json_master = json_master;
	}

	public List<TicketJsonMaster> getJson_Ticket() {
		return json_Ticket;
	}

	public void setJson_Ticket(List<TicketJsonMaster> json_Ticket) {
		this.json_Ticket = json_Ticket;
	}

	public List<HotelJsonMaster> getJson_Hotel() {
		return json_Hotel;
	}

	public void setJson_Hotel(List<HotelJsonMaster> json_Hotel) {
		this.json_Hotel = json_Hotel;
	}

	public List<CarJsonMaster> getJson_Car() {
		return json_Car;
	}

	public void setJson_Car(List<CarJsonMaster> json_Car) {
		this.json_Car = json_Car;
	}

	public List<OtherJsonMaster> getJson_Other() {
		return json_Other;
	}

	public void setJson_Other(List<OtherJsonMaster> json_Other) {
		this.json_Other = json_Other;
	}

	public List<SharingJsonMaster> getJson_Sharing() {
		return json_Sharing;
	}

	public void setJson_Sharing(List<SharingJsonMaster> json_Sharing) {
		this.json_Sharing = json_Sharing;
	}

}
