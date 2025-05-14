package com.paymybuddy.api.model.dto;

import java.math.BigDecimal;
import java.util.List;

public record TransferPageDto(int id, List<BeneficiaryDto> beneficiaries, BigDecimal balance,
		List<TransferDto> sentTransfers, List<TransferDto> receivedTransfers) {
}
