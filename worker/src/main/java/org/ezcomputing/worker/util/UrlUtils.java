package org.ezcomputing.worker.util;

import javax.servlet.http.HttpServletRequest;
/**
 * @author Bo Zhao
 *
 */
public class UrlUtils {

	// example: http://hostname.com/mywebapp/servlet/MyServlet/a/b;c=123?d=789
	public static String getFullUrl(HttpServletRequest req) {
	    String reqUrl = req.getRequestURL().toString();
	    String queryString = req.getQueryString();   // d=789
	    if (queryString != null) {
	        reqUrl += "?"+queryString;
	    }
	    return reqUrl;
	}
}
