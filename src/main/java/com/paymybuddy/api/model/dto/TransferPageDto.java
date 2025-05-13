package com.paymybuddy.api.model.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public record TransferPageDto(int id, Set<BeneficiaryDto> beneficiaries, BigDecimal balance,
		List<TransferDto> sentTransfers, List<TransferDto> receivedTransfers) {
}
