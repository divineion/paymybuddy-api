package com.paymybuddy.api.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.api.config.TestSecurityConfig;
import com.paymybuddy.api.exceptions.EmailAlreadyExistsException;
import com.paymybuddy.api.exceptions.RoleNotFoundException;
import com.paymybuddy.api.exceptions.UserNotFoundException;
import com.paymybuddy.api.model.Role;
import com.paymybuddy.api.services.dto.BeneficiaryDto;
import com.paymybuddy.api.services.dto.EmailRequestDto;
import com.paymybuddy.api.services.dto.TransferPageDto;
import com.paymybuddy.api.services.dto.UserAccountDto;
import com.paymybuddy.api.services.dto.UserDto;
import com.paymybuddy.api.services.user.UserService;
//https://www.baeldung.com/spring-mockmvc-vs-webmvctest
@Import(TestSecurityConfig.class)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	
	@MockitoBean
	private UserService service;
	
	@Test
	public void testGetUserById_ShouldReturnUserDto() throws Exception {
		// GIVEN
		int userId = 1;
		UserDto mockUserDto = new UserDto(userId, "Sylvia", "sylvia@email.com", BigDecimal.ZERO);

		// WHEN
		when(service.findUserById(userId)).thenReturn(mockUserDto);

		// THEN
		// https://www.javadoc.io/doc/org.springframework/spring-test/5.2.7.RELEASE/org/springframework/test/web/servlet/ResultActions.html
		mockMvc.perform(get("/api/user/{userId}", userId))
		.andExpectAll(
			status().isOk(), 
			jsonPath("$.id", is(userId)),
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
	
	@Test
	public void testRegister_shouldReturnCreated() throws Exception {
		//given
		UserAccountDto newAccountdto = new UserAccountDto("username", "email@email.com", "any_password");
		UserDto userDto = mock(UserDto.class);
		when(service.registerNewUserAccount(newAccountdto)).thenReturn(userDto);
		
		mockMvc.perform(post("/api/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newAccountdto)))
		.andExpect(status().isCreated());
	}
	
	@Test
	public void testRegister_shouldReturnConflict() throws Exception {
		//given
		UserAccountDto newAccountdto = new UserAccountDto("username", "email@email.com", "any_password");
		when(service.registerNewUserAccount(newAccountdto)).thenThrow(EmailAlreadyExistsException.class);
		
		mockMvc.perform(post("/api/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newAccountdto)))
		.andExpect(status().isConflict());
	}
	
	/**
	 * Verifies the controller's behavior when the service fails to find a {@link Role} 
	 * that is expected to always exist.
	 * <p>
	 * The service is mocked to throw a {@link RoleNotFoundException}, and the test 
	 * asserts that the controller responds with a 500 Internal Server Error.
	 * </p>
	 */
	@Test
	public void testRegister_shouldReturnInternalServerError() throws Exception {
		//given
		UserAccountDto newAccountdto = new UserAccountDto("username", "email@email.com", "any_password");
		when(service.registerNewUserAccount(newAccountdto)).thenThrow(RoleNotFoundException.class);
		
		mockMvc.perform(post("/api/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newAccountdto)))
		.andExpect(status().isInternalServerError());
	}
	
	@Test
	public void testAddRelation_ShouldReturnOk() throws Exception {
		//given
		int id = 1;
		String email = "email@email.com";
		EmailRequestDto emailDto = new EmailRequestDto(email);
		BeneficiaryDto mockBeneficiaryDto = mock(BeneficiaryDto.class);
		//when
		when(service.addBeneficiary(id, emailDto)).thenReturn(mockBeneficiaryDto);
		
		//then
		mockMvc.perform(put("/api/user/{id}/add-relation", id)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(emailDto)))
		.andExpect(status().isOk());
	}
	
	@Test
	public void testRemoveRelation_ShouldReturnOk() throws Exception {
		//given
		int id = 1;
		int beneficiaryId = 2;
		
		//then
		mockMvc.perform(delete("/api/user/{id}/remove-relation/{beneficiaryId}", id, beneficiaryId))
		.andExpect(status().isNoContent());
	}
}
