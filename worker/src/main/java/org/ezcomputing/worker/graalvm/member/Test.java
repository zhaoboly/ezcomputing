package org.ezcomputing.worker.graalvm.member;

import java.util.HashMap;
import java.util.Map;

import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author Bo Zhao
 *
 */
public class Test {
	
	private static final Logger logger = LoggerFactory.getLogger(Test.class);
	
	@HostAccess.Export
	public String name = "test";
	
	@HostAccess.Export
	public String getName() {
		return "test";
	}
	
	/*
	 * this example proved that javascript json is converted to map in java
	 */
	@HostAccess.Export
	public Map<String, Object> callWithJson(Map<String, Object> json) {
		
		json.put("pi", 3.14159);
		
		return json;
		
	}
	
	/*
	 * this example proved that javascript json is converted to value in java
	 */
	@HostAccess.Export
	public ProxyObject callWithJsonValue(Value val) {
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("pi", 3.1415);
		map.put("name", val.getMember("name"));
		return ProxyObject.fromMap(map);
		
	}
	
	/*
	 * this example proved that javascript json array is converted to map in java
	 * but can not find a way to manipulate it
	 */
	@HostAccess.Export
	public Map<String, Object> callWithJsonArray(Map<String, Object> json) {

		return json;
		
	}
	
	@HostAccess.Export
	public Boolean callBoolean(Boolean bool) {
		return !bool;
		
	}
	@HostAccess.Export
	public String callString(String str) {
		return "echo:"+str;
	}
	@HostAccess.Export
	public Integer callInt(Integer number) {
		if(number==null) throw new RuntimeException("number can't be null");
		return number + 100;
	}
	@HostAccess.Export
	public Double callDouble(Double number) {
		return number + 100;
	}
}


