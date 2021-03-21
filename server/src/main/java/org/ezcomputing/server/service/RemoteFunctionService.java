package org.ezcomputing.server.service;

import java.util.List;

import org.ezcomputing.server.dao.dto.App;
import org.ezcomputing.server.dao.dto.Job;
import org.ezcomputing.server.dao.dto.JobScheduler;
import org.ezcomputing.server.dao.dto.Module;
import org.ezcomputing.server.dao.dto.Outcome;
import org.ezcomputing.server.dao.dto.SourceHub;
import org.ezcomputing.server.dao.dto.Task;
/**
 * @author Bo Zhao
 *
 */
public interface RemoteFunctionService {

	
	public void saveTask(Task task);

	public Task loadTask(String name, String id);

	public void saveSourceHub(SourceHub sh);

	public SourceHub loadSourceHub(String name, String version);
	
	public List<Task> getPendingTask() ;
	
	public void saveJob(Job job);
	
	public Job loadJob(String id);
	
	public void deleteJob(Job job);
	
	public List<Job> getJobsByOwner(String ownerId) ;
	
	public Job loadJobsForWorker();
	
	public void saveOutcome(Outcome ou) ;

	public Outcome loadOutcome(String name, String hash) ;
	
	public void updateJobStatus(String jobId, String status);
	
	public List<JobScheduler> getEveryDayJobScheduler();
	
	public List<Module> getModulesByOwner(String ownerId);
	
	public void saveModule(Module module);
	
	public Module loadModule(String namespace, String version);
	
	public void deleteModule(Module module);
	
	public void saveApp(App app);
	
	public App loadApp(String id);
	
	public List<App> loadAppList();

}
