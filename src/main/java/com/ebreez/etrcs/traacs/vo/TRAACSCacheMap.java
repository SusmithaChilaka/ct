/**
 * Copyright (C)2016 Ebreez Software Pvt. Ltd.
 *
 * Ebreez Software Pvt. Ltd.
 * 6-3-1086/A, 
 * 3rd Floor, ANK'S Tower,
 * Hyderabad, India-500082
 * 
 * Telephone - 91-40-65690015
 * 
 * All rights reserved - The copyright notice above does not evidence
 * any actual or intended publication of this source code.
 * 
 * File Name: TRAACSCacheMap.java
 * 
 * Created On: June 24, 2016
 * 
 * Author: Veeramalleswarudu Seelam
 * 
 */
package com.ebreez.etrcs.traacs.vo;

/**
 * The Class TRAACSCacheMap.
 */
public class TRAACSCacheMap {

	
	/** Cache the authentication request of the one request and returns for others. */
	private static String cacheAuthKey;

	
	public static String getCacheAuthKey() {
		return cacheAuthKey;
	}

	public static synchronized void setCacheAuthKey( String cacheAuthKey) {
		TRAACSCacheMap.cacheAuthKey = cacheAuthKey;
	}

	
}
