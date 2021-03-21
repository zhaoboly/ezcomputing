package org.ezcomputing.server.spring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;
/**
 * @author Bo Zhao
 *
 */
@Component
public class ContextClosedHandler implements ApplicationListener<ContextClosedEvent>{
	
	private static final Logger logger = LoggerFactory.getLogger(ContextClosedHandler.class);
	


	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		logger.info("onApplicationEvent");
	}
}
