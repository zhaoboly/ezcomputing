package org.ezcomputing.server.dao.impl;

import org.ezcomputing.server.dao.SourceHubDao;
import org.ezcomputing.server.dao.dto.SourceHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
/**
 * @author Bo Zhao
 *
 */
@Repository
public class SourceHubDaoImpl implements SourceHubDao {

	private static final Logger logger = LoggerFactory.getLogger(SourceHubDaoImpl.class);
	
	@Autowired
	private DynamoDBMapper dynamoDBMapper;

	public void save(SourceHub sh) {
		dynamoDBMapper.save(sh);
	}

	public SourceHub load(String name, String version) {
		SourceHub sh = dynamoDBMapper.load(SourceHub.class, name, version);
		
		return sh;
	}

}
