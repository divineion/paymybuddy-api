package com.paymybuddy.api.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.servlet.http.Cookie;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.api.config.JWTService;
import com.paymybuddy.api.config.JwtTestUtil;
import com.paymybuddy.api.services.dto.TransferRequestDto;

@SpringBootTest
@AutoConfigureMockMvc 
@ActiveProfiles("test")
public class TransferControllerIT {
	@ Autowired
	MockMvc mockMvc;
	
	@Autowired
	JWTService jwtservice;
	
	@Autowired
	private JwtTestUtil jwtTestUtil;
	
	@Autowired
	ObjectMapper objectMapper;
	
	
	@Test
	public void testCreateTransfer_ShouldReturnOk() throws Exception {
		String sender = "georgia@email.com";
        String jwt = jwtTestUtil.generateToken(sender);

		int senderId = 1;
		int receiverId = 2;
		 BigDecimal amount = new BigDecimal("10.00");
		
		TransferRequestDto dto = new TransferRequestDto(senderId, receiverId, "test transfer", amount);
		
		 mockMvc.perform(post("/api/transfer")
	                .cookie(new Cookie("JWT", jwt))
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(objectMapper.writeValueAsString(dto)))
	            .andExpect(status().isCreated());
	}

	@Test
	public void testCreateTransfer_InsufficientAmount_ShouldReturnBadRequest() throws Exception {
		String sender = "georgia@email.com";
		String jwt = jwtTestUtil.generateToken(sender);
		
		int senderId = 1;
		int receiverId = 2;
		BigDecimal amount = new BigDecimal("0.50");
		
		TransferRequestDto dto = new TransferRequestDto(senderId, receiverId, "test transfer", amount);
		
		mockMvc.perform(post("/api/transfer")
				.cookie(new Cookie("JWT", jwt))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void testCreateTransfer_InsufficientBalance_ShouldReturnBadRequest() throws Exception {
		String sender = "georgia@email.com";
		String jwt = jwtTestUtil.generateToken(sender);
		
		int senderId = 1;
		int receiverId = 2;
		BigDecimal amount = new BigDecimal("2500.00");
		
		TransferRequestDto dto = new TransferRequestDto(senderId, receiverId, "test transfer", amount);
		
		mockMvc.perform(post("/api/transfer")
				.cookie(new Cookie("JWT", jwt))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
		.andExpect(status().isBadRequest());
	}
}


