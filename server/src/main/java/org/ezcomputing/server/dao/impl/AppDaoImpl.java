package org.ezcomputing.server.dao.impl;

import java.util.List;

import org.ezcomputing.server.dao.AppDao;
import org.ezcomputing.server.dao.dto.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
/**
 * @author Bo Zhao
 *
 */
@Repository
public class AppDaoImpl implements AppDao{

	private static final Logger logger = LoggerFactory.getLogger(AppDaoImpl.class);

	@Autowired
	private DynamoDBMapper dynamoDBMapper;

	public void save(App app) {
		dynamoDBMapper.save(app);
	}
	
	public App load(String id) {
		return dynamoDBMapper.load(App.class, id);
	}
	
	public List<App> loadAppList(){
		List<App> list = dynamoDBMapper.scan(App.class, new DynamoDBScanExpression());
		return list;
	}
}
