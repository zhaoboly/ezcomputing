package org.ezcomputing.server.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Bo Zhao
 *
 */
@Component
@WebFilter("/*")
public class ThrottlingFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(ThrottlingFilter.class);

	

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		
		//logger.info("start throttling filter");
		try {
			chain.doFilter(req, resp);
		} finally {
			
		}
	}

	@Override
	public void destroy() {
		
	}

	
}
