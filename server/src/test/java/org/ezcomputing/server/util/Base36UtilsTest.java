package org.ezcomputing.server.util;


import static org.junit.Assert.assertEquals;

import java.text.MessageFormat;
import java.util.Date;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Base36UtilsTest {
	
	private static final Logger logger = LoggerFactory.getLogger(Base36UtilsTest.class);

	@Test
	public void test_encode(){
		long number = 466780691334565888L;
		
		String str = Base36Utils.encode(number);
		long result = Base36Utils.decode(str);
		
		assertEquals(number, result);
		logger.debug("str:"+ str);
		
		logger.debug("current time:"+ Base36Utils.encode(System.currentTimeMillis()));
		logger.debug("current time:"+ Base58Utils.encode(System.currentTimeMillis()));
	}

	@Test
	public void test_messageFormat() {
		int planet = 70000;
		 String event = "a disturbance in the Force";
		String result = MessageFormat.format(
			     "At {1,time} on {1,date}, there was {2} on planet {0,number,}.",
			     planet, new Date(), event);
		
		logger.info(result);
	}
	
	
}
