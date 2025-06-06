package com.paymybuddy.api.services.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChangeEmailDto(
		@NotNull
		@NotBlank
		String oldEmail,
		
		@Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
		@NotNull
		@NotBlank
		String newEmail
		) {}
