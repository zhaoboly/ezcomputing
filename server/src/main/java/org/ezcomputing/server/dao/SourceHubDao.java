package org.ezcomputing.server.dao;

import org.ezcomputing.server.dao.dto.SourceHub;
/**
 * @author Bo Zhao
 *
 */
public interface SourceHubDao {

	public void save(SourceHub sh);
	
	public SourceHub load(String name, String version);
	
	
}
