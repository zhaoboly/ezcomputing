package org.ezcomputing.worker.manager;


import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.ezcomputing.worker.domain.Constants;
import org.ezcomputing.worker.domain.JobBean;
import org.ezcomputing.worker.domain.JobLogBean;
import org.ezcomputing.worker.graalvm.member.Http;
import org.ezcomputing.worker.graalvm.member.Lang;
import org.ezcomputing.worker.graalvm.member.Test;
import org.ezcomputing.worker.util.JsonUtils;
import org.ezcomputing.worker.util.ResourceUtils;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
/**
 * @author Bo Zhao
 *
 */
@Component
public class JsManager {

	private static final Logger logger = LoggerFactory.getLogger(JsManager.class);
	
	private static HttpClient httpClient = HttpClientBuilder.create().build();
	
	@org.springframework.beans.factory.annotation.Value("${remote.function.host}")
	private String remoteFunctionHost ;

	
	
	public  JobLogBean runJavascript(JobBean job, String workerName)  {

		JobLogBean jobLogBean = new JobLogBean();
		
		long time = System.currentTimeMillis();
		String error = null;
		String status = Constants.JOB_STATUS.success;

		try {
			Engine engine = Engine.create();
			Source source = Source.create("js", job.getSource());
			
			
			Context context = Context.newBuilder().engine(engine).allowAllAccess(false).build();
						
			context.getBindings("js").putMember("lang", new Lang(this));
			
			
			
			context.eval(Source.create("js", ResourceUtils.readResource("js/lang.js")));
			
			Map<String,String> modules = new HashMap<String, String>();
			loadModules(modules, job.getSource());
			
			// add js codes
			for(String code: modules.values()) {
				context.eval(Source.create("js", code));	
			}
			
			// add build-in functions
			if(modules.containsKey("http")) {
				context.getBindings("js").putMember("http", new Http());
			}
			if(modules.containsKey("test")) {
				context.getBindings("js").putMember("test", new Test());
			}
			
			context.eval(source);
			Value value=context.eval("js", "main("+ job.getRequest() +")");
			
			
			String response = context.eval("js", "JSON.stringify").execute(value).asString();
			
			status = Constants.JOB_STATUS.success;
			jobLogBean.setResponse( JsonUtils.prettyPrint(response));


		}  catch(Exception e){
			//e.printStackTrace();
			error = e.getMessage();
			status = Constants.JOB_STATUS.failed;
			
			//set response to empty if it's failed
			jobLogBean.setResponse(null);
			
		}finally {
			long duration = System.currentTimeMillis() - time;

			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			//job.addJobLog(workerName, formatter.format(new Date()), String.valueOf(duration),
			//		status, error);

			jobLogBean.setStatus(status);
			jobLogBean.setError(error);
			jobLogBean.setEndTime(formatter.format(new Date()));
			jobLogBean.setDuration(String.valueOf(duration));
			jobLogBean.setWorkerName(workerName);
		}

		return jobLogBean;
	}

	
	public  String runJavascript(String apiId, String request)  {

		logger.info("call runJavascript apiId:"+apiId);
		String apiSource = this.readApiSource(apiId);
		
		try {
			Engine engine = Engine.create();
			Source source = Source.create("js", apiSource);
			
			
			Context context = Context.newBuilder().engine(engine).allowAllAccess(false).build();
						
			context.getBindings("js").putMember("lang", new Lang(this));
			
			if(apiSource.contains("include('test')")) {
				context.getBindings("js").putMember("test", new Test());
			}
			if(apiSource.contains("include('http')")) {
				context.getBindings("js").putMember("http", new Http());
			}
			
			context.eval(Source.create("js", ResourceUtils.readResource("js/lang.js")));
			
			Map<String,String> modules = new HashMap<String, String>();
			loadModules(modules, apiSource);
			for(String code: modules.values()) {
				context.eval(Source.create("js", code));	
			}
			
			
			context.eval(source);
			Value value = context.eval("js", "main("+ request +")");
			
			String response = context.eval("js", "JSON.stringify").execute(value).asString();
			
			return response;
			
			

		}  catch(Exception e){
			 throw new RuntimeException(e.getMessage());
			
		}
	}


	
	protected  void loadModules(Map<String,String> modules, String source) {
		
		for(String module: retrieveLoadModules(source)) {
			
			
			if(!modules.containsKey(module)) {
				if(module.contains("_")) {
					//load customize module
					String code = readModuleContent(module);
					logger.info("load module:"+module);
					modules.put(module, code);
					
					//continue to load modules
					loadModules(modules, code);
				}else {
					//build-in module
					modules.put(module, "");
				}
				
			}
			
		}
		
	}
	
	/*
	 * match 
	 * 1. include("http")
	 * 2. include('http')
	 */
	protected List<String> retrieveLoadModules(String source) {
		
		List<String> results = new ArrayList<String>();
		
				
		 Pattern pattern = Pattern.compile("include\\([\"\']?[a-zA-Z0-9_]*[\"\']?\\)");
		 
		 Matcher matcher = pattern.matcher(source);
		 while (matcher.find()) {
			 
			 String val = matcher.group(0);
			 
			 if(val.contains("\"")) {
				 results.add(StringUtils.substringBetween(val, "\""));
			 }else {
				 results.add(StringUtils.substringBetween(val, "\'"));
			 }
		        
		  } 
		   
		    
		
		return results;
	}
	
	protected  String readModuleContent(String module) {
		String url = remoteFunctionHost +"/hub/module/source/"+ module;
		try {
			HttpResponse response = httpClient.execute(new HttpGet(url));
			
			int statusCode = response.getStatusLine().getStatusCode();

			if (HttpStatus.NOT_FOUND.value() == statusCode) {
				throw new RuntimeException("can not find module:"+ module);
			}

			if (HttpStatus.OK.value() == statusCode) {
				HttpEntity entity = response.getEntity();
				String responseString = EntityUtils.toString(entity, "UTF-8");
				
				return responseString;
			}else {
				throw new RuntimeException("load module error:"+ statusCode);
			}
			
			
		}catch(IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	
	protected  String readApiSource(String apiId) {
		String url = remoteFunctionHost +"/hub/app/source/"+ apiId;
		try {
			HttpResponse response = httpClient.execute(new HttpGet(url));
			
			int statusCode = response.getStatusLine().getStatusCode();

			if (HttpStatus.NOT_FOUND.value() == statusCode) {
				throw new RuntimeException("can not find api:"+ apiId);
			}

			if (HttpStatus.OK.value() == statusCode) {
				HttpEntity entity = response.getEntity();
				String responseString = EntityUtils.toString(entity, "UTF-8");
				
				return responseString;
			}else {
				throw new RuntimeException("load module error:"+ statusCode);
			}
			
			
		}catch(IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
}

