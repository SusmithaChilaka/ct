package com.ebreez.etrcs.traacs.processor;


//new added
import java.util.Map;

import org.mule.runtime.api.component.execution.ExecutableComponent;
import org.mule.runtime.api.event.Event;
import org.mule.runtime.api.event.EventContext;
import org.mule.runtime.api.message.Message;
import com.ebreez.etrcs.constants.ETRCSConstants;
import com.ebreez.etrcs.traacs.response.vo.TraacsResponse;
import com.ebreez.etrcs.traacs.vo.TRAACSCacheMap;

public abstract class TraacsResponseProcessor implements ExecutableComponent {

	public Object onCall(EventContext eventContext) throws Exception {
        Message message = ((Event) eventContext).getMessage();
        Object payload = message.getPayload().getValue();
        Message.Builder messageBuilder = Message.builder(message); 

        TraacsResponse response = (TraacsResponse) payload;
		
		if(response.getAuthenticationKey() != null){
			TRAACSCacheMap.setCacheAuthKey(response.getAuthenticationKey());
		}

		((Map<String,TraacsResponse>)messageBuilder).put(ETRCSConstants.VAR_JSON_RESPONSE, response);
		return message;
	}

}
