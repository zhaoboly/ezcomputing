package org.ezcomputing.server.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ezcomputing.server.dao.JobDao;
import org.ezcomputing.server.dao.dto.Constants;
import org.ezcomputing.server.dao.dto.Job;
import org.ezcomputing.server.dao.dto.JobScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
/**
 * @author Bo Zhao
 *
 */
@Repository
public class JobDaoImpl implements JobDao {

	private static final Logger logger = LoggerFactory.getLogger(JobDaoImpl.class);

	@Autowired
	private DynamoDBMapper dynamoDBMapper;

	public void save(Job job) {
		dynamoDBMapper.save(job);
	}

	public Job load(String id) {
		Job job = dynamoDBMapper.load(Job.class, id);

		return job;
	}
	
	public void delete(Job job) {
		dynamoDBMapper.delete(job);
	}

	public List<Job> getJobsByOwner(String ownerId) {
		
		Map<String, AttributeValue> map = new HashMap<String, AttributeValue>();
		
		map.put(":val1", new AttributeValue().withS(ownerId));

		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("ownerId = :val1 ").withExpressionAttributeValues(map);

		List<Job> list = dynamoDBMapper.scan(Job.class, scanExpression);
		return list;
	}
	
	public Job loadJobsForWorker(){
		Map<String, AttributeValue> map = new HashMap<String, AttributeValue>();
		
		map.put(":val1", new AttributeValue().withS(Constants.JOB_STATUS.readyToRun));

		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("jobStatus = :val1 ").withExpressionAttributeValues(map);

		List<Job> list = dynamoDBMapper.scan(Job.class, scanExpression.withLimit(1));
		
		Iterator it = dynamoDBMapper.scan(Job.class, scanExpression.withLimit(1)).iterator();
		if ( it.hasNext() ) {
			Job job = (Job) it.next();
			return job;
	    }else {
	    	return null;
	    }
		
	}

	public void updateJobStatus(String jobId, String status) {
		Job job = dynamoDBMapper.load(Job.class, jobId);
		
		job.setStatus(status);
		dynamoDBMapper.save(job);
	}
	
	public List<JobScheduler> getEveryDayJobScheduler(){
		
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression(" attribute_exists(scheduler) ");

		List<Job> list = dynamoDBMapper.scan(Job.class, scanExpression);
		

		List<JobScheduler> results = new ArrayList<JobScheduler>();
		
		for(Job job: list) {
			results.add(job.getScheduler());
		}
		
		return results;
	}
	
}
