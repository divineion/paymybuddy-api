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
	
	// récupérer l'Authentication 
	// JWTService founit le token
	@PostMapping("/api/login_check")
	public ResponseEntity<String> getToken(@RequestBody UserLoginDto loginDto) {
		//créer un auth token via spring security
		Authentication authToken = new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password());
		
		// d'aborsd authentifier le user - retourne 401 Unauthorized si erreur d'authentification
		Authentication authenticatedUser = authenticationManager.authenticate(authToken);
		
		// injecter le CustomUserDetails comme principal
	    CustomUserDetails userDetails = (CustomUserDetails) authenticatedUser.getPrincipal();

	    //passzr le CustomUserDetails pour l'utiliser dans le JWTService
		String token = jwtService.generateToken(userDetails);
		
		//https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/ResponseCookie.html
		ResponseCookie cookie = ResponseCookie.from("JWT", token)
				.httpOnly(true)
				.maxAge(ConfigConstants.DEV_ENV_COOKIE_DURATION_IN_SECONDS)
				.secure(false)
				.path("/") // définit les routes sur lesquelles le cookie sera envoyé depuis le front (par défaut le chemin de la requête qui l'a envoyé)
				.build();
		
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.header(HttpHeaders.SET_COOKIE, cookie.toString()) //convertir le cookie en string 
				// TODO remove token 
				.body(token); 
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
