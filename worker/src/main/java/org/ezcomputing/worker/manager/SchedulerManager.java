package org.ezcomputing.worker.manager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.ezcomputing.worker.Application;
import org.ezcomputing.worker.domain.Constants;
import org.ezcomputing.worker.domain.JobBean;
import org.ezcomputing.worker.domain.JobLogBean;
import org.ezcomputing.worker.domain.WorkerBean;
import org.ezcomputing.worker.util.JsonUtils;
import org.ezcomputing.worker.util.SystemInfoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
/**
 * @author Bo Zhao
 *
 */
@Component
public class SchedulerManager {

	private static final Logger logger = LoggerFactory.getLogger(SchedulerManager.class);

	@Autowired
	private JsManager jsManager;
	
	@Autowired
	private Environment environment;

	@Value("${remote.function.host}")
	private String host = null;

	public static String accessKey = null;

	public static WorkerBean worker = new WorkerBean();

	@Autowired
	public SchedulerManager() {
		
	}

	public String getTaskPullUri() {
		return host + "/hub/task/pull/" + accessKey;
	}

	public String getTaskSuccessUri() {
		return host + "/hub/task/success/" + accessKey;
	}

	public String getTaskFailUri() {
		return host + "/hub/task/fail/" + accessKey;
	}

	public String getJobPullUri() {
		return host + "/hub/job/pull/" + accessKey;
	}

	public String getJobSuccessUri() {
		return host + "/hub/job/success/" + accessKey;
	}

	public String getJobFailUri() {
		return host + "/hub/job/fail/" + accessKey;
	}

	public String getJobWaitForDependencyUri() {
		return host + "/hub/job/waitForDependency/" + accessKey;
	}

	public String getHeartBeatUri() {
		return host + "/heartbeat/" + accessKey;
	}

	public String getWorkerSyncUri() {
		return host + "/hub/worker/sync";
	}

	@Scheduled(fixedDelay = 300000)// 5 minutes
	// TODO if not specified pool size, right now run on single thread. only single
	// thread is supported for now
	public void schedulePullWithFixedDelay() throws Exception {

		// TODO weired setup, can't make it works: inject @Value into static field
		//if (RemoteLoaderManager.host == null) {
		//	RemoteLoaderManager.host = host;
		//	logger.info("connect to host:" + host);
		//}
		logger.info("schedulePullWithFixedDelay for job, " );
		runPullJob();

		//logger.info("schedulePullWithFixedDelay for task, current time" + new Date());
		//runPullTask();
	}

	@Scheduled(fixedDelay = 300000) // 5 minutes
	// TODO if not specified pool size, right now run on single thread. only single
	// thread is supported for now
	public void heartbeat() throws Exception {
		logger.info("heartbeat:" + getHeartBeatUri());
		HttpClient client = HttpClientBuilder.create().build();

		client.execute(new HttpGet(getHeartBeatUri()));
	}

	@PostConstruct
	@Scheduled(cron = "0 1 0 * * ?") //everyday at 0:01am
	public void runWorkerSync() throws Exception {
		
		// skip for test profile
		if (Arrays.stream(environment.getActiveProfiles()).anyMatch(env -> (env.equalsIgnoreCase("test")))) {
			return;
		}

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost(this.getWorkerSyncUri());
		
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("uuid", Application.uuid);
		map.put("email", Application.EMAIL);
		map.put("name", Application.NAME);
		map.put("version", "1");
		
		map.putAll(SystemInfoUtils.collectSystemInfo());
		
		request.setEntity(new StringEntity(JsonUtils.writeValueAsString(map)));

		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();

		if (HttpStatus.NOT_FOUND.value() == statusCode) {
			throw new RuntimeException("can not register the worker, please check your key.");
		}

		if (HttpStatus.OK.value() == statusCode) {
			String jsonString = EntityUtils.toString(response.getEntity(), "UTF-8");
			worker = JsonUtils.getObjectMapper().readValue(jsonString, WorkerBean.class);
			logger.info("successfully sync worker:" + worker.getName());
		}else {
			logger.warn("runWorkerSync failed:"+ statusCode);
		}

	}

	private void runPullJob() {
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpPost request = new HttpPost(getJobPullUri());

			HttpResponse response = client.execute(request);
			int statusCode = response.getStatusLine().getStatusCode();

			if (HttpStatus.NO_CONTENT.value() == statusCode) {
				//logger.info("receive zero job.");
				return;
			}

			if (HttpStatus.OK.value() == statusCode) {
				String jsonString = EntityUtils.toString(response.getEntity(), "UTF-8");
				JobBean job = JsonUtils.getObjectMapper().readValue(jsonString, JobBean.class);
				//logger.info("receive job:" + job.getName());
				// logger.info("receive job:"+ job.getSource());
				try {

					JobLogBean log = jsManager.runJavascript(job, worker.getName());
					logger.info(" job status:" + log.getStatus());
					if (Constants.JOB_STATUS.success.equals(log.getStatus())) {
						String jobResponseJson = JsonUtils.getObjectMapper().writeValueAsString(log);
						request = new HttpPost(getJobSuccessUri() + "/" + job.getId());
						StringEntity params = new StringEntity(jobResponseJson);

						request.setEntity(params);
						response = client.execute(request);
					} else if (Constants.JOB_STATUS.failed.equals(log.getStatus())) {
						String jobResponseJson = JsonUtils.getObjectMapper().writeValueAsString(log);
						request = new HttpPost(this.getJobFailUri() + "/" + job.getId());
						StringEntity params = new StringEntity(jobResponseJson);

						request.setEntity(params);
						response = client.execute(request);
					} else if (Constants.JOB_STATUS.waitForDependency.equals(log.getStatus())) {
						String jobResponseJson = JsonUtils.getObjectMapper().writeValueAsString(log);
						request = new HttpPost(getJobWaitForDependencyUri() + "/" + job.getId());
						StringEntity params = new StringEntity(jobResponseJson);

						request.setEntity(params);
						response = client.execute(request);
					}

				} catch (Exception e) {
					e.printStackTrace();
					// String executeLog =
					// MessageFormat.format("{0,date}.{0,time},{1},{2}ms,{3};\r\n", new Date(),
					// worker.getName(), String.valueOf(0), "failed,", e.getMessage());
					// job.setExecuteLog(executeLog);

					String jobResponseJson = JsonUtils.getObjectMapper().writeValueAsString(job);
					request = new HttpPost(getJobFailUri() + "/" + job.getId());
					StringEntity params = new StringEntity(jobResponseJson);

					request.setEntity(params);
					response = client.execute(request);

				}

			}else {
				logger.warn("runPullJob receive error status code:"+ statusCode);
			}
		} catch (HttpHostConnectException e) {
			logger.info(e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	/*-
	
	private void runJavascript111(Job job) throws ScriptException, NoSuchMethodException {
	
		// step 1, reset processingTask flag
		RemoteLoaderManager.processingTask = false;
					
		long time = System.currentTimeMillis();
		String error = null;
		String status = Constants.JOB_STATUS.success;
	
		try {
			
	
			ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
	
			// add build-in lang support
			String code = "load('classpath:js/lang.js')\r\n" + job.getSource();
	
			engine.eval(code);
	
			Invocable invocable = (Invocable) engine;
			Object funcResult = invocable.invokeFunction("main", "");
			
			job.setResponse((String)funcResult);
			
			
			if(RemoteLoaderManager.processingTask) {
				status = Constants.JOB_STATUS.waitForDependency;
			}else {
				status = Constants.JOB_STATUS.success;
			}
			
			//cleanup
			RemoteLoaderManager.processingTask = false;
			
			
			
		} catch (ScriptException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} finally {
			long duration = System.currentTimeMillis() - time;
			String executeLog = MessageFormat.format("{0,date}.{0,time},{1},{2}ms,{3};\r\n", new Date(),
					"worker1", String.valueOf(duration), status , error );
			job.setExecuteLog(executeLog);
			
			job.setStatus(status);
		}
		
	
	}
	
	*/

	
	/*-
	private void runPullTask() {
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpPost request = new HttpPost(getTaskPullUri());

			HttpResponse response = client.execute(request);
			int statusCode = response.getStatusLine().getStatusCode();

			if (HttpStatus.NO_CONTENT.value() == statusCode) {
				// logger.info("receive zero task.");
				return;
			}

			if (HttpStatus.OK.value() == statusCode) {
				String jsonString = EntityUtils.toString(response.getEntity(), "UTF-8");
				Task task = JsonUtils.getObjectMapper().readValue(jsonString, Task.class);
				logger.info("receive task:" + task.getName() + " " + task.getVersion());
				// logger.info("receive job:"+ job.getSource());
				try {

					runJavascript(task);

					if (Constants.JOB_STATUS.success.equals(task.getStatus())) {
						String jobResponseJson = JsonUtils.getObjectMapper().writeValueAsString(task);
						request = new HttpPost(getTaskSuccessUri() + "/" + task.getId());
						StringEntity params = new StringEntity(jobResponseJson);

						request.setEntity(params);
						response = client.execute(request);
					} else {
						// TODO deal with failed and waitfordependency
					}

				} catch (Exception e) {
					e.printStackTrace();
					String executeLog = MessageFormat.format("{0,date}.{0,time},{1},{2}ms,{3};\r\n", new Date(),
							worker.getName(), String.valueOf(0), "failed,", e.getMessage());
					// job.setExecuteLog(executeLog);
				
										String jobResponseJson = JsonUtils.getObjectMapper().writeValueAsString(job);
										request = new HttpPost(URI_JOB_FAIL + "/" + job.getId());
										StringEntity params = new StringEntity(jobResponseJson);
					
										request.setEntity(params);
										response = client.execute(request);
					
				}

			}
		} catch (HttpHostConnectException e) {
			logger.info(e.getMessage());

		} catch (Exception e) {
			e.printStackTrace();

		}
	}
	
	*/
	
	
/*-
	private void runJavascript(Task task) throws ScriptException, NoSuchMethodException {

		// step 1, reset processingTask flag
		RemoteLoaderManager.processingTask = false;

		long time = System.currentTimeMillis();
		String error = null;
		String status = Constants.JOB_STATUS.success;

		try {

			ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

			// add build-in lang support
			String code = "load('classpath:js/lang.js')\r\n" + task.getSource();

			engine.eval(code);
			Object json = engine.eval("Java.asJSONCompatible(" + task.getRequest() + ")");

			Invocable invocable = (Invocable) engine;
			Object funcResult = invocable.invokeFunction("main", json);

			task.setResponse((String) funcResult);

			if (RemoteLoaderManager.processingTask) {
				status = Constants.TASK_STATUS.waitForDependency;
			} else {
				status = Constants.TASK_STATUS.success;
			}

			// cleanup
			RemoteLoaderManager.processingTask = false;

		} catch (ScriptException e) {
			e.printStackTrace();
			status = Constants.TASK_STATUS.failed;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			status = Constants.TASK_STATUS.failed;
		} finally {
			long duration = System.currentTimeMillis() - time;
			String executeLog = MessageFormat.format("{0,date}.{0,time},{1},{2}ms,{3};\r\n", new Date(),
					worker.getName(), String.valueOf(duration), status, error);
			// task.setExecuteLog(executeLog);

			task.setStatus(status);
		}

	}
*/
}
