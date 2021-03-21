package org.ezcomputing.server.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HashUtilsTest {
	
	private static final Logger logger = LoggerFactory.getLogger(HashUtilsTest.class);

	@Test
	public void test_encode(){
		String res = HashUtils.hash("some very long article with json:{input:[2,3,5,6]}");
		logger.info(res);
		
	}

	@Test
	public void test() throws Exception{
		String res = EncodingUtils.createHash("0.0.1", "{\"input\":[1,2,3]}");
		logger.info(res);
	}
}

