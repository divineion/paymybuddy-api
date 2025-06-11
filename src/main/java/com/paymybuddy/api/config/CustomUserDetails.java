package com.paymybuddy.api.config;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int id;
	private final String activeEmail;
	private final String password;
	private final List<GrantedAuthority> authorities;
	
	public CustomUserDetails(int id, String email, String password, List<GrantedAuthority> authorities) {
		this.id = id;
		this.activeEmail = email;
		this.password = password;
		this.authorities = authorities;
	}

	public int getId() {
		return id;
	}

	public String getEmail() {
		return activeEmail;
	}

	public String getPassword() {
		return password;
	}

	public List<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getUsername() {
		return activeEmail;
	}
	
	@Override
	public boolean isEnabled() {
		if (activeEmail == null) {
			return false;
		}
		
		return true;
	}
}
