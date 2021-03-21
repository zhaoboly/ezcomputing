package org.ezcomputing.server.dao;

import org.ezcomputing.server.dao.dto.Outcome;
/**
 * @author Bo Zhao
 *
 */
public interface OutcomeDao {

	public void save(Outcome ou);

	public Outcome load(String name, String hash) ;
}
