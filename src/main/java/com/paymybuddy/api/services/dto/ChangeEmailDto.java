package com.paymybuddy.api.services.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ChangeEmailDto(
		@NotBlank
		String oldEmail,
		
		@Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
		@NotBlank
		String newEmail
		) {}
