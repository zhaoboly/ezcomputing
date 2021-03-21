package org.ezcomputing.server.util;
/**
 * @author Bo Zhao
 *
 */
public class TemplateUtils {
	
	private static String newLine = System.getProperty("line.separator");

	public static String getJobSourceTemplate() {

		return String.join(newLine, 
				"/*",
				"  This is a sample code." ,
				"  main function will be called using json request, ",
				"  main function return will be put into job response field ",
				"*/", 
				"function main(input){", 
				"    input.name = input.name + ' world';",
				"    return input;",
				"}");

	}
	
	public static String getJobRequestTemplate() {

		return String.join(newLine, 
				"{", 
				"    \"name\": \"hello \",", 
				"}");

	}
	
	public static String getJobResponseTemplate() {

		return String.join(newLine, 
				"{", 
				"    \"name\": \"hello world\",", 
				"}");

	}
}
