package com.paymybuddy.api.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import com.paymybuddy.api.exceptions.RoleNotFoundException;
import com.paymybuddy.api.services.dto.UserAccountDto;
import com.paymybuddy.api.services.user.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerRoleNotFoundExceptionIT {
	@Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;
    /**
	 * Verifies the controller's behavior when the service fails to find a {@link Role} 
	 * that is expected to always exist.
	 * <p>
	 * The service is mocked to throw a {@link RoleNotFoundException}, and the test 
	 * asserts that the controller responds with a 500 Internal Server Error.
	 * </p>
	 * @throws Exception 
	 */
	@Test
	public void testRegister_shouldReturnInternalServerError() throws Exception, RuntimeException {
		//given
		UserAccountDto newAccountdto = new UserAccountDto("NewUser", "newuser@email.com", "Newuser@t3st");
		
		when(userService.registerNewUserAccount(newAccountdto))
			.thenThrow(new RoleNotFoundException("Role not found"));
		
		mockMvc.perform(post("/api/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newAccountdto)))
		.andExpect(status().isInternalServerError());
	}
}
