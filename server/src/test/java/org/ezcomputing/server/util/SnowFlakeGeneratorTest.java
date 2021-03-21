package org.ezcomputing.server.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SnowFlakeGeneratorTest {

	private static final Logger logger = LoggerFactory.getLogger(SnowFlakeGeneratorTest.class);

	@Test
	public void test(){
		SnowFlakeGenerator gen = new SnowFlakeGenerator();
		
		logger.debug("str:"+ gen.nextUUID());
		logger.debug("current time:"+ System.currentTimeMillis());
		
	}
}
