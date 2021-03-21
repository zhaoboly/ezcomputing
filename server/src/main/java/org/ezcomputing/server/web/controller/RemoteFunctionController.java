package org.ezcomputing.server.web.controller;

import java.io.IOException;
import java.text.MessageFormat;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpSession;

import org.ezcomputing.server.dao.dto.Constants;
import org.ezcomputing.server.dao.dto.Outcome;
import org.ezcomputing.server.dao.dto.SourceHub;
import org.ezcomputing.server.dao.dto.Task;
import org.ezcomputing.server.service.RemoteFunctionService;
import org.ezcomputing.server.util.EncodingUtils;
import org.ezcomputing.server.util.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Bo Zhao
 *
 */
@RestController
@RequestMapping("rf")
public class RemoteFunctionController {

	private static final Logger logger = LoggerFactory.getLogger(RemoteFunctionController.class);

	@Autowired
	private RemoteFunctionService remoteFunctionService;

	@RequestMapping(method = RequestMethod.GET, value = "{functionName}/{hash}/{version}")
	public ResponseEntity<String> getOutcomeByHash(HttpSession session, @PathVariable String functionName,
			@PathVariable String hash, @PathVariable String version) throws Exception {

		// assumption 1, already pass the validation, no need to check
		// functionName+version status

		// TODO use JsonNode.equls to check json structure equal

		// step 1, go to outcome repository to get the response
		Outcome ou = remoteFunctionService.loadOutcome(functionName, hash);
		if (ou != null) {

			if (Constants.OUTCOME_STATUS.success.equals(ou.getOutcomeStatus())) {
				// success
				// TODO increate invoke count, should be async
				ou.setInvokeCount(ou.getInvokeCount() + 1);
				remoteFunctionService.saveOutcome(ou);

				return new ResponseEntity<String>(ou.getResponse(), null, HttpStatus.OK);

			} else if (Constants.OUTCOME_STATUS.failed.equals(ou.getOutcomeStatus())) {
				//
				return new ResponseEntity<String>(ou.getErrorMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
			} else if (Constants.OUTCOME_STATUS.pending.equals(ou.getOutcomeStatus())) {
				//
				SourceHub sh = remoteFunctionService.loadSourceHub(functionName, version);
				return new ResponseEntity<String>(sh.getSampleResponse(), null, HttpStatus.PROCESSING);
			}

		}

		// step 2, if not found, load function's sampelresponse, return http code: 404 
		// so the client can continue for the code running
		
		SourceHub sh = remoteFunctionService.loadSourceHub(functionName, version);
		return new ResponseEntity<String>(sh.getSampleResponse(), null, HttpStatus.NOT_FOUND);

	}

	/*
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value = "run/{functionName}/{version}/{encodedJsonRequest}")
	public ResponseEntity<String> runFunctionOffline(HttpSession session, @PathVariable String functionName,
			@PathVariable String version, @PathVariable String encodedJsonRequest) throws Exception {

		String jsonRequest = EncodingUtils.decode(encodedJsonRequest);
		String hash = EncodingUtils.createHash(version, jsonRequest);

		// assumption 1, already pass the validation, no need to check
		// functionName+version status

		// TODO use JsonNode.equls to check json structure equal

		// step 1, go to outcome repository to check outcome
		Outcome ou = remoteFunctionService.loadOutcome(functionName, hash);
		if (ou != null) {
			// duplicate run request
			return new ResponseEntity<String>(ou.getResponse(), null, HttpStatus.CONFLICT);
		}

		// step 2, load source hub
		SourceHub sh = remoteFunctionService.loadSourceHub(functionName, version);
		if (sh == null) {
			// not exist
			return new ResponseEntity<String>("function not found.", null, HttpStatus.BAD_REQUEST);
		}
		if (!Constants.Source_HUB_STATUS.published.equals(sh.getStatus())) {
			// status is not right
			return new ResponseEntity<String>("function is not published.", null, HttpStatus.BAD_REQUEST);
		}

		// step 3, create pending outcome
		ou = new Outcome();
		ou.setOutcomeStatus(Constants.OUTCOME_STATUS.pending);
		ou.setName(functionName);
		ou.setHash(hash);
		ou.setVersion(version);
		ou.setRequest(jsonRequest);

		ou.setSource(sh.getSource());

		remoteFunctionService.saveOutcome(ou);
		
		// step 4, create task to run the js code
		Task task = new Task();
		task.setName(functionName);
		task.setHash(hash);
		task.setVersion(version);
		task.setRequest(jsonRequest);
		task.setSource(sh.getSource());
		

		remoteFunctionService.saveTask(task);
		
		return new ResponseEntity<String>("success", null, HttpStatus.OK);

	

	}

	/*
	 * 
	 */
	/*-
	@RequestMapping(method = RequestMethod.GET, value = "{functionName}/{version}/{encodedJsonRequest}")
	public ResponseEntity<String> runFunctionOnline(HttpSession session, @PathVariable String functionName,
			@PathVariable String version, @PathVariable String encodedJsonRequest) throws Exception {

		String jsonRequest = EncodingUtils.decode(encodedJsonRequest);
		String hash = EncodingUtils.createHash(version, jsonRequest);

		// assumption 1, already pass the validation, no need to check
		// functionName+version status

		// TODO use JsonNode.equls to check json structure equal

		// step 1, go to outcome repository to get the response
		Outcome ou = remoteFunctionService.loadOutcome(functionName, hash);
		if (ou != null) {
			// TODO increate invoke count, should be async
			ou.setInvokeCount(ou.getInvokeCount() + 1);
			remoteFunctionService.saveOutcome(ou);
			return new ResponseEntity<String>(ou.getResponse(), null, HttpStatus.OK);
		}

		// step 2, if not found, run js code to get the response
		// is failed, return http code: 404 not found
		SourceHub sh = remoteFunctionService.loadSourceHub(functionName, version);
		if (sh == null) {
			// not exist
			return new ResponseEntity<String>("function not found.", null, HttpStatus.BAD_REQUEST);
		}
		if (!Constants.Source_HUB_STATUS.published.equals(sh.getStatus())) {
			// status is not right
			return new ResponseEntity<String>("function is not published.", null, HttpStatus.BAD_REQUEST);
		}

		String response = runJavascript(sh.getSource(), jsonRequest);

		ou = new Outcome();
		ou.setName(sh.getName());
		ou.setHash(hash);
		ou.setVersion(sh.getVersion());

		ou.setRequest(jsonRequest);
		ou.setResponse(response);

		remoteFunctionService.saveOutcome(ou);
		return new ResponseEntity<String>(response, null, HttpStatus.OK);

		// something wrong
		// return new ResponseEntity<String>("unknown error.", null,
		// HttpStatus.INTERNAL_SERVER_ERROR);

	}*/

	private String runJavascript(String source, String request) throws IOException, ScriptException, NoSuchMethodException {

		// request = request.replaceAll("\"", "");
		// logger.info("runJavascript:" + request);
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("graal.js");

		// add build-in lang support
		//String code = "load('classpath:js/lang.js')\r\n" + source;
		String code = ResourceUtils.readResource("js/lang.js") +"\r\n" + source;


		engine.eval(code);

		Object json = engine.eval("Java.asJSONCompatible(" + request + ")");

		Invocable invocable = (Invocable) engine;
		Object funcResult = invocable.invokeFunction("main", json);
		return (String) funcResult;

	}

//TODO not used
	@RequestMapping(method = RequestMethod.GET, value = "test/{functionName}/{version}/{encodedJson}")
	public ResponseEntity<String> get(HttpSession session, @PathVariable String functionName,
			@PathVariable String version, @PathVariable String encodedJson) throws Exception {

		// TODO use JsonNode.equls to check json structure equal

		// step 1, go to function repository to check js source, if not exist or status
		// is failed, return http code: 404 not found
		SourceHub sh = remoteFunctionService.loadSourceHub(functionName, version);
		if (sh == null) {
			// not exist
			return new ResponseEntity<String>("function not found.", null, HttpStatus.BAD_REQUEST);
		}
		if (!Constants.Source_HUB_STATUS.published.equals(sh.getStatus())) {
			// status is not right
			return new ResponseEntity<String>("function is not published.", null, HttpStatus.BAD_REQUEST);
		}

		// step 2, if js source exist, create a task in task repository,
		String jsonRequest = EncodingUtils.decode(encodedJson);
		String versionAndRequest = EncodingUtils.createHash(version, jsonRequest);

		Task task = remoteFunctionService.loadTask(functionName, versionAndRequest);
		if (task == null) {
			task = new Task();
			BeanUtils.copyProperties(sh, task);
			//task.setVersionAndRequest(versionAndRequest);

			remoteFunctionService.saveTask(task);
		} else {

			if (Constants.TASK_STATUS.success.equals(task.getStatus())) {
				// if success, return response
				return new ResponseEntity<String>(task.getResponse(), null, HttpStatus.OK);

			} else if (Constants.TASK_STATUS.failed.equals(task.getStatus())) {

				// if failed before, reset status and failCount
				task.setStatus(Constants.TASK_STATUS.pending);
				task.setFailCount(task.getFailCount() + 1);
			}
		}

		// something wrong
		return new ResponseEntity<String>("unknown error.", null, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	/*-
		@RequestMapping(method = RequestMethod.GET, value = "status/{functionName}/{version}/{encodedJson}")
		public ResponseEntity<String> status(HttpSession session, @PathVariable String functionName,
				@PathVariable String version, @PathVariable String encodedJson) throws Exception {
	
			String jsonRequest = EncodingUtils.decode(encodedJson);
	
			String versionAndRequest = EncodingUtils.createVersionAndRequestEncoding(version, jsonRequest);
	
			SourceHub sh = remoteFunctionService.loadSourceHub(functionName, version);
			if (sh == null) {
				// not exist
				return new ResponseEntity<String>(" error. \r\n", null, HttpStatus.NOT_FOUND);
			}
	
			return new ResponseEntity<String>(sh.getStatus(), null, HttpStatus.OK);
	
		}
	
		@RequestMapping(method = RequestMethod.GET, value = "run/{functionName}/{version}/{encodedJson}")
		public ResponseEntity<String> run(HttpSession session, @PathVariable String functionName,
				@PathVariable String version, @PathVariable String encodedJson) throws Exception {
	
			String jsonRequest = EncodingUtils.decode(encodedJson);
	
			// step 1, go to function repository to check js source, if not exist or status
			// is failed, return http code: 404 not found
			SourceHub sh = remoteFunctionService.loadSourceHub(functionName, version);
			if (sh == null) {
				// not exist
				return new ResponseEntity<String>(" error. \r\n", null, HttpStatus.BAD_REQUEST);
			}
			if (!Constants.Source_HUB_STATUS.published.equals(sh.getStatus())) {
				// status is not right
				return new ResponseEntity<String>(" error. \r\n", null, HttpStatus.BAD_REQUEST);
			}
	
			// step 2, if js source exist, create a task in task repository,
			String versionAndRequest = EncodingUtils.createVersionAndRequestEncoding(version, jsonRequest);
	
			Task task = remoteFunctionService.loadTask(functionName, versionAndRequest);
			if (task == null) {
				task = new Task();
				BeanUtils.copyProperties(sh, task);
				task.setVersionAndRequest(versionAndRequest);
	
			} else {
				// TODO what if the task status is different from expected
				if (Constants.TASK_STATUS.success.equals(task.getStatus())) {
					// do nothing, ready to retrieve the data
				} else if (Constants.TASK_STATUS.failed.equals(task.getStatus())) {
					// if failed before, reset status and failCount
					task.setStatus(Constants.TASK_STATUS.pending);
					task.setFailCount(task.getFailCount() + 1);
				}
			}
	
			remoteFunctionService.saveTask(task);
	
			return new ResponseEntity<String>("", null, HttpStatus.OK);
	
		}
	*/
	// validate function, if good, return sampleResponse to check syntax error
	@RequestMapping(method = RequestMethod.GET, value = "validate/{functionName}/{version}")
	public ResponseEntity<String> validate(HttpSession session, @PathVariable String functionName,
			@PathVariable String version) {

		logger.info(MessageFormat.format("RemoteFunctionController.validate {0}.{1}", functionName, version));

		SourceHub sh = remoteFunctionService.loadSourceHub(functionName, version);
		if (sh == null) {
			// not exist
			return new ResponseEntity<String>("function not found.", null, HttpStatus.BAD_REQUEST);
		}
		if (!Constants.Source_HUB_STATUS.published.equals(sh.getStatus())) {
			// status is not right
			return new ResponseEntity<String>("function is not published.", null, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<String>(sh.getSampleResponse(), null, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "test-case/{functionName}/{version}")
	public ResponseEntity<String> testCase(HttpSession session, @PathVariable String functionName,
			@PathVariable String version) {

		logger.info(MessageFormat.format("RemoteFunctionController.test-case {0}.{1}", functionName, version));

		SourceHub sh = remoteFunctionService.loadSourceHub(functionName, version);
		if (sh == null) {
			// not exist
			return new ResponseEntity<String>("function not found.", null, HttpStatus.BAD_REQUEST);
		}
		if (!Constants.Source_HUB_STATUS.published.equals(sh.getStatus())) {
			// status is not right
			return new ResponseEntity<String>("function is not published.", null, HttpStatus.BAD_REQUEST);
		}

		try {
			String response = runJavascript(sh.getSource(), sh.getSampleRequest());
			return new ResponseEntity<String>(response, null, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
