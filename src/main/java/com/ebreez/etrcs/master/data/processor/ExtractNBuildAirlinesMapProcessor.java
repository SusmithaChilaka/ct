package com.ebreez.etrcs.master.data.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mule.runtime.api.metadata.TypedValue;

import com.ebreez.etrcs.constants.ETRCSConstants;
import com.ebreez.etrcs.vo.Airline;

public abstract class ExtractNBuildAirlinesMapProcessor {
	
	
	public static final String NUMERIC_CODE = "NUMERIC_CODE";
	public static final String AIRLINE_NAME = "AIRLINE_NAME";
	public static final String AIRLINE_CODE = "AIRLINE_CODE";

	@SuppressWarnings("unchecked")
	public Object onCall(List<Map<String,Object>> list) throws Exception {

		Map<String,Airline> airlinesMap = new HashMap<String,Airline>();

		List<Map<String,Object>> comAlnList =  (List<Map<String, Object>>) list.getClass().getResourceAsStream(ETRCSConstants.VAR_COM_AIRLINE_DATA);
		
		for(Map<String,Object> airline : comAlnList){
			Airline aln = new Airline();
			String key = (String)airline.get(AIRLINE_CODE);
			aln.setAlnName((String)airline.get(AIRLINE_NAME));
			aln.setAlnCCode(key);
			aln.setAlnNCode((String)airline.get(NUMERIC_CODE));
			airlinesMap.put(key, aln);
		}
		((Map<String,TypedValue<?>>) list).remove(ETRCSConstants.VAR_COM_AIRLINE_DATA);
		
		return airlinesMap;
	}

}
