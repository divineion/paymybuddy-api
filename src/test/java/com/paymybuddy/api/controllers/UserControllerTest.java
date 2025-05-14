package com.paymybuddy.api.controllers;

import static org.hamcrest.CoreMatchers.is;
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
import com.paymybuddy.api.model.dto.UserDto;
import com.paymybuddy.api.services.finder.UserFinderService;

// https://www.baeldung.com/spring-mockmvc-vs-webmvctest
@Import(SecurityConfig.class)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private UserFinderService finder;

	@Test
	public void shouldReturnUserDto() throws Exception {
		// GIVEN
		UserDto mockUserDto = new UserDto(1, "Sylvia", "sylvia@email.com");

		// WHEN
		when(finder.findUserById(1)).thenReturn(mockUserDto);

		// ASSERT
		// https://www.javadoc.io/doc/org.springframework/spring-test/5.2.7.RELEASE/org/springframework/test/web/servlet/ResultActions.html
		mockMvc.perform(get("/api/user/1"))
		.andExpectAll(
			status().isOk(), 
			jsonPath("$.id", is(1)),
			jsonPath("$.username", is("Sylvia")), 
			jsonPath("$.email", is("sylvia@email.com"))
		);
	}
}
