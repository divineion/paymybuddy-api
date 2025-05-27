package com.paymybuddy.api.config;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
		User user = repository.findByActiveEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException(ApiMessages.INVALID_CREDENTIALS));
		// build un UserDetails avec les prop dont j'ai besoin
		List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().getName().name()));
		
		return new CustomUserDetails(user.getId(), user.getActiveEmail(), user.getPassword(), authorities);
	}
}
