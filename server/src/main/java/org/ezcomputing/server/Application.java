package org.ezcomputing.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
/**
 * @author Bo Zhao
 *
 */

@SpringBootApplication
@EnableCaching
@EnableAsync
@EnableScheduling
@ComponentScan(basePackages = { "org.ezcomputing.server" })
public class Application {

	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	
}
