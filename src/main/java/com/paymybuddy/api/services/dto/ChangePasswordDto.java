package com.paymybuddy.api.services.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangePasswordDto(
			@NotBlank
			String oldPassword, 
			
			@Size(min = 8, max = 128, message = "The password must contain between 8 and 128 characters")
			@Pattern(
			        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_]).{8,128}$",
			        message = "The password must contain at least one upper, one lower, one number and one special character"
			    )
			@NotBlank
			String newPassword
		) {}
