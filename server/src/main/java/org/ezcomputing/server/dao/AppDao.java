package org.ezcomputing.server.dao;

import java.util.List;

import org.ezcomputing.server.dao.dto.App;
/**
 * @author Bo Zhao
 *
 */
public interface AppDao {

	public void save(App app);
	
	public App load(String id);
	
	public List<App> loadAppList();
}
