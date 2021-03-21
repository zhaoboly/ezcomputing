package org.ezcomputing.server.web.controller;


import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;


public class HomeControllerTest  {

//public class HomeControllerTest extends AbstractControllerTest {

	/*-

	//@Test
	public void testHealth() throws Exception {

		String expected = "healthy";

		mockMvc.perform(get("/health").contentType(MediaType.TEXT_HTML)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(expected));
	}
	
	//@Test
	public void testStatus() throws Exception {

		String expected = "Dandelion running on test";

		this.mockMvc.perform(get("/status").contentType(MediaType.TEXT_HTML)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString(expected)));
	}
	
	//@Test
	public void testStatus1() throws Exception {

		String expected = "Dandelion running on test";

		this.mockMvc.perform(get("http://dandelionapp.com/status").contentType(MediaType.TEXT_HTML)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString(expected)));
	}
	    */
}

