package com.paymybuddy.api.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.api.config.ConfigConstants;
import com.paymybuddy.api.config.CustomUserDetails;
import com.paymybuddy.api.config.JWTService;
import com.paymybuddy.api.exceptions.EmailNotFoundException;
import com.paymybuddy.api.services.dto.UserDto;
import com.paymybuddy.api.services.dto.UserLoginDto;
import com.paymybuddy.api.services.user.UserService;

import jakarta.validation.Valid;

@RestController
public class LoginController {
	private final JWTService jwtService;
	private final AuthenticationManager authenticationManager;
	private UserService userService;
	
	public LoginController(JWTService jwtService, AuthenticationManager authenticationManager, UserService userService) {
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
		this.userService = userService;
	}
	
	/**
	 * Authenticates the user based on provided credentials and generates a JWT token.
	 * <p>
	 * Steps performed:
	 * <ol>
	 *   <li>Creates an authentication token from the username and password.</li>
	 *   <li>Authenticates the token using AuthenticationManager.</li>
	 *   <li>Retrieves the authenticated user details from the authentication principal.</li>
	 *   <li>Generates a JWT token for the authenticated user.</li>
	 *   <li>Creates an HTTP-only cookie containing the JWT token with appropriate settings.</li>
	 *   <li>Sets the cookie in the response header.</li>
	 * </ol>
	 *
	 * @param loginDto a {@link UserLoginDto} containing username and password, validated before processing
	 * @return {@link ResponseEntity} with JWT token in body and HTTP-only cookie header
	 * @throws AuthenticationException if authentication fails, triggering a 401 Unauthorized
	 */
	@PostMapping("/api/login_check")
	public ResponseEntity<String> getToken(@Valid @RequestBody UserLoginDto loginDto) {
		Authentication authToken = new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password());
		
		Authentication authenticatedUser = authenticationManager.authenticate(authToken);
		
	    CustomUserDetails userDetails = (CustomUserDetails) authenticatedUser.getPrincipal();

		String token = jwtService.generateToken(userDetails);
		
		ResponseCookie cookie = ResponseCookie.from("JWT", token)
				.httpOnly(true)
				.maxAge(ConfigConstants.DEV_ENV_COOKIE_DURATION_IN_SECONDS)
				.secure(false)
				.path("/")
				.build();
		
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.header(HttpHeaders.SET_COOKIE, cookie.toString()) 
				.build(); 
	}

	/**
	 * Handles user authentication verification.
	 *
	 * @param authentication the Spring Security authentication object
	 * @return a ResponseEntity containing the authenticated user's information
	 * @throws EmailNotFoundException if the email associated with the authentication cannot be found
	 */
	@GetMapping("/api/auth_check")
	public ResponseEntity<UserDto> checkAuthentication(Authentication authentication) throws EmailNotFoundException {
	    boolean authenticated = authentication != null 
	    		&& authentication.isAuthenticated()
	    		&& !(authentication instanceof AnonymousAuthenticationToken);
	    	    
        String email = authentication.getName();
        
	    UserDto userDto = userService.findUserDtoByEmail(email);
	    
	    return ResponseEntity.status(HttpStatus.OK).body(userDto);
	}
	
	@PostMapping("/api/logout")
	public ResponseEntity<Void> logout() {
		ResponseCookie deleteCookie = ResponseCookie.from("JWT", "")
				.httpOnly(true)
				.secure(false)
				.path("/")
				.maxAge(0)
				.build();
		
		return ResponseEntity.status(HttpStatus.OK)
				.header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
				.build();
	}
}
