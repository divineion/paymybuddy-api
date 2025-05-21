package com.paymybuddy.api.services.transfer;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.paymybuddy.api.model.Transfer;
import com.paymybuddy.api.services.dto.BeneficiaryDto;
import com.paymybuddy.api.services.dto.TransferDto;
import com.paymybuddy.api.utils.AmountRounder;

@Service
public class TransferMapper {
	/**
	 * This method maps a {@link Transfer} to a {@link TransferDto} adapted for a specific user. The
	 * amount shown depends on whether the user is the sender (totalAmount) or the
	 * receiver (amount).
	 * 
	 * @param transfer the {@link Transfer} entity to map
	 * @param userId   the id of the user requesting the transfer information
	 * @return a {@link TransferDto} representing the transfer from the perspective
	 *         of the specified user
	 */
	public TransferDto fromTransferToTransferDto(Transfer transfer, int userId) {
		BeneficiaryDto sender = new BeneficiaryDto(transfer.getSender().getId(), transfer.getSender().getUsername());
		BeneficiaryDto receiver = new BeneficiaryDto(transfer.getReceiver().getId(),
				transfer.getReceiver().getUsername());

		BigDecimal amount = AmountRounder.round(transfer.getSender().getId() == userId ? transfer.getTotalAmount() : transfer.getAmount());

		return new TransferDto(transfer.getId(), sender, receiver, transfer.getDescription(), amount,
				transfer.getDate());
	}
}
