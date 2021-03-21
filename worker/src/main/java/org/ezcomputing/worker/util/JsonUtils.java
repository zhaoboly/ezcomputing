package org.ezcomputing.worker.util;



import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Bo Zhao
 *
 */
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * @author Bo Zhao
 *
 */
public class JsonUtils {

	private static ObjectMapper objectMapper = new ObjectMapper();

	static {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public static ObjectMapper getObjectMapper() {
		return objectMapper;
	}
	
	public static String stringify(String input){
		try {
			StringWriter writer = new StringWriter();
			objectMapper.writeValue(writer, input);
			return writer.toString();
		}catch(Exception e) {
			return "";
		}
	}
	public static Map<String, Object> jsonToMap(String json){
		try {
			Map<String,Object> result = objectMapper.readValue(json, HashMap.class);
			
			return result;
		}catch(Exception e) {
			return new HashMap<String, Object>();
		}
		
	}

	
	public static String writeValueAsString(List list) {

		try {
			return objectMapper.writeValueAsString(list);
		} catch (Exception e) {
			e.printStackTrace();
			return "[]";
		}
	}
	
	public static String writeValueAsString(Object obj) {

		try {
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			e.printStackTrace();
			return "{}";
		}
	}
	
	public static String prettyPrint(String json) {
		
		if(StringUtils.isBlank(json)) {
			return null;
		}

		try {
			Object obj = objectMapper.readValue(StringUtils.strip(json), Object.class);

			String indented = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);

			return indented;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

