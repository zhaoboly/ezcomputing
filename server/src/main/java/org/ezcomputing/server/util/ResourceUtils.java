package org.ezcomputing.server.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
/**
 * @author Bo Zhao
 *
 */
public class ResourceUtils {

	public static String readResource(String fileName) throws IOException{
		ClassLoader classLoader = ResourceUtils.class.getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(fileName);
		String res = readFromInputStream(inputStream);

		return res;
	}
	
	private static String readFromInputStream(InputStream inputStream) throws IOException {
		StringBuilder resultStringBuilder = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			while ((line = br.readLine()) != null) {
				resultStringBuilder.append(line).append("\n");
			}
		}
		return resultStringBuilder.toString();
	}
}
