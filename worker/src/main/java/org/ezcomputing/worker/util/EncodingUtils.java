package org.ezcomputing.worker.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
/**
 * @author Bo Zhao
 *
 */
public class EncodingUtils {
	
	// version , example: 0.0.1
	// request example: {"input": [1,2,3,4]}
	// 1, combine version and request:  -version-request
	// 2. create url encoding, served as url get parameter
	// 3. it also served as range key in DynamoDB
	public static String createHash(String version, String request) throws UnsupportedEncodingException, NoSuchAlgorithmException{
	//	public static String createVersionAndRequestEncoding(String version, String request) throws UnsupportedEncodingException{
					
		String versionAndRequest = "-"+version+"-"+request;
		
		//String encodingString = URLEncoder.encode(versionAndRequest, java.nio.charset.StandardCharsets.UTF_8.toString());
		
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		byte[] digest = messageDigest.digest(versionAndRequest.getBytes("UTF-8"));
		String dataEncoded = Base64.getEncoder().encodeToString(digest);
		return String.valueOf(dataEncoded);
	}

	
	public static String decode(String str) throws UnsupportedEncodingException{
		String result = URLDecoder.decode(str, java.nio.charset.StandardCharsets.UTF_8.toString());
		
		return result;

	}
	
	public static String encode(String str) throws UnsupportedEncodingException{
		String encoded = URLEncoder.encode(str, java.nio.charset.StandardCharsets.UTF_8.toString());
		return encoded;
	}
}
