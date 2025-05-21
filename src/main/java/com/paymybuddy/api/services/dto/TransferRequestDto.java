package com.paymybuddy.api.services.dto;

import java.math.BigDecimal;

public record TransferRequestDto(int senderId, int receiverId, String description, BigDecimal amount) {

}
