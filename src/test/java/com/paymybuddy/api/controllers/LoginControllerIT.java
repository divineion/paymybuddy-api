package com.paymybuddy.api.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.paymybuddy.api.config.CustomUserDetailsService;
import com.paymybuddy.api.config.JwtTestUtil;

import jakarta.servlet.http.Cookie;

@Import(JwtTestUtil.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LoginControllerIT {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	CustomUserDetailsService service;
	
	@Autowired
	private JwtTestUtil jwtTestUtil;
	
	@Test
	public void testLoginCheck_ShouldReturnJwt() throws Exception {
		String userLoginJson = "{\"username\":\"georgia@email.com\",\"password\":\"User1@test\"}";
		
		MvcResult result = mockMvc.perform(post("/api/login_check")
		        .contentType(MediaType.APPLICATION_JSON)
		        .content(userLoginJson))
		        .andReturn();
		
		String setCookie = result.getResponse().getHeader(HttpHeaders.SET_COOKIE);
		Assertions.assertNotNull(setCookie, "The header Set-Cookie is absent");
		Assertions.assertTrue(setCookie.startsWith("JWT="), "The JWT cookie is absent");	
	}
	
	@Test
	public void testLoginCheckWithInvalidPassword_ShouldReturnUnauthorized() throws Exception {
		String userLoginJson = "{\"username\":\"georgia@email.com\",\"password\":\"AnyPwd1@test\"}";
		
		mockMvc.perform(post("/api/login_check")
				.contentType(MediaType.APPLICATION_JSON)
				.content(userLoginJson))
				.andExpect(status().is(401));
	}
	
	@Test
	public void testLoginCheckWithUnknownEmail_ShouldReturnNotFound() throws Exception {
		String userLoginJson = "{\"username\":\"platipus@email.com\",\"password\":\"AnyPwd1@test\"}";
		
		mockMvc.perform(post("/api/login_check")
				.contentType(MediaType.APPLICATION_JSON)
				.content(userLoginJson))
				.andExpect(status().is(401));
	}
	
	@Test
	public void testAuthenticationCheckWithValidJwt_ShouldReturnUserDto() throws Exception {
		String email = "georgia@email.com";
				
		String jwt = jwtTestUtil.generateToken(email);
		Cookie cookie = new Cookie("JWT", jwt);
						
		mockMvc.perform(get("/api/auth_check")
				.cookie(cookie))
				.andExpect(status().isOk());
	}
	
	@Test
	public void testLogout_ShouldClearJwtCookie() throws Exception {
	    MvcResult result = mockMvc.perform(post("/api/logout"))
	        .andExpect(status().isOk())
	        .andReturn();

	    String setCookie = result.getResponse().getHeader(HttpHeaders.SET_COOKIE);
	    Assertions.assertNotNull(setCookie, "Le header Set-Cookie est absent");
	    Assertions.assertTrue(setCookie.contains("JWT="), "Le cookie JWT est absent dans le header");
	    Assertions.assertTrue(setCookie.contains("Max-Age=0"), "Le cookie JWT n'est pas supprimé (Max-Age=0 attendu)");
	}
}
