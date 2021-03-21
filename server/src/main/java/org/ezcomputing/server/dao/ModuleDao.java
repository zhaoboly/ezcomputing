package org.ezcomputing.server.dao;


import java.util.List;

import org.ezcomputing.server.dao.dto.Module;
/**
 * @author Bo Zhao
 *
 */
public interface ModuleDao {

	public void save(Module module);
	
	public Module load(String namespace, String version);
	
	public void delete(Module module);
	
	public List<Module> getModulesByOwner(String ownerId) ;
	
	public void updateModuleStatus(String namespace, String version, String status);
	
	
}
