package org.ezcomputing.server.service;
/**
 * @author Bo Zhao
 *
 */
public interface HeartBeatService {

	
	public void scheduleDeadWorkerCleanup();

}
