package org.ezcomputing.server.dao.impl;

import org.ezcomputing.server.dao.OutcomeDao;
import org.ezcomputing.server.dao.dto.Outcome;
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
public class OutcomeDaoImpl implements OutcomeDao {

	private static final Logger logger = LoggerFactory.getLogger(OutcomeDaoImpl.class);
	
	@Autowired
	private DynamoDBMapper dynamoDBMapper;

	public void save(Outcome ou) {
		dynamoDBMapper.save(ou);
	}

	public Outcome load(String name, String hash) {
		Outcome ou = dynamoDBMapper.load(Outcome.class, name, hash);
		
		return ou;
	}

}
