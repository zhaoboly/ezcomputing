package org.ezcomputing.server.util;

import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemUtilsTest {

	private static final Logger logger = LoggerFactory.getLogger(Base36UtilsTest.class);

	@Test
	public void test() throws Exception {
		Map<String, String> map = SystemInfoUtils.collectSystemInfo();
		String json = JsonUtils.writeValueAsString(map);
		logger.info(json);
	}
}
