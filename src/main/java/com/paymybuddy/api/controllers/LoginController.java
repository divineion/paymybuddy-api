package com.paymybuddy.api.controllers;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.api.config.CustomUserDetails;
import com.paymybuddy.api.config.JWTService;
import com.paymybuddy.api.services.dto.UserLoginDto;

@RestController
public class LoginController {
	private final JWTService jwtService;
	private final AuthenticationManager authenticationManager;
	
	public LoginController(JWTService jwtService, AuthenticationManager authenticationManager) {
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
	}
	
	// récupérer l'Authentication 
	// JWTService founit le token
	@PostMapping("/api/login_check")
	public String getToken(@RequestBody UserLoginDto loginDto) {
		//créer un auth token via spring security
		Authentication authToken = new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password());
		
		// d'aborsd authentifier le user - retourne 401 Unauthorized si erreur d'authentification
		Authentication authenticatedUser = authenticationManager.authenticate(authToken);
		
		// injecter le CustomUserDetails comme principal
	    CustomUserDetails userDetails = (CustomUserDetails) authenticatedUser.getPrincipal();

	    //passzr le CustomUserDetails pour l'utiliser dans le JWTService
		String token = jwtService.generateToken(userDetails);
		
		return token;
	}

}
