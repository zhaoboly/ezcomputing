package org.ezcomputing.server.manager;

import java.text.MessageFormat;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
/**
 * @author Bo Zhao
 *
 */
public class RemoteLoaderManager {

	private static final Logger logger = LoggerFactory.getLogger(RemoteLoaderManager.class);
	
	public static String HOST;
	
		
		
	@Autowired
	public RemoteLoaderManager(@Value("${remote.function.host}") String remoteFunctionHost) {
		HOST = remoteFunctionHost;
	}
	
	public static String loadRemoteFunction(String functionName, String version, String json) throws Exception{
		logger.info("RemoteLoaderManager host:"+ HOST);
		logger.info(MessageFormat.format("call {0}.{1} with json:{2}", functionName, version, json));

		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = client.execute(new HttpGet(HOST +"/rf/validate/"+functionName+"/"+version));
		int statusCode = response.getStatusLine().getStatusCode();

		if (HttpStatus.OK.value() == statusCode) {
			String jsonString = EntityUtils.toString(response.getEntity(), "UTF-8");
			logger.info("RemoteLoaderManager.success:"+ jsonString);
			return jsonString;
		} else {
			String msg = MessageFormat.format("{0}.{1} is not available, status:{2}", functionName, version,statusCode);
			logger.info("RemoteLoaderManager.failed:"+ msg);
			throw new Exception(msg);
		}

		
		
	}


}
