package org.ezcomputing.server.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.ezcomputing.server.dao.dto.App;
import org.ezcomputing.server.dao.dto.Constants;
import org.ezcomputing.server.dao.dto.Job;
import org.ezcomputing.server.dao.dto.JobScheduler;
import org.ezcomputing.server.dao.dto.Module;
import org.ezcomputing.server.dao.dto.User;
import org.ezcomputing.server.dao.dto.Worker;
import org.ezcomputing.server.manager.JsServerManager;
import org.ezcomputing.server.service.RemoteFunctionService;
import org.ezcomputing.server.service.UserService;
import org.ezcomputing.server.util.IdUtils;
import org.ezcomputing.server.util.JsonUtils;
import org.ezcomputing.server.util.ResourceUtils;
import org.ezcomputing.server.util.TemplateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
/**
 * @author Bo Zhao
 *
 */
@Controller
@RequestMapping("web")
public class WebController {

	private static final Logger logger = LoggerFactory.getLogger(WebController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private JsServerManager jsServerManager;

	@Autowired
	private RemoteFunctionService remoteFunctionService;

	private User retrieveLoginUser() {
		SecurityContext sc = SecurityContextHolder.getContext();
		Authentication auth = sc.getAuthentication();

		User user = (User) auth.getPrincipal();

		return user;
	}

	private Authentication retrieveAuthentication() {
		SecurityContext sc = SecurityContextHolder.getContext();
		Authentication auth = sc.getAuthentication();

		return auth;
	}

	private String retrieveLoginUserEmail() {

		return retrieveLoginUser().getEmail();
	}

	@GetMapping(value = "profile")
	public ModelAndView profile() {

		Map<String, Object> params = new HashMap<String, Object>();

		User user = this.retrieveLoginUser();
		params.put("user", user);
		params.put("isTester", this.retrieveAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_TESTER")));


		return new ModelAndView("profile", params);
	}

	@GetMapping(value = "console")
	public ModelAndView console() {

		Map<String, Object> params = new HashMap<String, Object>();

		User user = retrieveLoginUser();
		params.put("username", user.getGivenName());

		return new ModelAndView("console", params);
	}

	@GetMapping(value = "job/list")
	public ModelAndView jobList() {

		List<Job> list = remoteFunctionService.getJobsByOwner(this.retrieveLoginUserEmail());

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("jobList", list);

		User user = retrieveLoginUser();
		params.put("username", user.getGivenName());
		params.put("isTester", this.retrieveAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_TESTER")));


		return new ModelAndView("jobList", params);
	}

	@RequestMapping(method = RequestMethod.POST, value = "job/create")
	public ModelAndView jobCreate(HttpSession session) {

		Job job = new Job();
		job.setName("new job");
		job.setOwner(this.retrieveLoginUserEmail());
		
		job.setSource(TemplateUtils.getJobSourceTemplate());
		job.setRequest(TemplateUtils.getJobRequestTemplate());
		job.setResponse(TemplateUtils.getJobResponseTemplate());
		
		remoteFunctionService.saveJob(job);

		return new ModelAndView("redirect:/web/job/list");

	}

	@GetMapping(value = { "job/view/{id}" })
	public ModelAndView jobView(@PathVariable String id) {

		Job job = remoteFunctionService.loadJob(id);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("job", job);

		return new ModelAndView("jobView", params);
	}

	@GetMapping(value = { "job/scheduler/view/{id}" })
	public ModelAndView jobSchedulerView(@PathVariable String id) {

		Job job = remoteFunctionService.loadJob(id);

		JobScheduler scheduler = job.getScheduler();
		if (scheduler == null) {
			scheduler = new JobScheduler();
			scheduler.setJobId(id);
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("job", job);
		params.put("scheduler", scheduler);

		return new ModelAndView("jobSchedulerView", params);
	}

	@PostMapping(value = { "job/scheduler/submit" })
	public ModelAndView jobSchedulerSubmit(String id, String jobId, String type, int hours, int minutes) {

		logger.info("jobSchedulerSubmit" + jobId);
		Job job = remoteFunctionService.loadJob(jobId);

		JobScheduler scheduler = job.getScheduler();
		if (scheduler == null) {
			scheduler = new JobScheduler();
			scheduler.setJobId(jobId);
			job.setScheduler(scheduler);
		}

		scheduler.setType(type);
		scheduler.setHours(hours);
		scheduler.setMinutes(minutes);

		remoteFunctionService.saveJob(job);

		return new ModelAndView("redirect:/web/job/list");
	}

	@PostMapping(value = { "job/scheduler/delete" })
	public ModelAndView jobSchedulerDeletee(String jobId) {

		logger.info("jobSchedulerDeletee:" + jobId);

		Job job = remoteFunctionService.loadJob(jobId);

		job.setScheduler(null);

		remoteFunctionService.saveJob(job);

		return new ModelAndView("redirect:/web/job/list");
	}

	@RequestMapping(method = RequestMethod.POST, value = "job/delete")
	public ModelAndView jobDelete(HttpSession session, String id) {

		logger.info("jobDelete:" + id);

		Job job = remoteFunctionService.loadJob(id);

		if (job != null) {
			remoteFunctionService.deleteJob(job);
		}

		return new ModelAndView("redirect:/web/job/list");
	}

	@RequestMapping(method = RequestMethod.POST, value = "job/publish")
	public ModelAndView jobPublish(HttpSession session, String jobId, String name, String description) {

		logger.info("jobPublish:"+ jobId+" name:" + name);

		Job job = remoteFunctionService.loadJob(jobId);

		if (job != null) {
			App app = new App();
			
			
			app.setOwner(job.getOwner());
			
			app.setJobId(jobId);
			
			String id = name.replaceAll(" ","_") + "_"+ IdUtils.generateWorkerId();
			app.setId(id);
			app.setName(name);
			app.setDescription(description);
			
			app.setSource(job.getSource());
			app.setRequest(job.getRequest());
			app.setResponse(job.getResponse());
			
			app.setPublishDate(new Date());
			
			remoteFunctionService.saveApp(app);
			
			job.setPublishedAppId (id);
			remoteFunctionService.saveJob(job);
		}

		return new ModelAndView("redirect:/web/job/list");
	}

	
	@RequestMapping(method = RequestMethod.POST, value = "job/save")
	public ModelAndView jobSave(HttpSession session, String id, String request, String source) {

		Job job = new Job();

		if (!StringUtils.isEmpty(id)) {
			job = remoteFunctionService.loadJob(id);
		}

		// job.setName(name);
		// job.setRequest(StringUtils.strip(request));
		job.setRequest(request);
		job.setSource(source);
		remoteFunctionService.saveJob(job);

		return new ModelAndView("redirect:/web/job/view/" + id);
	}

	@RequestMapping(method = RequestMethod.POST, value = "job/validate")
	public ResponseEntity<String> jobValidate(HttpSession session, String id) {

		Job job = remoteFunctionService.loadJob(id);

		// validate job to determine can be run independently or waitfordependency
		String error = validateJob(job);

		if (error == null) {
			job.setStatus(Constants.JOB_STATUS.validated);
			remoteFunctionService.saveJob(job);
			return new ResponseEntity<String>("success. \r\n", null, HttpStatus.OK);

		} else {
			return new ResponseEntity<String>(error, null, HttpStatus.BAD_REQUEST);

		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "job/run")
	public ModelAndView jobRun(HttpSession session, String id) {

		if (StringUtils.isBlank(id)) {
			return new ModelAndView("redirect:/web/job/list");
		}

		Job job = remoteFunctionService.loadJob(id);

		if (job != null) {

			job.setStatus(Constants.JOB_STATUS.readyToRun);
			remoteFunctionService.saveJob(job);
		}

		// return new ModelAndView("redirect:/web/job/list");
		return new ModelAndView("redirect:/web/job/view/" + id);

	}

	@RequestMapping(method = RequestMethod.POST, value = "job/run-all")
	public ModelAndView jobRunAll(HttpSession session, String id) {

		logger.info("receive jobRunAll:" + id);

		if (StringUtils.isBlank(id)) {
			return new ModelAndView("redirect:/web/job/list");
		}

		String[] jobIdList = id.split(";");

		for (String jobId : jobIdList) {
			Job job = remoteFunctionService.loadJob(jobId);

			if (job != null) {

				job.setStatus(Constants.JOB_STATUS.readyToRun);
				remoteFunctionService.saveJob(job);
			}
		}

		return new ModelAndView("redirect:/web/job/list");

	}

	@RequestMapping(method = RequestMethod.POST, value = "job/runLocal")
	public ModelAndView jobRunLocal(HttpSession session, String id) throws Exception {

		Job job = remoteFunctionService.loadJob(id);

		
		jsServerManager.runJavascript(job, "ezcomputing.org worker");

		remoteFunctionService.saveJob(job);

		return new ModelAndView("redirect:/web/job/view/" + id);

	}

	private String validateJob(Job job) {

		String error = null;

		try {

			ScriptEngine engine = new ScriptEngineManager().getEngineByName("graal.js");

			// add build-in lang support
			// String code = "load('classpath:js/lang.js')\r\n" + job.getSource();
			String code = ResourceUtils.readResource("js/lang.js") + "\r\n" + job.getSource();

			engine.eval(code);

			Invocable invocable = (Invocable) engine;
			Object funcResult = invocable.invokeFunction("main", "");

		} catch (ScriptException e) {
			// e.printStackTrace();
			logger.info("got JS exception:" + e.getMessage());
			error = e.getMessage();
		} catch (NoSuchMethodException e) {
			// e.printStackTrace();
			error = e.getMessage();
		} catch (IOException e) {
			// e.printStackTrace();
			error = e.getMessage();
		}

		return error;

	}

	@GetMapping(value = "worker/list")
	public ModelAndView workerList() {
		User user = this.retrieveLoginUser();
		String email = user.getEmail();

		List<Worker> list = userService.getWorkerListByOwner(email);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("workerList", list);
		params.put("workerListJson", JsonUtils.writeValueAsString(list));
		params.put("hasPrivateWorker", !list.isEmpty());
		
		params.put("publicWorkerList", userService.getPublicWorkerList());

		params.put("username", user.getGivenName());
		params.put("isTester", this.retrieveAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_TESTER")));


		return new ModelAndView("workerList", params);
	}
/*-
	@RequestMapping(method = RequestMethod.POST, value = "worker/create")
	public ModelAndView workerCreate(HttpSession session) {

		User user = this.retrieveLoginUser();
		String email = user.getEmail();

		Worker worker = new Worker();
		worker.setId(IdUtils.generateWorkerId());
		worker.setEmail(email);
		worker.setName("new Worker");
		userService.saveWorker(worker);

		user.getWorkerList().add(worker.getId());
		userService.saveUser(user);

		return new ModelAndView("redirect:/web/worker/list");

	}*/

	@RequestMapping(method = RequestMethod.POST, value = "worker/delete")
	public ModelAndView workerCreate(HttpSession session, String id) {

		// User user = this.retrieveLoginUser();

		User user = userService.loadUser(this.retrieveLoginUserEmail());

		Worker worker = userService.loadWorker(id);
		userService.deleteWorker(worker);

		user.getWorkerList().remove(id);
		userService.saveUser(user);

		return new ModelAndView("redirect:/web/worker/list");

	}

	@RequestMapping(method = RequestMethod.POST, value = "worker/changeName")
	public ModelAndView workerChangeName(HttpSession session, String id, String name) {

		logger.info("workerChangeName:" + id + ";" + name);
		Worker worker = userService.loadWorker(id);
		if(worker!=null) {
			worker.setName(name);
			userService.saveWorker(worker);
		}

		return new ModelAndView("redirect:/web/worker/list");

	}
	
	@RequestMapping(method = RequestMethod.POST, value = "worker/verify")
	public ModelAndView workerVerify(HttpSession session, String id) {

		logger.info("workerVerify:" + id );
		Worker worker = userService.loadWorker(id);

		if(worker!=null) {
			worker.setVerified(true);
			userService.saveWorker(worker);
		}

		return new ModelAndView("redirect:/web/worker/list");

	}

	@RequestMapping(method = RequestMethod.POST, value = "job/changeName")
	public ModelAndView jobChangeName(HttpSession session, String id, String name) {

		logger.info("jobChangeName:" + id + ";" + name);
		Job job = this.remoteFunctionService.loadJob(id);
		if (job != null) {
			job.setName(name);
			remoteFunctionService.saveJob(job);
		}

		return new ModelAndView("redirect:/web/job/view/" + id);

	}

	@GetMapping(value = "module/list")
	public ModelAndView moduleList() {

		List<Module> list = remoteFunctionService.getModulesByOwner(this.retrieveLoginUserEmail());

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("list", list);
		params.put("isTester", this.retrieveAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_TESTER")));

		return new ModelAndView("moduleList", params);
	}

	@RequestMapping(method = RequestMethod.POST, value = "module/create")
	public ModelAndView moduleCreate(HttpSession session, String namespace) {

		Module module = new Module();
		module.setNamespace(namespace);
		module.setOwner(this.retrieveLoginUserEmail());

		remoteFunctionService.saveModule(module);

		return new ModelAndView("redirect:/web/module/list");

	}

	@GetMapping(value = { "module/view/{namespace}/{version}" })
	public ModelAndView moduleView(@PathVariable String namespace, @PathVariable String version) {

		Module module = remoteFunctionService.loadModule(namespace, version);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("module", module);

		return new ModelAndView("moduleView", params);
	}

	@RequestMapping(method = RequestMethod.POST, value = "module/save")
	public ModelAndView moduleSave(String namespace, String version, String source) {

		Module module = remoteFunctionService.loadModule(namespace, version);

		if (module != null) {
			module.setSource(source);
			remoteFunctionService.saveModule(module);
		}

		return new ModelAndView("redirect:/web/module/view/" + namespace + "/" + version);
	}
	@RequestMapping(method = RequestMethod.POST, value = "module/delete")
	public ModelAndView moduleDelete(String namespace, String version) {
		
		//TODO verify owner

		Module module = remoteFunctionService.loadModule(namespace, version);

		if (module != null) {
			
			remoteFunctionService.deleteModule(module);
		}
		return new ModelAndView("redirect:/web/module/list");
		
	}

	@RequestMapping(method = RequestMethod.POST, value = "module/run")
	public ModelAndView moduleRun(String namespace, String version) throws Exception {

		Module module = remoteFunctionService.loadModule(namespace, version);

		if (module != null) {
			jsServerManager.runJavascript(module);

			remoteFunctionService.saveModule(module);

		}
		return new ModelAndView("redirect:/web/module/view/" + namespace + "/" + version);

	}

	
	@RequestMapping(method = RequestMethod.GET, value = "/target/{fileName}")
	public ResponseEntity<String> logfile(HttpSession session, @PathVariable String fileName) {

		
		if(this.retrieveAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
		
			try {
				Resource resource = new FileSystemResource("/usr/src/ezcomputing/target/" + fileName);
				InputStream inputStream = resource.getInputStream();
	
				String text = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
	
				text = text.replaceAll("2021", "<br>2021");
				return new ResponseEntity<String>(text, null, HttpStatus.OK);
	
			} catch (IOException e) {
				e.printStackTrace();
				return new ResponseEntity<String>("error", null, HttpStatus.NOT_FOUND);
			}
		}else {
			return new ResponseEntity<String>("error", null, HttpStatus.UNAUTHORIZED);
		}
	}
	
	@GetMapping(value = "app/list")
	public ModelAndView appList() {

		logger.info("appList");
		List<App> list = remoteFunctionService.loadAppList();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("appList", list);

		params.put("isTester", this.retrieveAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_TESTER")));

		return new ModelAndView("appList", params);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "app/import")
	public ModelAndView appImport(HttpSession session,String id) {
		
		App app = this.remoteFunctionService.loadApp(id);
		
		if(app!=null) {
			Job job = new Job();
			job.setName(app.getName());
			job.setOwner(this.retrieveLoginUserEmail());
			job.setType(Constants.JOB_TYPE.fromApp);
			job.setFromAppId(app.getId());
			job.setRequest(app.getRequest());
			job.setSource(app.getSource());
			job.setFromAppDate(app.getPublishDate());
			
			remoteFunctionService.saveJob(job);
		}

		

		return new ModelAndView("redirect:/web/job/list");

	}
	
	@GetMapping(value = { "app/view/{id}" })
	public ModelAndView appView(@PathVariable String id) {

		App app = remoteFunctionService.loadApp(id);
		
		User user = userService.loadUser(app.getOwner());
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("app", app);
		params.put("owner", user.toFullName());

		return new ModelAndView("appView", params);
	}

	
	

}
