package org.ezcomputing.server.web.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
/**
 * @author Bo Zhao
 *
 */
@Configuration
public class RequestLoggingFilterConfig {
 
    @Bean
    public CommonsRequestLoggingFilter logFilter() {
    	CustomRequestLoggingFilter filter
          = new CustomRequestLoggingFilter();
    	/*-
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);
        filter.setAfterMessagePrefix("REQUEST : ");
        */
        return filter;
    }
}