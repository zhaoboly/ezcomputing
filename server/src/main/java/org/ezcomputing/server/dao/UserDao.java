package org.ezcomputing.server.dao;

import org.ezcomputing.server.dao.dto.User;
/**
 * @author Bo Zhao
 *
 */
public interface UserDao {

	public void save(User user);
	
	public User load(String email);
}
