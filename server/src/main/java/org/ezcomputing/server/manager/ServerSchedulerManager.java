package org.ezcomputing.server.manager;

import java.util.Date;
import java.util.List;

import org.ezcomputing.server.dao.dto.Constants;
import org.ezcomputing.server.dao.dto.JobScheduler;
import org.ezcomputing.server.service.HeartBeatService;
import org.ezcomputing.server.service.RemoteFunctionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
/**
 * @author Bo Zhao
 *
 */
@Component
public class ServerSchedulerManager {

	private static final Logger logger = LoggerFactory.getLogger(ServerSchedulerManager.class);
	
	@Autowired
	private TaskScheduler executor;

	@Autowired
	private HeartBeatService heartBeatService;
	
	@Autowired
	private RemoteFunctionService remoteFunctionService;
	
	/*-
	@Scheduled(fixedDelay = 900000, initialDelay=3000) // 15minutes	
	public void scheduleMapCleanUp()  {
		logger.info("scheduleMapCleanUp starting.");
		heartBeatService.scheduledMapCleanUp();
		
		
	}
	*/
	
	@Scheduled(fixedDelay = 900000, initialDelay=3000) // 15minutes	
	public void scheduleDeadWorkerCleanup()  {
		logger.info("scheduleDeadWorkerCleanup starting.");
		heartBeatService.scheduleDeadWorkerCleanup();
	}
	
	@Scheduled(cron = "0 1 0 * * ?") //everyday at 0:01am
	public void scheduleJobScheduler()  {
		logger.info("scheduleJobScheduler starting.");
		
		List<JobScheduler> list = remoteFunctionService.getEveryDayJobScheduler();
		
		for(JobScheduler scheduler: list) {
			Date date = new Date();
			date.setHours(scheduler.getHours());
			date.setMinutes(scheduler.getMinutes());
			
			executor.schedule(new RunOnWorkerTask(remoteFunctionService,scheduler.getJobId()), date);
		}
		
	
	}
}

class RunOnWorkerTask implements Runnable{
	
	private String jobId;
	
	private RemoteFunctionService service;
	
	public RunOnWorkerTask(RemoteFunctionService service, String jobId) {
		this.service = service;
		this.jobId = jobId;
	}
	
	@Override
	public void run() {
		service.updateJobStatus(jobId, Constants.JOB_STATUS.readyToRun);
	}
	
}
