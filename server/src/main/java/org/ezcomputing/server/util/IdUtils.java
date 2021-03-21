package org.ezcomputing.server.util;
/**
 * @author Bo Zhao
 *
 */
public class IdUtils {

	public static String generateWorkerId() {
		String id = Base36Utils.encode(System.currentTimeMillis());
		return id;
	}
}
