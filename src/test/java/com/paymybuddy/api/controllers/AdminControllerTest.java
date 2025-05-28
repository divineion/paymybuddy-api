package com.paymybuddy.api.controllers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.paymybuddy.api.config.TestSecurityConfig;
import com.paymybuddy.api.constants.ApiMessages;
import com.paymybuddy.api.services.user.UserNotSoftDeletedException;
import com.paymybuddy.api.services.user.UserService;

@Import(TestSecurityConfig.class)
@WebMvcTest(AdminController.class)
public class AdminControllerTest {
	
	@MockitoBean
	private UserService service;
	
	@Autowired MockMvc mockMvc;
	
	@Test
	public void testDeleteUser_ShouldReturnNoContent() throws Exception {
		int id = 1;
		
		doNothing().when(service).deleteUser(id);
		
		mockMvc.perform(delete("/api/admin/user/{id}", id))
			.andExpect(status().isNoContent());
	}
	
	@Test
	public void testDeleteUserNotSoftDeleted_ShouldReturnBadRequest() throws Exception {
		int id = 1;
		
		doThrow(new UserNotSoftDeletedException(ApiMessages.USER_NOT_SOFT_DELETED)).when(service).deleteUser(id);
		
		mockMvc.perform(delete("/api/admin/user/1"))
			.andExpect(status().isBadRequest());
	}
}
