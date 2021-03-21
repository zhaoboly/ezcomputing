package org.ezcomputing.server.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * @author Bo Zhao
 *
 */
@Configuration
@Controller
public class AdminController {
	
	
	
	 @Autowired
	 private   Environment environment;
    
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

   
    JsonParser jsonParser = new BasicJsonParser();


    @GetMapping("/admin")
    public String admin(Model model, @RequestParam(name="password") String password) {
		
    	
       
        String profiles = "";
        for (final String profileName : environment.getActiveProfiles()) {
            profiles = profiles + profileName;
            
        } 
        model.addAttribute("profile", profiles);

        return "admin";
    }
    

   
   
    
}