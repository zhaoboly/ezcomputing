package org.ezcomputing.server.web.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.ezcomputing.server.dao.dto.Constants;
import org.ezcomputing.server.dao.dto.Worker;
import org.ezcomputing.server.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class HeartBeatController {

	private static final Logger logger = LoggerFactory.getLogger(HeartBeatController.class);

	@Autowired
	private UserService userService;
	
	//@Autowired
	//private HeartBeatService heartBeatService;
	
	@RequestMapping(method = RequestMethod.GET, value = "heartbeat/{id}")
	public ResponseEntity<String> heartbeat(HttpSession session, @PathVariable String id) throws Exception {

		//logger.info("heartbeat:"+ id);
		
		if(StringUtils.isBlank(id)) {
			return new ResponseEntity<String>("", null, HttpStatus.BAD_REQUEST);
		}
		
		
		
		Worker worker = userService.loadWorker(id);
		if(worker==null) {
			logger.warn("receive invalid heartbeat key:"+ id);
			
		}else {
			if(Constants.WORKER_STATUS.offline.equals(worker.getWorkerStatus())) {
				worker.setWorkerStatus(Constants.WORKER_STATUS.online);
			}
			userService.saveWorker(worker);
		}
		
		
		return new ResponseEntity<String>("", null, HttpStatus.OK);
		
	}
	
}

