package com.paymybuddy.api.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.api.config.JWTService;

@RestController
public class LoginController {
	private final JWTService jwtService;
	
	public LoginController(JWTService jwtService) {
		this.jwtService = jwtService;
	}
	
	// Security fournit l'authentication à partir de la requête basic auth
	// JWTService founit le token
	@PostMapping("/login_check")
	public String getToken(Authentication auth) {
		String token = jwtService.generateToken(auth);
		
		return token;
	}
}
