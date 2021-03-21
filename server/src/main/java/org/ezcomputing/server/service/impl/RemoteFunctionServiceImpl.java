package org.ezcomputing.server.service.impl;

import java.util.List;

import org.ezcomputing.server.dao.AppDao;
import org.ezcomputing.server.dao.JobDao;
import org.ezcomputing.server.dao.ModuleDao;
import org.ezcomputing.server.dao.OutcomeDao;
import org.ezcomputing.server.dao.SourceHubDao;
import org.ezcomputing.server.dao.TaskDao;
import org.ezcomputing.server.dao.dto.App;
import org.ezcomputing.server.dao.dto.Job;
import org.ezcomputing.server.dao.dto.JobScheduler;
import org.ezcomputing.server.dao.dto.Module;
import org.ezcomputing.server.dao.dto.Outcome;
import org.ezcomputing.server.dao.dto.SourceHub;
import org.ezcomputing.server.dao.dto.Task;
import org.ezcomputing.server.service.RemoteFunctionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * @author Bo Zhao
 *
 */
@Service
public class RemoteFunctionServiceImpl implements RemoteFunctionService {

	private static final Logger logger = LoggerFactory.getLogger(RemoteFunctionServiceImpl.class);

	@Autowired
	private TaskDao taskDao;

	@Autowired
	private SourceHubDao sourceHubDao;

	@Autowired
	private JobDao jobDao;
	
	@Autowired
	private OutcomeDao outcomeDao;
	
	@Autowired
	private ModuleDao moduleDao;
	
	@Autowired
	private AppDao appDao;

	public void saveTask(Task task) {
		taskDao.save(task);
	}

	public Task loadTask(String name, String id) {
		return taskDao.load(name, id);
	}

	public void saveSourceHub(SourceHub sh) {
		sourceHubDao.save(sh);
	}

	public SourceHub loadSourceHub(String name, String version) {
		return sourceHubDao.load(name, version);
	}

	public List<Task> getPendingTask() {
		return taskDao.getPendingTask();
	}

	public void saveJob(Job job) {
		jobDao.save(job);
	}

	public Job loadJob(String id) {
		return jobDao.load(id);
	}
	
	public Job loadJobsForWorker(){
		return jobDao.loadJobsForWorker();
	}
	
	public void deleteJob(Job job) {
		 jobDao.delete(job);
	}
	public List<Job> getJobsByOwner(String ownerId) {
		return jobDao.getJobsByOwner(ownerId);
	}
	public void saveOutcome(Outcome ou) {
		outcomeDao.save(ou);
	}

	public Outcome loadOutcome(String name, String hash) {
		return outcomeDao.load(name, hash);
	}
	
	public void updateJobStatus(String jobId, String status) {
		jobDao.updateJobStatus(jobId, status);
	}
	
	public List<JobScheduler> getEveryDayJobScheduler(){
		return jobDao.getEveryDayJobScheduler();
	}
	
	public List<Module> getModulesByOwner(String ownerId) {
		return moduleDao.getModulesByOwner(ownerId);
	}
	
	public void saveModule(Module module) {
		this.moduleDao.save(module);
	}
	
	public Module loadModule(String namespace, String version) {
		return this.moduleDao.load(namespace, version);
	}
	
	public void deleteModule(Module module) {
		this.moduleDao.delete(module);
	}
	
	public void saveApp(App app) {
		this.appDao.save(app);
	}
	
	public App loadApp(String id) {
		return this.appDao.load(id);
	}
	
	public List<App> loadAppList(){
		return this.appDao.loadAppList();
	}
}
