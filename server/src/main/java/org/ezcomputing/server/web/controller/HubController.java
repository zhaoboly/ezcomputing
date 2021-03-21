package org.ezcomputing.server.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.ezcomputing.server.dao.dto.App;
import org.ezcomputing.server.dao.dto.Constants;
import org.ezcomputing.server.dao.dto.Job;
import org.ezcomputing.server.dao.dto.Module;
import org.ezcomputing.server.dao.dto.Outcome;
import org.ezcomputing.server.dao.dto.SourceHub;
import org.ezcomputing.server.dao.dto.Task;
import org.ezcomputing.server.dao.dto.User;
import org.ezcomputing.server.dao.dto.Worker;
import org.ezcomputing.server.service.RemoteFunctionService;
import org.ezcomputing.server.service.UserService;
import org.ezcomputing.server.util.JsonUtils;
import org.ezcomputing.server.util.SystemInfoUtils;
import org.ezcomputing.worker.domain.JobLogBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author Bo Zhao
 *
 */

@RestController
@RequestMapping("hub")
public class HubController {

	private static final Logger logger = LoggerFactory.getLogger(HubController.class);
	
	
	@Autowired
	private RemoteFunctionService remoteFunctionService;

	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.POST, value = "/task/pull/{accessKey}")
	public ResponseEntity<String> get(HttpSession session, @PathVariable String accessKey) throws Exception {

		// step 1, check whether client accessKey is registered and valid
		// if not, return new ResponseEntity<String>("", null, HttpStatus.UNauthorized);

		// step 2, find the oldest pending task
		// TODO, need rewrite
		List<Task> taskList = remoteFunctionService.getPendingTask();
		if (taskList.isEmpty()) {
			return new ResponseEntity<String>("no task.", null, HttpStatus.NO_CONTENT);
		}

		Task task = taskList.get(0);
		String taskJson = JsonUtils.getObjectMapper().writeValueAsString(task);
		return new ResponseEntity<String>(taskJson, null, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.POST, value = "/job/pull/{accessKey}")
	public ResponseEntity<String> jobPull(HttpSession session, @PathVariable String accessKey) throws Exception {

		// step 1, check whether client accessKey is registered and valid
		Worker worker = userService.loadWorker(accessKey);
		
		if(worker == null) {
			return new ResponseEntity<String>("invalid accessKey", null, HttpStatus.NOT_FOUND);
		}
		
		
		Job job = remoteFunctionService.loadJobsForWorker();
		
		if(job!=null) {
			// remove job log
			job.setJobLogList(new ArrayList());
			// send this job;
			String json = JsonUtils.getObjectMapper().writeValueAsString(job);
			return new ResponseEntity<String>(json, null, HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("no task.", null, HttpStatus.NO_CONTENT);
		}
	}

	// use this method to send back the job result
	@RequestMapping(method = RequestMethod.POST, value = "/job/success/{accessKey}/{id}")
	public ResponseEntity<String> jobSuccess(HttpSession session, @PathVariable String accessKey,
			@PathVariable String id, @RequestBody String json) throws Exception {

		Job job = remoteFunctionService.loadJob(id);
		if (job == null) {
			return new ResponseEntity<String>("", null, HttpStatus.OK);
		}

		JobLogBean log = JsonUtils.getObjectMapper().readValue(json, JobLogBean.class);

		job.setResponse(JsonUtils.prettyPrint(log.getResponse()));
		job.setStatus(Constants.JOB_STATUS.success);
		job.setError(log.getError());
		
		job.addJobLog(log.getWorkerName(), log.getEndTime(), log.getDuration(), log.getStatus(), log.getError());
		

		remoteFunctionService.saveJob(job);

		return new ResponseEntity<String>("", null, HttpStatus.OK);
	}

	// use this method to send back the job result
	@RequestMapping(method = RequestMethod.POST, value = "/job/waitForDependency/{accessKey}/{id}")
	public ResponseEntity<String> jobWait(HttpSession session, @PathVariable String accessKey, @PathVariable String id,
			@RequestBody String json) throws Exception {

		Job job = remoteFunctionService.loadJob(id);
		if (job == null) {
			return new ResponseEntity<String>("", null, HttpStatus.OK);
		}

		Job jobNew = JsonUtils.getObjectMapper().readValue(json, Job.class);

		job.setResponse(jobNew.getResponse());
		job.setStatus(jobNew.getStatus());
		// job.setExecuteLog(job.getExecuteLog() + "\r\n" + jobNew.getExecuteLog());
		remoteFunctionService.saveJob(job);

		runRestartJobTask(job.getId());

		return new ResponseEntity<String>("", null, HttpStatus.OK);
	}

	@Async
	private synchronized void runRestartJobTask(String jobId) {
		Executors.newScheduledThreadPool(1).schedule(() -> {
			logger.info("runRestartJobTask:" + jobId);
			Job job = remoteFunctionService.loadJob(jobId);
			job.setStatus(Constants.JOB_STATUS.readyToRun);

			remoteFunctionService.saveJob(job);
		}, 2, TimeUnit.MINUTES);
	}

	// use this method to send back the task result
	@RequestMapping(method = RequestMethod.POST, value = "/task/success/{accessKey}/{id}")
	public ResponseEntity<String> taskSuccess(HttpSession session, @PathVariable String accessKey,
			@PathVariable String id, @RequestBody String json) throws Exception {

		Task requestTask = JsonUtils.getObjectMapper().readValue(json, Task.class);

		Task task = remoteFunctionService.loadTask(requestTask.getName(), id);
		if (task == null) {
			return new ResponseEntity<String>("", null, HttpStatus.NOT_FOUND);
		}

		// save task
		task.setStatus(Constants.TASK_STATUS.success);
		task.setResponse(requestTask.getResponse());
		remoteFunctionService.saveTask(task);

		// save outcome
		Outcome ou = remoteFunctionService.loadOutcome(task.getName(), task.getHash());
		ou.setOutcomeStatus(Constants.OUTCOME_STATUS.success);
		ou.setResponse(task.getResponse());
		remoteFunctionService.saveOutcome(ou);

		return new ResponseEntity<String>("", null, HttpStatus.OK);
	}

	// use this method to send back the fail result
	@RequestMapping(method = RequestMethod.POST, value = "/job/fail/{accessKey}/{id}")
	public ResponseEntity<String> jobFail(HttpSession session, @PathVariable String accessKey, @PathVariable String id,
			@RequestBody String json) throws Exception {

		Job job = remoteFunctionService.loadJob(id);
		if (job == null) {
			return new ResponseEntity<String>("", null, HttpStatus.OK);
		}

		JobLogBean log = JsonUtils.getObjectMapper().readValue(json, JobLogBean.class);
		
		job.setResponse(JsonUtils.prettyPrint(log.getResponse()));
		job.setStatus(Constants.JOB_STATUS.failed);
		job.setError(log.getError());
		
		job.addJobLog(log.getWorkerName(), log.getEndTime(), log.getDuration(), log.getStatus(), log.getError());
		

		
		remoteFunctionService.saveJob(job);

		return new ResponseEntity<String>("", null, HttpStatus.OK);
	}

	// commit function to function repository
	@RequestMapping(method = RequestMethod.POST, value = "function/commit")
	public ResponseEntity<String> functionCommit(HttpSession session, String name, String version, String source,
			String sampleRequest, String sampleResponse) {

		// source = StringUtils.strip(source);
		sampleRequest = StringUtils.strip(sampleRequest);
		sampleResponse = StringUtils.strip(sampleResponse);

		// logger.info(MessageFormat.format("commit: \r\n{0} \r\n{1} \r\n[{2}] \r\n[{3}]
		// \r\n[{4}]", name, version, source,sampleRequest,sampleResponse));

		// go to function repository to override

		SourceHub sh = new SourceHub();
		sh.setName(name);
		sh.setVersion(version);
		sh.setSource(source);
		sh.setSampleRequest(sampleRequest);
		sh.setSampleResponse(sampleResponse);

		remoteFunctionService.saveSourceHub(sh);

		return new ResponseEntity<String>("ok. \r\n", null, HttpStatus.OK);

	}

	// use this method to send back the job result
	@RequestMapping(method = RequestMethod.POST, value = "/worker/sync")
	public ResponseEntity<String> workerSync(HttpServletRequest request,  @RequestBody String body) throws Exception {

		//TODO check worker version and support modules
		logger.info("workerSync:"+ body);
		
		Map<String, Object> map = JsonUtils.jsonToMap(body);
		
		String uuid = (String)map.get("uuid");
		String email = (String)map.get("email");
		String version = (String)map.get("version");
		
		Worker worker = userService.loadWorker(uuid);
		if(worker==null) {
			User user = userService.loadUser(email);
			
			//new worker
			worker = new Worker();
			worker.setId(uuid);
			worker.setEmail(email);
			worker.setOwnerName(user.toShortFullName());
			worker.setName((String)map.get("name"));
			worker.setApiVersion(Integer.valueOf(version));
			worker.setSystemInfo(SystemInfoUtils.generateString(map));
			userService.saveWorker(worker);
			
			
			if(!user.getWorkerList().contains(uuid)) {
				user.getWorkerList().add(0, uuid);
				userService.saveUser(user);
			}
			
		}else {
			// save ip address
			String ip = request.getRemoteAddr();
			worker.setIp(ip);
			userService.saveWorker(worker);
		}
		

		
		// hide email
		worker.setEmail("");
		String response = JsonUtils.getObjectMapper().writeValueAsString(worker);

		return new ResponseEntity<String>(response, null, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/module/source/{namespace}")
	public ResponseEntity<String> moduleSource(HttpServletRequest request, HttpSession session, @PathVariable String namespace)  {

		//TODO check worker version and support modules
		logger.info("moduleSource, namespace:"+ namespace );
		
		Module module = this.remoteFunctionService.loadModule(namespace, "latest");
		
		if(module!=null) {
			return new ResponseEntity<String>(module.getSource(), null, HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("", null, HttpStatus.NOT_FOUND);
		}
		
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/app/source/{appId}")
	public ResponseEntity<String> appSource( @PathVariable String appId)  {

		
		logger.info("appSource, id:"+ appId );
		
		App app = this.remoteFunctionService.loadApp(appId);
		
		if(app!=null) {
			return new ResponseEntity<String>(app.getSource(), null, HttpStatus.OK);
		}else {
			return new ResponseEntity<String>("", null, HttpStatus.NOT_FOUND);
		}
		
	}
}
