package org.ezcomputing.server.service;

import java.util.List;

import org.ezcomputing.server.dao.dto.User;
import org.ezcomputing.server.dao.dto.Worker;
/**
 * @author Bo Zhao
 *
 */
public interface UserService {

	public void saveWorker(Worker wo) ;

	public Worker loadWorker( String id) ;
	
	public void deleteWorker(Worker wo) ;
	
	public List<Worker> getWorkerListByOwner(String email);
	
	public List<Worker> getPublicWorkerList();
	
	
	public void saveUser(User user);
	
	public User loadUser(String email);
	
	

}
