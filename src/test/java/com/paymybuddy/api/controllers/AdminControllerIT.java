package com.paymybuddy.api.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.paymybuddy.api.config.JwtTestUtil;
import com.paymybuddy.api.services.dto.ChangePasswordDto;
import com.paymybuddy.api.services.user.UserService;

import jakarta.servlet.http.Cookie;

@SpringBootTest 
@AutoConfigureMockMvc 
@ActiveProfiles("test")
public class AdminControllerIT {
	
	@MockitoBean
	private UserService service;
	
	@Autowired MockMvc mockMvc;

	
	@Autowired
	private JwtTestUtil jwtTestUtil;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Test
	public void testDeleteUser_ShouldReturnNoContent() throws Exception {
		String username = "admin@email.com";
		String jwt = jwtTestUtil.generateToken(username);
		
		int id = 1;
		
		mockMvc.perform(delete("/api/admin/user/{id}", id)
				.cookie(new Cookie("JWT", jwt)))
			.andExpect(status().isNoContent());
	}
	
	/**
	 * Access to an admin-only route with a ROLE_USER token. 
	 * @throws Exception
	 */
	@Test
	public void testDeleteUserNotSoftDeleted_ShouldReturnForbidden() throws Exception {
		String username = "tanka@email.com";
		String jwt = jwtTestUtil.generateToken(username);
		
		int id = 1;
				
		mockMvc.perform(delete("/api/admin/user/{id}", id)
				.cookie(new Cookie("JWT", jwt)))
			.andExpect(status().isForbidden());
	}
	
	 @Test
	    public void testUpdatePassword_AdminShouldReturnOk() throws Exception {
	        int userIdToChange = 1; // l'utilisateur dont on change le mdp
	        String adminEmail = "admin@email.com";

	        String jwt = jwtTestUtil.generateToken(adminEmail);

	        ChangePasswordDto changePasswordDto = new ChangePasswordDto(
	            "User3@t3st", 
	            "NewPass@1234" 
	        );

	        mockMvc.perform(put("/api/admin/user/{id}/change-password", userIdToChange)
	                .cookie(new Cookie("JWT", jwt))
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(objectMapper.writeValueAsString(changePasswordDto)))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.message").value("The user password has been updated"));
	    }
}
