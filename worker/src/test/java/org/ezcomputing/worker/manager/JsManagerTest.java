package org.ezcomputing.worker.manager;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;

public class JsManagerTest {

	private static final Logger logger = LoggerFactory.getLogger(JsManagerTest.class);

	
	@Test
	public void test() {
		JsManager jsManager = new JsManager();
		
		String source = "include(\"http\");   include(\'ez_computing_lang\');  ";
		
		List<String> list = jsManager.retrieveLoadModules(source);
		
		assertTrue(list.contains("http"));
		assertTrue(list.contains("ez_computing_lang"));
	}

}
