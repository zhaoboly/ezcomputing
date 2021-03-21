package org.ezcomputing.server.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ezcomputing.server.dao.WorkerDao;
import org.ezcomputing.server.dao.dto.Constants;
import org.ezcomputing.server.dao.dto.Worker;
import org.ezcomputing.server.util.DateUtil;
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
public class WorkerDaoImpl implements WorkerDao {

	private static final Logger logger = LoggerFactory.getLogger(WorkerDaoImpl.class);

	@Autowired
	private DynamoDBMapper dynamoDBMapper;

	@Override
	public void save(Worker wo) {
		dynamoDBMapper.save(wo);
	}

	@Override
	public Worker load( String id) {
		Worker wo = dynamoDBMapper.load(Worker.class,  id);

		return wo;
	}
	@Override
	public void delete(Worker wo) {
		dynamoDBMapper.delete(wo);
	}
	
	@Override
	public List<Worker> loadOnlineWorkerList() {
		Map<String, AttributeValue> map = new HashMap<String, AttributeValue>();
		
		map.put(":val1", new AttributeValue().withS(Constants.WORKER_STATUS.online));

		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("workerStatus = :val1 ").withExpressionAttributeValues(map);

		List<Worker> list = dynamoDBMapper.scan(Worker.class, scanExpression);
		return list;
	}
	
	public List<Worker> getPublicWorkerList(){
		Map<String, AttributeValue> map = new HashMap<String, AttributeValue>();
		
		map.put(":val1", new AttributeValue().withN("1"));
		map.put(":val2", new AttributeValue().withS(Constants.WORKER_STATUS.online));

		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("verified = :val1 and workerStatus = :val2 ").withExpressionAttributeValues(map);

		List<Worker> list = dynamoDBMapper.scan(Worker.class, scanExpression);
		return list;
	}
	
	@Override
	public void scheduleDeadWorkerCleanup() {
		
		//logger.info("scheduleDeadWorkerCleanup starting");
		
		//set time to 15 minutes before
		Long time  = new Date().getTime() - 15 * DateUtil.ONE_MINUTE_IN_MILLIS;
		
		Map<String, AttributeValue> map = new HashMap<String, AttributeValue>();
		map.put(":val1", new AttributeValue().withN(String.valueOf(time)));
		map.put(":val2", new AttributeValue().withS(Constants.WORKER_STATUS.online));
		
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("updateTimestamp < :val1 and workerStatus = :val2 ").withExpressionAttributeValues(map);

		List<Worker> list = dynamoDBMapper.scan(Worker.class, scanExpression);
	
		for(Worker worker : list) {
			logger.info("set ant to offline:"+ worker.toString());
			worker.setWorkerStatus(Constants.WORKER_STATUS.offline);
			dynamoDBMapper.save(worker);
		}
	}
		
}
