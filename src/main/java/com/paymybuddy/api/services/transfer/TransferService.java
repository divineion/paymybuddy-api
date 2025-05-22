package com.paymybuddy.api.services.transfer;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.api.constants.ApiMessages;
import com.paymybuddy.api.constants.TransferSettings;
import com.paymybuddy.api.exceptions.InsufficientAmountException;
import com.paymybuddy.api.exceptions.InsufficientBalanceException;
import com.paymybuddy.api.exceptions.RelationNotFoundException;
import com.paymybuddy.api.exceptions.UserNotFoundException;
import com.paymybuddy.api.model.Transfer;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.repositories.TransferRepository;
import com.paymybuddy.api.repositories.UserRepository;
import com.paymybuddy.api.services.dto.TransferDto;
import com.paymybuddy.api.services.dto.TransferRequestDto;
import com.paymybuddy.api.utils.AmountUtils;

@Service
public class TransferService {

	private final TransferMapper mapper;
	private final TransferRepository repository;
	private final UserRepository userRepository;

	public TransferService(TransferMapper mapper, TransferRepository repository, UserRepository userRepository) {
		this.mapper = mapper;
		this.repository = repository;
		this.userRepository = userRepository;
	}

	/**
	 * Fetches sender and receiver.
	 * Processes business validation (the receiver is one of the sender's beneficiaries, the sender balance in sufficient, 
	 * the min transfer amount is OK)
	 * Maps the {@link TransferRequestDto} to a {@link Transfer}
	 * @param transferReqDto
	 * 
	 * @return {@link TransferDto}
	 * @throws UserNotFoundException
	 * @throws InsufficientBalanceException
	 * @throws InsufficientAmountException
	 * @throws RelationNotFoundException
	 */
	@Transactional(rollbackFor = Exception.class)
	public TransferDto createTransfer(TransferRequestDto transferReqDto) throws UserNotFoundException,
			InsufficientBalanceException, InsufficientAmountException, RelationNotFoundException {
		User sender = userRepository.findById(transferReqDto.senderId())
				.orElseThrow(() -> new UserNotFoundException(ApiMessages.USER_NOT_FOUND));
		
		User receiver = userRepository.findById(transferReqDto.receiverId())
				.orElseThrow(() -> new UserNotFoundException(ApiMessages.USER_NOT_FOUND));

		if (!userRepository.beneficiaryExists(transferReqDto.senderId(), transferReqDto.receiverId())) {
			throw new RelationNotFoundException(ApiMessages.RELATION_NOT_FOUND);
		}

		if (AmountUtils.calculateAmountWithFees(transferReqDto.amount()).compareTo(sender.getBalance()) == 1) {
			throw new InsufficientBalanceException(ApiMessages.INSUFFICIENT_BALANCE);
		}

		if (transferReqDto.amount().compareTo(TransferSettings.MIN_TRANSFER_AMOUNT) == -1) {
			throw new InsufficientAmountException(ApiMessages.INSUFFICIENT_AMOUNT);
		}

		BigDecimal amount = transferReqDto.amount();
		
		BigDecimal updatedSenderBalance = sender.getBalance()
				.subtract(AmountUtils.calculateAmountWithFees(amount));
		
		BigDecimal updatedReceiverBalance = receiver.getBalance().add(amount);

		// map tranfer dto to transfer entity
		Transfer transfer = mapper.fromTransferRequestDtoToTransfer(transferReqDto, sender, receiver);

		userRepository.updateBalance(sender.getId(), updatedSenderBalance);
		userRepository.updateBalance(receiver.getId(), updatedReceiverBalance);
		repository.save(transfer);

		TransferDto transferDto = mapper.fromTransferToTransferDto(transfer, transferReqDto.senderId());

		return transferDto;
	}

}
