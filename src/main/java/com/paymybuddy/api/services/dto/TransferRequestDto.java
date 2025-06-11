package com.paymybuddy.api.services.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO that represents a transfer made by a user.
 *
 * @param id the id of the sender (user who initiates the transfer)
 * @param receiverId the id of the user who receives the transfer
 * @param description a short description of the transfer
 * @param amount the amount sent to the receiver (excluding any fees)
 */
public record TransferRequestDto(
		@NotNull(message = "sender id is required") 
		int id,

		@NotNull(message = "receiver id is required") 
		int receiverId,

		@Size(min = 10, max = 255, message = "description size must be between 10 and 255 characters ") 
		@NotBlank(message = "description is required") 
		String description,

		@NotNull(message = "amount is required") 
		@Min(value = 1, message = "Amount must be greater than or equal to 1.") 
		BigDecimal amount
	){}
