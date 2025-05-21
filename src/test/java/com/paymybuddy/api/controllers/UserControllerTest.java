package com.paymybuddy.api.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.paymybuddy.api.config.SecurityConfig;
import com.paymybuddy.api.exceptions.UserNotFoundException;
import com.paymybuddy.api.services.dto.TransferPageDto;
import com.paymybuddy.api.services.dto.UserDto;
import com.paymybuddy.api.services.user.UserService;
//https://www.baeldung.com/spring-mockmvc-vs-webmvctest
@Import(SecurityConfig.class)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private UserService service;
	
	@Test
	public void testGetUserById_ShouldReturnUserDto() throws Exception {
		// GIVEN
		int userId = 1;
		UserDto mockUserDto = new UserDto(userId, "Sylvia", "sylvia@email.com");

		// WHEN
		when(service.findUserById(userId)).thenReturn(mockUserDto);

		// THEN
		// https://www.javadoc.io/doc/org.springframework/spring-test/5.2.7.RELEASE/org/springframework/test/web/servlet/ResultActions.html
		mockMvc.perform(get("/api/user/{userId}", userId))
		.andExpectAll(
			status().isOk(), 
			jsonPath("$.id", is(1)),
			jsonPath("$.username", is("Sylvia")), 
			jsonPath("$.email", is("sylvia@email.com"))
		);
	}
	
	@Test
	public void testGetUserById_ShouldReturnUserNotFound() throws Exception {
		// GIVEN
		int userId = 1;
		
		// WHEN
		when(service.findUserById(userId)).thenThrow(UserNotFoundException.class);
		
		//THEN
		mockMvc.perform(get("/api/user/{userId}", userId))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$.status").exists());
	}
	
	@Test
	public void testGetTransferPage_shouldReturnTransferPageDto() throws Exception {
	    // GIVEN
	    int userId = 1;
	    // création d'un mock vide
	    TransferPageDto mockTransferPageDto = mock(TransferPageDto.class);
	    when(service.findUserTransferPageInfo(userId)).thenReturn(mockTransferPageDto);

	    // WHEN
	    mockMvc.perform(get("/api/user/{id}/transfers", userId))
	    
	    // THEN
	    .andExpect(status().isOk())
	    .andExpect(jsonPath("$").exists());
	}
	
	@Test
	public void testTransferPage_shouldReturnNotFound() throws Exception {
	    // GIVEN
	    int userId = 1;
	    when(service.findUserTransferPageInfo(userId)).thenThrow(UserNotFoundException.class);

	    // WHEN
	    mockMvc.perform(get("/api/user/{id}/transfers", userId))
	    
	    // THEN
	    .andExpect(status().isNotFound())
	    .andExpect(jsonPath("$.status").exists());
	}
}
