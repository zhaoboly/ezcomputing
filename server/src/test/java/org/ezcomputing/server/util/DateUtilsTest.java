package org.ezcomputing.server.util;

import java.util.Date;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtilsTest {
	
	private static final Logger logger = LoggerFactory.getLogger(DateUtilsTest.class);

	
	@Test
	public void test() {
		Long now = new Date().getTime();
		logger.info("now:"+ now);
		
		Long end = new Date().getTime();
	}
	
}
