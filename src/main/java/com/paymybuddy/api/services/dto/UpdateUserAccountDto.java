package com.paymybuddy.api.services.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserAccountDto(
		@NotBlank
		@Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
	    String email,
	    
		@Size(min = 8, max = 30, message = "The password must contain between 8 and 30 characters")
		@Pattern(
		        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_]).{8,30}$",
		        message = "The password must contain at least one upper, one lower, one number and one special character"
		    )
		@NotBlank
	    String currentPassword,
	    
	    @Size(min = 8, max = 128, message = "The password must contain between 8 and 30 characters")
		@Pattern(
		        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_]).{8,30}$",
		        message = "The password must contain at least one upper, one lower, one number and one special character"
		    )
		@NotBlank
	    String newPassword
	) {}
