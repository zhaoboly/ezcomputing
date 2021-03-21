package org.ezcomputing.server.dao;


import java.util.List;

import org.ezcomputing.server.dao.dto.Job;
import org.ezcomputing.server.dao.dto.JobScheduler;
/**
 * @author Bo Zhao
 *
 */
public interface JobDao {

	public void save(Job job);
	
	public Job load(String id);
	
	public Job loadJobsForWorker();
	
	public void delete(Job job);
	
	public List<Job> getJobsByOwner(String ownerId) ;
	
	public void updateJobStatus(String jobId, String status);
	
	public List<JobScheduler> getEveryDayJobScheduler();
}
