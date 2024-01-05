/**
 * 
 */
package com.ebreez.etrcs.traacs.roeCapture;

import java.util.HashMap;
import java.util.Map;

import org.mule.runtime.api.component.execution.ExecutableComponent;
import org.mule.runtime.api.event.Event;
import org.mule.runtime.api.event.EventContext;
import org.mule.runtime.api.message.Message;

import com.ebreez.etrcs.request.vo.TraacsRoeRequest;
import com.ebreez.etrcs.traacs.vo.CurrencyRoeCapture;
import com.ebreez.etrcs.traacs.vo.RoeAuthentication;

/**
 * @author Rakesh K
 *
 */
public abstract class TraacsRoeCaptureEGP implements  ExecutableComponent{


	public Object onCall(TraacsRoeRequest roeRQ) throws Exception {

		Map<String, Map<String, String>> json = new HashMap<String, Map<String, String>>();
		
		CurrencyRoeCapture roeCapture = new CurrencyRoeCapture();
		RoeAuthentication auth = new RoeAuthentication();
		
		auth.setSTR_USER_NAME("user");
		auth.setSTR_PASSWORD("user123");
		
		for(Map<String, String> roe : roeRQ.getRoeDetails().get("EGP")){
			if(null != roe){
				Map<String, String> temp =  new HashMap<String, String>();
				 temp.put("STR_EFFECTIVE_DATE",roe.get("date"));
				 temp.put("DBL_EXCHANGE_RATE",roe.get("excRate"));
				 json.put(roe.get("currency"),temp);
				 roeCapture.setRoeDetails(json);
			}
		}
		
		roeCapture.setRoeAuthentication(auth);
		return roeCapture;
	}
}
