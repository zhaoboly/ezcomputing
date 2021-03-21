package org.ezcomputing.server.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
/**
 * @author Bo Zhao
 *
 */

@Component
@WebFilter("/*")
public class StatsFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(StatsFilter.class);


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// empty
	}
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		long time = System.currentTimeMillis();
		try {
			chain.doFilter(req, resp);
		} finally {
			String uri =((HttpServletRequest) req).getRequestURI();

			// skip
			if ("/health".equals(uri)) {
				return;
			}
			
			if (uri.startsWith("/status")) {
				return;
			}
			if (uri.startsWith("/heartbeat")) {
				return;
			}
			if (uri.startsWith("/hub/job/pull/")) {
				return;
			}
			if (uri.startsWith("/hub/task/pull/")) {
				return;
			}
			
			
			time = System.currentTimeMillis() - time;
			logger.info("{} duration: {} ms ", uri, time);
		}
	}

	@Override
	public void destroy() {
		// empty
	}

	
}
