package org.ezcomputing.worker.domain;
/**
 * @author Bo Zhao
 *
 */
public class Constants {
	
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
