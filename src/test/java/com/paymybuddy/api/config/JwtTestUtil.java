package com.paymybuddy.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtTestUtil {
	@Autowired
	private JWTService jwtService;
	
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	public String generateToken(String username) {
		UserDetails user = userDetailsService.loadUserByUsername(username);

		return jwtService.generateToken((CustomUserDetails) user);
	}
}
