package org.ezcomputing.server.dao.dto;
/**
 * @author Bo Zhao
 *
 */
public class Constants {
	
	public static class API_STATUS {
		public static final String underReview = "underReview";
		
	}
	
	public static class MODULE_STATUS {
		public static final String pending = "pending";
		public static final String success = "success";
		public static final String failed = "failed";
		public static final String published = "published";
	}
	
	public static class JOB_SCHEDULER_TYPE {
		public static final String EveryDay = "EveryDay";
		public static final String EveryMonth = "EveryMonth";
		public static final String EveryWeek = "EveryWeek";
	}
	
	
	public static class WORKER_STATUS {
		public static final String online = "online";
		public static final String offline = "offline";
	}
	
	//pending, published, deleted, expired
	public static class Source_HUB_STATUS{
		public static String pending = "pending";
		public static String published = "published";
		public static String deleted = "deleted";
		public static String expired = "expired";
	}
	
	////pending, success, failed, waitForDependency, running
	public static class TASK_STATUS{
		public static String pending = "pending";
		public static String success = "success";
		public static String failed = "failed";
		public static String waitForDependency = "waitForDependency";
		public static String running = "running";
		
	}
	
	public static class JOB_V_STATUS{
		public static String pri = "private";
		public static String published = "published";
	
	}
	
	
	////pending, success, failed, waitForDependency, running
	public static class JOB_STATUS{
		public static String pending = "pending";
		public static String validated = "validated";
		public static String readyToRun = "readyToRun";
		public static String success = "success";
		public static String failed = "failed";
		public static String waitForDependency = "waitForDependency";
		public static String running = "running";
		
	}
	
	public static class JOB_TYPE{
		public static String original = "original";
		public static String fromApp = "fromApp";
		
		
	}
	
	
	public static class LANGUAGE_SUPPORT{
		public static String js = "js";
		public static String python = "python";
	}
	
	public static class OUTCOME_STATUS{
		public static String pending = "pending";
		public static String success = "success";
		public static String failed = "failed";
	}
}
