package org.ezcomputing.server.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ezcomputing.server.dao.TaskDao;
import org.ezcomputing.server.dao.dto.Constants;
import org.ezcomputing.server.dao.dto.Task;
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
public class TaskDaoImpl implements TaskDao {

	private static final Logger logger = LoggerFactory.getLogger(TaskDaoImpl.class);

	@Autowired
	private DynamoDBMapper dynamoDBMapper;

	public void save(Task task) {
		dynamoDBMapper.save(task);
	}

	public Task load(String name, String id) {
		Task task = dynamoDBMapper.load(Task.class, name, id);

		return task;
	}

	public List<Task> getPendingTask() {
		
		Map<String, AttributeValue> map = new HashMap<String, AttributeValue>();
		
		map.put(":val1", new AttributeValue().withS(Constants.TASK_STATUS.pending));

		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("taskStatus = :val1 ").withExpressionAttributeValues(map);

		List<Task> list = dynamoDBMapper.scan(Task.class, scanExpression);
		return list;
	}

}
