//package com.paymybuddy.api.controllers;
//
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.math.BigDecimal;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.paymybuddy.api.config.TestSecurityConfig;
//import com.paymybuddy.api.services.dto.TransferDto;
//import com.paymybuddy.api.services.dto.TransferRequestDto;
//import com.paymybuddy.api.services.transfer.TransferService;
//
//@Import(TestSecurityConfig.class)
//@WebMvcTest(TransferController.class)
//public class TransferControllerTest {
//	@Autowired
//	MockMvc mockMvc;
//	
//	@Autowired
//	ObjectMapper objectMapper;
//	
//	@MockitoBean
//	TransferService mockService;
//	
//	@Test
//	public void testCreateTransfer_ShouldReturnOk() throws Exception {
//		int senderId = 1;
//		int receiverId = 2;
//		TransferRequestDto dto = new TransferRequestDto(senderId, receiverId, "test transfer", new BigDecimal("10"));
//		TransferDto responseDto = mock(TransferDto.class);
//		
//		when(mockService.createTransfer(dto)).thenReturn(responseDto);
//		
//		mockMvc.perform(post("/api/transfer")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(objectMapper.writeValueAsString(responseDto)))
//		.andExpect(status().isCreated());
//	}
//}


