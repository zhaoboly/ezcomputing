package org.ezcomputing.worker.graalvm.member;

import org.ezcomputing.worker.manager.JsManager;
import org.graalvm.polyglot.HostAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * Language level member, should always be included
 * 
 * @author Bo Zhao
 *
 */
public class Lang {

	private static final Logger logger = LoggerFactory.getLogger(Lang.class);
	
	
	private JsManager jsManager;
	
	public Lang(JsManager jsManager) {
		this.jsManager = jsManager;
	}
	
	@HostAccess.Export
	public String call(String apiId, String request) {
		logger.info("call api:"+ apiId );
		
		String res = jsManager.runJavascript(apiId, request);
		
		return res;
		
	}
}
