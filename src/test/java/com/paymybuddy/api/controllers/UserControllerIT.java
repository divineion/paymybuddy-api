package com.paymybuddy.api.controllers;

import static org.hamcrest.CoreMatchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.api.config.JWTService;
import com.paymybuddy.api.config.JwtTestUtil;
import com.paymybuddy.api.repositories.UserRepository;
import com.paymybuddy.api.services.dto.EmailRequestDto;
import com.paymybuddy.api.services.dto.TransferPageDto;
import com.paymybuddy.api.services.dto.UserAccountDto;

import jakarta.servlet.http.Cookie;


@SpringBootTest // charger le contexte complet
@AutoConfigureMockMvc // créer le bean injectable mockMvc
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
	public void testGetTransferPage_shouldReturnNotFound() throws Exception {
		String username = "georgia@email.com";
		String jwt = jwtTestUtil.generateToken(username);
		
		int id = 999;

	    // WHEN
	    mockMvc.perform(get("/api/user/{id}/transfers", id)
    		.cookie(new Cookie("JWT", jwt)))
	    .andExpect(status().isForbidden())
	    .andExpect(jsonPath("$.status").exists());
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
	public void testRegister_shouldReturnConflict() throws Exception {
		UserAccountDto userAccountDto = new UserAccountDto("Georgina", "georgia@email.com", "Georgina@t3st");
		
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
	
	@Disabled
	@Test
	public void testAddRelation_ShouldReturnConflict() throws Exception {
		String username = "georgia@email.com";
		String jwt = jwtTestUtil.generateToken(username);
		
		int id = 1;
		String email = "tanka@email.com";
		int beneficiaryId = 2;
		
		
		Integer count = jdbcTemplate.queryForObject(
		        "SELECT COUNT(*) FROM user_beneficiary WHERE user_id = ? AND beneficiary_id = ?",
		        Integer.class,
		        id, beneficiaryId
		    );
		
		EmailRequestDto emailDto = new EmailRequestDto(email);
		
		mockMvc.perform(post("/api/user/{id}/add-relation", id)
			.cookie(new Cookie("JWT", jwt))
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(emailDto)))
		.andExpect(status().isConflict());
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
	public void testRemoveRelation_ShouldReturnBadRequest() throws Exception {
		String username = "georgia@email.com";
		String jwt = jwtTestUtil.generateToken(username);
		
		int id = 1;
		int beneficiaryId = 3;
		
		mockMvc.perform(delete("/api/user/{id}/remove-relation/{beneficiaryId}", id, beneficiaryId)
			.cookie(new Cookie("JWT", jwt)))
		.andExpect(status().isBadRequest());
	}

}