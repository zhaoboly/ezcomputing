package org.ezcomputing.server.dao;

import java.util.List;

import org.ezcomputing.server.dao.dto.Task;
/**
 * @author Bo Zhao
 *
 */
public interface TaskDao {

	public void save(Task task);

	public Task load(String name, String id);
	
	//get all pending task
	public List<Task> getPendingTask();

}
