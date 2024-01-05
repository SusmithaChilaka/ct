package com.ebreez.etrcs.traacs.mappers;

import java.util.Map;

import org.mule.runtime.api.message.Message;
import com.ebreez.etrcs.constants.ETRCSConstants;
import com.ebreez.etrcs.traacs.vo.Authentication;


public class TraacsBaseMapper {
	
	protected String action;
	
	protected String status;
	
	protected String autoInvoice;
	
	protected String fopAccnts;
	
	protected String suppAccnts;
	
	// Default constructor
	public TraacsBaseMapper() {
		super();
	}
	
	public void initMapper( Authentication authDetails, Map<String,String> appProperties, 
			Message msg ) {	
	    action = ETRCSConstants.TRAACS_ACTION;
		status = appProperties.get(ETRCSConstants.TRAACS_STATUS);
		autoInvoice = appProperties.get(ETRCSConstants.TRAACS_AUTOINVOICE);	
		fopAccnts = appProperties.get(ETRCSConstants.TRAACS_FOP_IGNORE_ACCNTS);
		suppAccnts = appProperties.get(ETRCSConstants.TRAACS_SUPPLIER_IGNORE_ACCNTS);	
	}

	
	@SuppressWarnings("unchecked")
	public Object map( Object... fromObjects  ) {
		initMapper( ( Authentication ) fromObjects[0], 
				( Map<String,String> ) fromObjects[1], 
				( Message ) fromObjects[2] );
		
		return new Object();
	}
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAutoInvoice() {
		return autoInvoice;
	}

	public void setAutoInvoice(String autoInvoice) {
		this.autoInvoice = autoInvoice;
	}

	public String getFopAccnts() {
		return fopAccnts;
	}

	public void setFopAccnts(String fopAccnts) {
		this.fopAccnts = fopAccnts;
	}

	public String getSuppAccnts() {
		return suppAccnts;
	}

	public void setSuppAccnts(String suppAccnts) {
		this.suppAccnts = suppAccnts;
	}	
	
}
