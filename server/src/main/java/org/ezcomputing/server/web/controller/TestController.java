package org.ezcomputing.server.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ezcomputing.server.dao.dto.User;
import org.ezcomputing.server.service.UserService;
import org.ezcomputing.server.util.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
/**
 * @author Bo Zhao
 *
 */
@RestController
@Profile("local")
public class TestController {

	private static final Logger logger = LoggerFactory.getLogger(TestController.class);
	
	@Autowired
	private UserService userService;
	
		
	@GetMapping(value = { "test/login/{email}" })
	public ModelAndView testLogin(@PathVariable String email) {
		
		User user = userService.loadUser(email);
		final List<GrantedAuthority> grantedAuths = new ArrayList<>();
		grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
		
		if(user.getAuthorityList()!=null) {
			for(String auth: user.getAuthorityList().split(";")) {
				grantedAuths.add(new SimpleGrantedAuthority(auth));
			}
		}
		
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, "",grantedAuths);
		
		SecurityContext sc = SecurityContextHolder.getContext();
		sc.setAuthentication(auth);
		
		
		return new ModelAndView("redirect:/web/app/list");
	}

	private String readJson(String fileName) throws IOException {
		//ClassLoader classLoader = getClass().getClassLoader();
		//InputStream inputStream = classLoader.getResourceAsStream(filename);
		String data = ResourceUtils.readResource(fileName);
		
		return "Java.asJSONCompatible("+ data+")";
	}
}
