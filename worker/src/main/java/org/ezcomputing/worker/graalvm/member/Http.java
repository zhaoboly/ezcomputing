package org.ezcomputing.worker.graalvm.member;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.ezcomputing.worker.util.JsonUtils;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.proxy.ProxyObject;
/**
 * @author Bo Zhao
 *
 */
public class Http {
	
	
	@HostAccess.Export
	public String getName() {
		return "http";
	}
	@HostAccess.Export
	public ProxyObject get(String url) {
		
		//TODO valide input
		
		HttpClient client = HttpClientBuilder.create().build();
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			
				HttpGet request = new HttpGet(url);
				HttpResponse response = client.execute(request);
				int statusCode = response.getStatusLine().getStatusCode();
				String responseStr = EntityUtils.toString(response.getEntity(), "UTF-8");
				

				map.put("statusCode", statusCode);
				
				map.put("data", JsonUtils.stringify(responseStr));
				
				
				return ProxyObject.fromMap(map);
			
		}catch(Exception e) {
			map.put("error", e.getMessage());
			return ProxyObject.fromMap(map);
		}
		
		
	}
}
