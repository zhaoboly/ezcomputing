package org.ezcomputing.server.dao.impl;

import org.ezcomputing.server.dao.UserDao;
import org.ezcomputing.server.dao.dto.User;
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
public class UserDaoImpl implements UserDao {

	private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

	@Autowired
	private DynamoDBMapper dynamoDBMapper;

	@Override
	public void save(User user) {
		dynamoDBMapper.save(user);
	}

	public User load(String email) {
		return dynamoDBMapper.load(User.class, email);
		
	}
}
