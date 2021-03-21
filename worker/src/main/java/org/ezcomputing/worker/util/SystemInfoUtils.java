package org.ezcomputing.worker.util;

import java.util.HashMap;
import java.util.Map;
/**
 * @author Bo Zhao
 *
 */
public class SystemInfoUtils {
	
	//https://www.roseindia.net/java/beginners/OSInformation.shtml
	public static Map<String, String> collectSystemInfo() {
		HashMap<String, String> map = new HashMap<String, String>();
		
		map.put("os.name", System.getProperty("os.name"));
		map.put("os.version", System.getProperty("os.version"));
		map.put("os.arch",System.getProperty("os.arch") );
		map.put("java.version",System.getProperty("java.version") );
		map.put("java.vendor",System.getProperty("java.vendor") );
		
		int cores = Runtime.getRuntime().availableProcessors();
		map.put("cores",String.valueOf(cores) );
		
		//ip map table : https://datahub.io/core/geoip2-ipv4
			
		return map;
	}

}
