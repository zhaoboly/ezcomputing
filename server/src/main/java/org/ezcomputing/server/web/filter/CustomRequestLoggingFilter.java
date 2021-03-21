package org.ezcomputing.server.web.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.filter.CommonsRequestLoggingFilter;
/**
 * @author Bo Zhao
 *
 */
public class CustomRequestLoggingFilter extends CommonsRequestLoggingFilter {

	public CustomRequestLoggingFilter() {
		super.setIncludeQueryString(true);
		super.setIncludePayload(true);
		super.setMaxPayloadLength(10000);
		super.setIncludeHeaders(false);
		super.setAfterMessagePrefix("REQUEST : ");
	}
	@Override
	protected void beforeRequest(HttpServletRequest httpServletRequest, String message) {
		/*String uri = httpServletRequest.getRequestURI();

		// skip
		if ("/status".equals(uri)) {
			return;
		}

		this.logger.debug(message);
		*/
	}

	@Override
	protected void afterRequest(HttpServletRequest httpServletRequest, String message) {
		String uri = httpServletRequest.getRequestURI();

		

		//this.logger.debug(message);
	}
}