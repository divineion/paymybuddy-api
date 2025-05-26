package com.paymybuddy.api.controllers;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.paymybuddy.api.config.JWTService;
import com.paymybuddy.api.services.dto.UserLoginDto;

@RestController
public class LoginController {
	private final JWTService jwtService;
	
	public LoginController(JWTService jwtService) {
		this.jwtService = jwtService;
	}
	
	// récupérer l'Authentication 
	// JWTService founit le token
	@PostMapping("/login_check")
	public String getToken(@RequestBody UserLoginDto loginDto) {
		//créer un auth token via spring security
		Authentication auth = new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password());
		
		String token = jwtService.generateToken(auth);
		
		return token;
	}

}
