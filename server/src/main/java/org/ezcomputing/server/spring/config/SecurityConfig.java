package org.ezcomputing.server.spring.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
/**
 * @author Bo Zhao
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
			
			.authorizeRequests()
				.antMatchers("/","/doc/**", "/test/**", "/accessDenied","/invalidSession","/css/**", "/js/**", "/health", "/hub/**", "/heartbeat/*" , "/index.html", "/login.htm", "/cognito-callback.htm", "/target/logfile.log").permitAll()
				.anyRequest().authenticated()
				
				.and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())

				  .and()
				  
			.logout()
			.logoutSuccessUrl("/index.html")
				.permitAll();
	}

	
	@Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new AuthenticationEntryPoint() {

            @Override
            public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException authException) throws IOException, ServletException {
              
            	res.sendRedirect("https://ezcomputing.org");
            }
        };
    }
	
}