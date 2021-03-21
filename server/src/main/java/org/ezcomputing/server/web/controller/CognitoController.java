package org.ezcomputing.server.web.controller;


import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.ezcomputing.server.dao.dto.User;
import org.ezcomputing.server.service.UserService;
import org.ezcomputing.server.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;


/**
 * @author Bo Zhao
 *
 */
@RestController

public class CognitoController  {

	private static final Logger logger = LoggerFactory.getLogger(CognitoController.class);
	
	@Autowired
	private UserService userService;


	// timeout is 30 seconds
	private static RestTemplate restTemplate = new RestTemplateBuilder().setConnectTimeout(30000).setReadTimeout(30000).build();

	@Value("${cognito.clientId}")
	private   String clientId;
	@Value("${cognito.clientSecret}")
	private   String clientSecret;
	@Value("${cognito.login.uri}")
	private   String cognitoLoginUri;
	@Value("${cognito.callback.uri}")
	private String cognitoCallbackUri;
	
	@RequestMapping(method = RequestMethod.GET, value = "/login.htm", produces = "application/json")
	public ModelAndView get( ModelMap model)  {
		
		return new ModelAndView("redirect:"+ cognitoLoginUri, model);

	}
	@RequestMapping(method = RequestMethod.GET, value = "/logout", produces = "application/json")
	public ModelAndView logout( ModelMap model)  {
		logger.info("user logout.");
		SecurityContextHolder.getContext().setAuthentication(null);
		return new ModelAndView("redirect:/index.html");

	}
	
	
	
	@RequestMapping(method = RequestMethod.GET, value = "/cognito-callback.htm")
	public ModelAndView callback(@RequestParam String code)  {
		
		logger.info("authorization code:"+ code);

		String json = obtainToken(code);
		

		try {
			
			JsonNode node = JsonUtils.getObjectMapper().readTree(json);
			String idToken = node.get("id_token").asText();
			
			
			DecodedJWT jwt = JWT.decode(idToken);
/*-
			logger.debug(jwt.getAlgorithm());
			logger.debug(jwt.getType());
			logger.debug(jwt.getKeyId());
			logger.debug(jwt.getSubject());
			logger.debug(jwt.getIssuer());
			logger.debug(jwt.getClaim("email").asString());
*/
			String subject = jwt.getSubject();
			String email = jwt.getClaim("email").asString();
			String familyName = jwt.getClaim("family_name").asString();
			String givenName = jwt.getClaim("given_name").asString();
			
			//logger.info("subject:"+ subject);
			logger.info("email:"+ email);
			//logger.info("family name:"+ familyName);
			//logger.info("given name:"+ givenName);
			
			User user = userService.loadUser(email);
			if(user==null) {
				user = new User();
				user.setSubject(subject);
				user.setEmail(email);
				user.setFamilyName(familyName);
				user.setGivenName(givenName);
				
				userService.saveUser(user);
			}
			
			//manually trigger authentication 
			final List<GrantedAuthority> grantedAuths = new ArrayList<>();
			grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
			
			if(user.getAuthorityList()!=null) {
				for(String auth: user.getAuthorityList().split(";")) {
					grantedAuths.add(new SimpleGrantedAuthority(auth));
				}
			}
			
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, subject,grantedAuths);
			
			SecurityContext sc = SecurityContextHolder.getContext();
			sc.setAuthentication(auth);
			
			
			return new ModelAndView("redirect:"+ "https://ezcomputing.org/web/job/list");


		} catch (JWTDecodeException e) {
			
			return new ModelAndView("redirect:/error.html");

		}catch (IOException e1) {
			
			return new ModelAndView("redirect:/error.html");

		}
		
	}

	
	private String obtainToken(String authorizationCode) {
		String url = "https://ezcomputing.auth.us-east-1.amazoncognito.com/oauth2/token";
		

		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		String auth = clientId + ":" + clientSecret;
		String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(Charset.forName("US-ASCII")));

		headers.add("Authorization", "Basic " + encodedAuth);

		// headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));

		// request body parameters
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("grant_type", "authorization_code");
		map.add("client_id", clientId);
		map.add("code", authorizationCode);
		map.add("redirect_uri", cognitoCallbackUri);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);



		logger.debug(request.toString());
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

		return response.getBody();

	}


}
