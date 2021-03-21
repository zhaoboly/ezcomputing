package org.ezcomputing.server.service.impl;

import org.ezcomputing.server.dao.WorkerDao;
import org.ezcomputing.server.service.HeartBeatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * @author Bo Zhao
 *
 */
@Service
public class HeartBeatServiceImpl implements HeartBeatService {
	
	private static final Logger logger = LoggerFactory.getLogger(RemoteFunctionServiceImpl.class);

	@Autowired
	private WorkerDao workerDao;
	
	@Override
	public void scheduleDeadWorkerCleanup() {
		workerDao.scheduleDeadWorkerCleanup();
	}
}
	
	/* String: workerId
	 * Long: timestamp
	 */
	
	//private static ConcurrentHashMap<String, HeartBeatInfo> heartBeatMap = new ConcurrentHashMap<String, HeartBeatInfo> ();

	/*-
	@Async
	public void receiveHeartBeat(String workerId) {
		
		long now = (new Date()).getTime();
		logger.info("receive heartbeat:" + workerId+";at:"+ now);
		
		if(heartBeatMap.containsKey(workerId)) {
			heartBeatMap.get(workerId).setHeartBeatTime(now);
			
			
		}else {
			//try to load worker into map
			Worker worker = workerDao.load(workerId);
			if(worker==null) {
				logger.warn("receive invalid heartbeat key:"+ workerId);
				
			}else {
				logger.info("add worker to map:"+ workerId);
				//update status to online
				if(Constants.WORKER_STATUS.offline.equals(worker.getWorkerStatus())) {
					worker.setWorkerStatus(Constants.WORKER_STATUS.online);
				}
				workerDao.save(worker); //refresh modify date
				
				// add to map
				heartBeatMap.put(workerId, new HeartBeatInfo(workerId, now, now));
			}
			
		}
		
	}
	*/
	
	/*
	 * periodic check heartbeat map, if timestamp greater than 15 minutes, remove that workerId
	 */
	//@Scheduled(fixedDelay = 900000, initialDelay=10000) // 15minutes
	
	//TODO consider to change to Redis in cluster environment
	/*-
	public void scheduledMapCleanUp()  {
		
		
		long now = (new Date()).getTime();
		
		try {
			for(String key: heartBeatMap.keySet()) {
				HeartBeatInfo info =heartBeatMap.get(key);
				logger.info("scheduledMapCleanUp for:"+ info.toJson());
				
				//Long lastHeartBeat = heartBeatMap.get(key).getHeartBeatTime();
				
				//if heartbeat timestamp is 15 minutes ago, remove it
				if(( now- info.getHeartBeatTime()) > 900000) {
					logger.info("remove workerId:"+ key);
					//heartbeat stop, remove it
					heartBeatMap.remove(key);
					
					Worker worker = workerDao.load(key);
					if(worker==null) {
						//have been deleted by user, do nothing
					}else {
						
						
							worker.setWorkerStatus(Constants.WORKER_STATUS.offline);
							workerDao.save(worker);
						
						
					}
				}
				
				//every one hour, flush lastUpdateDate to database
				if( now - info.getStartTime()  > 3600000) {
					Worker worker = workerDao.load(key);
					if(worker==null) {
						//have been deleted by user, do nothing
						heartBeatMap.remove(key);
					}else {
						workerDao.save(worker);
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		

		
	}
	
	@Override
	public void scheduleDeadWorkerCleanup() {
		workerDao.scheduleDeadWorkerCleanup();
		
		List<Worker> list = workerDao.loadOnlineWorkerList();
		long now = (new Date()).getTime();
		
		for(Worker worker: list) {
			//if 24 hours no update, change status to offline
			logger.info("checking worker:"+ worker.getId());
			if((now - worker.getUpdateDate().getTime()) > 86400000) {
				logger.warn("worker set to offline :"+ worker.getId());
				worker.setWorkerStatus(Constants.WORKER_STATUS.offline);
				workerDao.save(worker);
			}
		}
		
		
	}
	
	*/


/*-
class HeartBeatInfo {
	private String workerId;
	private Long startTime;
	private Long heartBeatTime;
	
	
	public HeartBeatInfo(String workerId, Long startTime, Long heartBeatTime) {
		this.workerId = workerId;
		this.startTime = startTime;
		this.heartBeatTime = heartBeatTime;
	}
	
	public String toJson() {
		return JsonUtils.writeValueAsString(this);
	}
	
	public String getWorkerId() {
		return workerId;
	}
	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public Long getHeartBeatTime() {
		return heartBeatTime;
	}
	public void setHeartBeatTime(Long heartBeatTime) {
		this.heartBeatTime = heartBeatTime;
	}
	
}
*/