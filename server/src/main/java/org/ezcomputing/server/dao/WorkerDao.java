package org.ezcomputing.server.dao;

import java.util.List;

import org.ezcomputing.server.dao.dto.Worker;
/**
 * @author Bo Zhao
 *
 */
public interface WorkerDao {

	public void save(Worker wo);

	public Worker load(String id);
	
	public void delete(Worker wo);
	
	public List<Worker> loadOnlineWorkerList() ;
	
	public void scheduleDeadWorkerCleanup() ;
	
	public List<Worker> getPublicWorkerList();
}
