package com.paymybuddy.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class User {
	private int id;
	
	private String username;
	
	private String email;
	
	private LocalDateTime deletedAt;
	
	private BigDecimal balance;
	
	private String password;
	
	private String activeEmail;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public LocalDateTime getDeletedAt() {
		return deletedAt;
	}
	
	public void setDeletedAt(LocalDateTime deletedAt) {
		this.deletedAt = deletedAt;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getActiveEmail() {
		return activeEmail;
	}
	
	public void setActiveEmail(String activeEmail) {
		this.activeEmail = activeEmail;
	}
}
