package org.ezcomputing.server.util;
/**
 * @author Bo Zhao
 *
 */
public class HashUtils {

	public static String hash(String str) {
		
		    char[] code = new char[32];
		    for(int i = 0; i < str.length(); i++) {
		        code[i % code.length] = (char)((int)code[i % code.length] ^ (int)str.charAt(i));
		    }
		    return new String(code);
		
	}
}
