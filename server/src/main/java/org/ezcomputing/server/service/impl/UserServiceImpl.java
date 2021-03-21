package org.ezcomputing.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.ezcomputing.server.dao.UserDao;
import org.ezcomputing.server.dao.WorkerDao;
import org.ezcomputing.server.dao.dto.User;
import org.ezcomputing.server.dao.dto.Worker;
import org.ezcomputing.server.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * @author Bo Zhao
 *
 */
@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(RemoteFunctionServiceImpl.class);

	@Autowired
	private WorkerDao workerDao;
	
	@Autowired
	private UserDao userDao;

	
	public void saveWorker(Worker wo) {
		workerDao.save(wo);
	}

	public Worker loadWorker(  String id) {
		return workerDao.load(id);
	}

	public void deleteWorker(Worker wo) {
		workerDao.delete(wo);
	}
	
	public void saveUser(User user) {
		userDao.save(user);
	}
	
	public User loadUser(String email) {
		return userDao.load(email);
	}

	public List<Worker> getWorkerListByOwner(String email){
		User user = userDao.load(email);
		
		List<Worker> list = new ArrayList<Worker>();
		for(String workerId: user.getWorkerList()) {
			list.add(workerDao.load( workerId));
		}
		return list;
	}
	
	public List<Worker> getPublicWorkerList(){
		return workerDao.getPublicWorkerList();
	}
	
}
