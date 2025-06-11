package com.paymybuddy.api.controllers;

import static org.hamcrest.CoreMatchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.api.config.JWTService;
import com.paymybuddy.api.config.JwtTestUtil;
import com.paymybuddy.api.repositories.UserRepository;
import com.paymybuddy.api.services.dto.EmailRequestDto;
import com.paymybuddy.api.services.dto.TransferPageDto;
import com.paymybuddy.api.services.dto.UpdateUserAccountDto;
import com.paymybuddy.api.services.dto.UserAccountDto;

import jakarta.servlet.http.Cookie;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerIT {

	@ Autowired
	MockMvc mockMvc;
	
	@Autowired
	JWTService jwtservice;
	
	@Autowired
	private JwtTestUtil jwtTestUtil;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Test
	public void testGetUserById_ShouldReturnUserDto() throws Exception {
		String username = "georgia@email.com";
		String jwt = jwtTestUtil.generateToken(username);
		
		int id = 1;

		mockMvc.perform(get("/api/user/{userId}", id)
				.cookie(new Cookie("JWT", jwt)))
		.andExpectAll(
			status().isOk(),
			jsonPath("$.id", is(1)),
			jsonPath("$.username", is("Georgia")), 
			jsonPath("$.email", is("georgia@email.com"))
		);
	}
	
	
	@Test
	public void testGetUserById_ShouldReturnForbidden() throws Exception {
		String username = "georgia@email.com";
		String jwt = jwtTestUtil.generateToken(username);
		
		int id = 999;

		mockMvc.perform(get("/api/user/{userId}", id)
			.cookie(new Cookie("JWT", jwt)))
			.andExpectAll(
				status().isForbidden()
		);
	}
	
	/**
	 * {@link TransferPageDto}
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetTransferPage_shouldReturnTransferPageDto() throws Exception {
		String username = "georgia@email.com";
		String jwt = jwtTestUtil.generateToken(username);
		
		int id = 1;
		
	    mockMvc.perform(get("/api/user/{id}/transfers", id)
    		.cookie(new Cookie("JWT", jwt)))
	    .andExpectAll(
    		status().isOk(),
    		jsonPath("$.id", is(1)),
    		jsonPath("$.beneficiaries").isArray(),
    		jsonPath("$.balance").isNumber(),
    		jsonPath("$.sentTransfers").isArray(),
    		jsonPath("$.receivedTransfers").isArray()    		
		);
	}
	
	@Test
	public void testGetTransferPage_WithoutValidToken_shouldReturnForbidden() throws Exception {
	    String invalidUsername = "doesnotexists@email.com";
	    
	   Assertions.assertThrows(UsernameNotFoundException.class, () -> {
		   String jwt = jwtTestUtil.generateToken(invalidUsername);
	   
	        int id = 999;

	    mockMvc.perform(get("/api/user/{id}/transfers", id)
	            .cookie(new Cookie("JWT", jwt)))
	        .andExpect(status().isForbidden()) 
	        .andExpect(jsonPath("$.status").exists())
	        .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("Forbidden access")));
	    });
	}
	
	@Test
	public void testRegister_shouldReturnCreated() throws Exception {
		//given
		UserAccountDto userAccountDto = new UserAccountDto("Lilia", "lilia@email.com", "Lilia@t3st");
		
		mockMvc.perform(post("/api/register")
				.contentType(org.springframework.http.MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userAccountDto)))
		.andExpect(status().isCreated());
	}
	
	@Test
	public void testRegister_WithExistingUser_ShouldReturnConflict() throws Exception {
		UserAccountDto userAccountDto = new UserAccountDto("Tanka", "tanka@email.com", "Tanka@t3st");
		
		mockMvc.perform(post("/api/register")
				.contentType(org.springframework.http.MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userAccountDto)))
		.andExpect(status().isConflict());
	}	
	
	@Test
	public void testAddRelation_ShouldReturnOk() throws Exception {
		String username = "georgia@email.com";
		String jwt = jwtTestUtil.generateToken(username);
		
		int id = 1;
		String email = "jeena@email.com";
		
		EmailRequestDto emailDto = new EmailRequestDto(email);
		
		mockMvc.perform(post("/api/user/{id}/add-relation", id)
				.cookie(new Cookie("JWT", jwt))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(emailDto)))
		.andExpect(status().isOk());
	}
	
	@Test
	public void testAddRelation_WithExistingRelation_ShouldReturnBadRequest() throws Exception {
		String username = "tanka@email.com";
		String jwt = jwtTestUtil.generateToken(username);
		
		int id = 2;
		String email = "tanka@email.com";
		int beneficiaryId = 2;
		
		EmailRequestDto emailDto = new EmailRequestDto(email);
		
		mockMvc.perform(post("/api/user/{id}/add-relation", id)
			.cookie(new Cookie("JWT", jwt))
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(emailDto)))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testRemoveRelation_ShouldReturnNoContent() throws Exception {
		String username = "georgia@email.com";
		String jwt = jwtTestUtil.generateToken(username);
		
		int id = 1;
		int beneficiaryId = 2;
		
		//then
		mockMvc.perform(delete("/api/user/{id}/remove-relation/{beneficiaryId}", id, beneficiaryId)
			.cookie(new Cookie("JWT", jwt)))
		.andExpect(status().isNoContent());
	}
	
	@Test
	public void testRemoveRelation_WithRelationNotFound_ShouldReturnBadRequest() throws Exception {
		String username = "georgia@email.com";
		String jwt = jwtTestUtil.generateToken(username);
		
		int id = 1;
		int beneficiaryId = 3;
		
		mockMvc.perform(delete("/api/user/{id}/remove-relation/{beneficiaryId}", id, beneficiaryId)
			.cookie(new Cookie("JWT", jwt)))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testUpdateUserInfo_ShouldReturnOk() throws Exception {
	    String username = "georgia@email.com";
	    String jwt = jwtTestUtil.generateToken(username);

	    int userId = 1;

	    UpdateUserAccountDto updateDto = new UpdateUserAccountDto(
	        "georgia.updated@email.com",
	        "User1@test",          
	        "User1Updated@test"
	    );

	    mockMvc.perform(
	            patch("/api/user/{id}/change-user-info", userId)
	                .cookie(new Cookie("JWT", jwt))
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(objectMapper.writeValueAsString(updateDto))
	        )
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.message", is("The user account has been updated")));
	}
	
	@Test
	public void testUpdateUserInfo_WithInvalidCurrentPassword_ShouldReturnBadRequest() throws Exception {
	    String username = "mania@email.com";
	    String jwt = jwtTestUtil.generateToken(username);

	    int userId = 4;

	    UpdateUserAccountDto updateDto = new UpdateUserAccountDto(
	        "mania.updated@email.com",
	        "Mania3@test",          
	        "User3Updated@test"
	    );

	    mockMvc.perform(
	            patch("/api/user/{id}/change-user-info", userId)
	                .cookie(new Cookie("JWT", jwt))
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(objectMapper.writeValueAsString(updateDto))
	        )
	        .andExpect(status().isBadRequest());
	}
	
	@Test
	public void testDeleteAccount_ShouldReturnNoContent() throws Exception {
	    int userId = 5;

	    String jwt = jwtTestUtil.generateToken("jeena@email.com");

	    mockMvc.perform(put("/api/user/{id}/delete-account", userId)
	            .cookie(new Cookie("JWT", jwt)))
	        .andExpect(status().isNoContent());
	}

	@Test
	public void testDeleteAccount_UserNotFound_ShouldReturnNotFound() throws Exception {
	    int nonExistingUserId = 9999;

	    String jwt = jwtTestUtil.generateToken("admin@email.com");

	    mockMvc.perform(put("/api/user/{id}/delete-account", nonExistingUserId)
	            .cookie(new Cookie("JWT", jwt)))
	        .andExpect(status().isNotFound());
	}
	
}