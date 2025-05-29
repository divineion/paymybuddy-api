package com.paymybuddy.api.services.dto;

import java.math.BigDecimal;

/**
 * DTO that represents a transfer made by a user.
 *
 * @param id the id of the sender (user who initiates the transfer)
 * @param receiverId the id of the user who receives the transfer
 * @param description a short description of the transfer
 * @param amount the amount sent to the receiver (excluding any fees)
 */
public record TransferRequestDto(int id, int receiverId, String description, BigDecimal amount) {}
