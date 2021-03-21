package org.ezcomputing.server.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ezcomputing.server.dao.ModuleDao;
import org.ezcomputing.server.dao.dto.Module;
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
public class ModuleDaoImpl implements ModuleDao{
	
	private static final Logger logger = LoggerFactory.getLogger(ModuleDaoImpl.class);

	@Autowired
	private DynamoDBMapper dynamoDBMapper;


	public void save(Module module) {
		dynamoDBMapper.save(module);
	}
	
	public Module load(String namespace, String version) {
		Module module = this.dynamoDBMapper.load(Module.class, namespace, version);
		
		return module;
	}
	
	public void delete(Module module) {
		this.dynamoDBMapper.delete(module);
	}
	
	public List<Module> getModulesByOwner(String ownerId) {
		Map<String, AttributeValue> map = new HashMap<String, AttributeValue>();
		
		map.put(":val1", new AttributeValue().withS(ownerId));

		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("ownerId = :val1 ").withExpressionAttributeValues(map);

		List<Module> list = dynamoDBMapper.scan(Module.class, scanExpression);
		return list;
	}
	
	public void updateModuleStatus(String namespace, String version, String status) {
		Module module = this.dynamoDBMapper.load(Module.class, namespace, version);
		module.setStatus(status);
		dynamoDBMapper.save(module);
		
	}
	
}
