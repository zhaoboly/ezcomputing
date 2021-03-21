package org.ezcomputing.server.util;

import java.util.HashMap;
import java.util.Map;

import com.ibm.icu.text.MessageFormat;
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

	
	public static String generateString(Map<String,Object> map) {
		String str = MessageFormat.format("{0}:{1} <br> Arch: {2} <br>java:{3} <br> java version:{4} <br> api version:{5}", (String)map.get("os.name"),(String)map.get("os.version"),(String)map.get("os.arch"),(String)map.get("java.vendor"),(String)map.get("java.version"),(String)map.get("version"));
		return str;
	}
}
