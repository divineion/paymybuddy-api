package com.paymybuddy.api.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferDto(int id, BeneficiaryDto sender, BeneficiaryDto receiver, String description, BigDecimal amount,
		LocalDateTime date) {
}