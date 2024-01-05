package com.ebreez.etrcs.traacs.request.vo;

import java.util.List;



import com.ebreez.etrcs.traacs.vo.Authentication;
import com.ebreez.etrcs.traacs.vo.HotelJsonMaster;
import com.ebreez.etrcs.traacs.vo.RoomDetailsJson;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonAutoDetect(
	    fieldVisibility = JsonAutoDetect.Visibility.ANY,
	    getterVisibility = JsonAutoDetect.Visibility.NONE,
	    setterVisibility = JsonAutoDetect.Visibility.NONE
	)
	@JsonSerialize
	@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.ALWAYS)
public class HotelJson {
	
	@JsonProperty("str_authentication_key")
	private Authentication str_authentication_key;
	
	@JsonProperty("json_master")
	private HotelJsonMaster json_master;
	
	@JsonProperty("json_details")
	private List<RoomDetailsJson> json_details;


	public HotelJsonMaster getJson_master() {
		return json_master;
	}

	public void setJson_master(HotelJsonMaster json_master) {
		this.json_master = json_master;
	}

	public Authentication getStr_authentication_key() {
		return str_authentication_key;
	}

	public void setStr_authentication_key(Authentication str_authentication_key) {
		this.str_authentication_key = str_authentication_key;
	}

	public List<RoomDetailsJson> getJson_details() {
		return json_details;
	}

	public void setJson_details(List<RoomDetailsJson> json_details) {
		this.json_details = json_details;
	}

}
