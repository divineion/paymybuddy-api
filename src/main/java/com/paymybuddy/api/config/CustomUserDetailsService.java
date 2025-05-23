package com.paymybuddy.api.config;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.paymybuddy.api.constants.ApiMessages;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository repository;
	
	public CustomUserDetailsService(UserRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// récupérer le user
		User user = repository.findByActiveEmail(username).orElseThrow(() -> new UsernameNotFoundException(ApiMessages.INVALID_CREDENTIALS));
		// build un UserDetails avec mes identifiants de conn
		return org.springframework.security.core.userdetails.User.builder()
				.username(user.getActiveEmail())
				.password(user.getPassword())
				.build();
	}

}
