package com.paymybuddy.api.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record TransferDto(int id, BeneficiaryDto sender, BeneficiaryDto receiver, String description, BigDecimal amount,
		 @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime date) {
}