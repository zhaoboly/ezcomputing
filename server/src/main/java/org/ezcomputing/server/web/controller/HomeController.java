package org.ezcomputing.server.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author Bo Zhao
 *
 */
@RestController
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	// used by aws
	@RequestMapping("/health")
	public String health() {
		return "healthy";
	}

	@RequestMapping("/health-check")
	public String health_check() {
		return "healthy";
	}

	@RequestMapping("/status")
	public String status() {
		String str = "ezcomputing.com is running ";
		return str;
	}


}
