package org.ezcomputing.worker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.ezcomputing.worker.manager.SchedulerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
/**
 * @author Bo Zhao
 *
 */
@SpringBootApplication
@EnableScheduling
//@ComponentScan(basePackages = { "org.ezcomputing" })
public class Application {

	//public static String SERVER_KEY;

	public static String EMAIL;

	public static String NAME;

	public static String uuid;

	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		logger.info("start Application. ");
		for (String arg : args) {
			

			if (arg.startsWith("email=")) {
				EMAIL = StringUtils.substringAfter(arg, "=");
			}

			if (arg.startsWith("name=")) {
				NAME = StringUtils.substringAfter(arg, "=");
			}
		}

		

		// TODO what if the email is changed for the same server

		if (EMAIL == null) {
			throw new RuntimeException(
					"need email to start.   using this command to start the application: java -jar ezcomputing-worker.jar email=YOUR_EMAIL name=\"WORKER_NAME\"");

		} else {
			logger.info("current email:" + EMAIL);
		}
		if (NAME == null) {
			throw new RuntimeException(
					"need worker name to start.   using this command to start the application: java -jar ezcomputing-worker.jar email=YOUR_EMAIL name=\"WORKER_NAME\"");

		} else {
			logger.info("current name:" + NAME);
		}

		initId();
		SchedulerManager.accessKey = uuid;
		SpringApplication.run(Application.class, args);
	}

	public static void initId() {

		try {
			String fileName = "ezcomputing.secure";
			File file = new File(fileName);
			if (file.exists()) {
				
				BufferedReader reader = new BufferedReader(new FileReader(file));

				uuid = reader.readLine();
				if (uuid.isEmpty() || uuid.length() != 32) {
					file.delete();
					reader.close();
					throw new RuntimeException("ezcomputing.secure file corrupted, please restart the application.");
				}
				reader.close();
			} else {
				FileWriter fileWriter = new FileWriter(file);
				PrintWriter printWriter = new PrintWriter(fileWriter);
				uuid = UUID.randomUUID().toString().replace("-", "");
				printWriter.print(uuid);
				printWriter.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	

}
