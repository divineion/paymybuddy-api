package com.paymybuddy.api.services.transfer;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paymybuddy.api.constants.ApiMessages;
import com.paymybuddy.api.constants.TransferSettings;
import com.paymybuddy.api.exceptions.InsufficientAmountException;
import com.paymybuddy.api.exceptions.InsufficientBalanceException;
import com.paymybuddy.api.exceptions.UserNotFoundException;
import com.paymybuddy.api.model.Transfer;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.repositories.TransferRepository;
import com.paymybuddy.api.repositories.UserRepository;
import com.paymybuddy.api.services.dto.TransferDto;
import com.paymybuddy.api.services.dto.TransferRequestDto;
import com.paymybuddy.api.services.dto.UserDto;
import com.paymybuddy.api.services.user.UserMapper;
import com.paymybuddy.api.services.user.UserService;
import com.paymybuddy.api.utils.AmountUtils;

@Service
public class TransferService {

	private final TransferMapper mapper;
	private final TransferRepository repository;
	private final UserService userService;
	private final UserMapper userMapper;
	private final UserRepository userRepository;


	public TransferService(TransferMapper mapper, TransferRepository repository, UserService userService, UserMapper userMapper, UserRepository userRepository) {
		this.mapper = mapper;
		this.repository = repository;
		this.userService = userService;
		this.userMapper = userMapper;
		this.userRepository = userRepository;
	}

	@Transactional(rollbackFor = Exception.class)
	public TransferDto createTransfer(TransferRequestDto transferReqDto) throws UserNotFoundException, InsufficientBalanceException, InsufficientAmountException {
		// FETCH SENDER infos
		UserDto senderDto = userService.findUserById(transferReqDto.senderId());
		
		if (AmountUtils.calculateAmountWithFees(transferReqDto.amount()).compareTo(senderDto.balance()) == 1) {
			throw new InsufficientBalanceException(ApiMessages.INSUFFICIENT_BALANCE);
		}
		
		if (transferReqDto.amount().compareTo(TransferSettings.MIN_TRANSFER_AMOUNT) == -1) {
			throw new InsufficientAmountException(ApiMessages.INSUFFICIENT_AMOUNT);
		}
		
		// FETCH RECEIVER INFOS
		UserDto receiverDto = userService.findUserById(transferReqDto.receiverId());
		
		// map dtos to user entities
		User sender = userMapper.fromUserDtoToUser(senderDto);
		User receiver = userMapper.fromUserDtoToUser(receiverDto);
		
		BigDecimal amount = transferReqDto.amount();
		BigDecimal updatedSenderBalance = sender.getBalance().subtract(amount.add(amount.multiply(TransferSettings.TRANSFER_FEES)));
		BigDecimal updatedReceiverBalance = receiver.getBalance().add(amount);

		//map tranfer dto to transfer entity 
		Transfer transfer = mapper.fromTransferDtoToTransfer(transferReqDto, sender, receiver);

		repository.save(transfer);
		userRepository.updateBalance(sender.getId(), updatedSenderBalance);
		userRepository.updateBalance(receiver.getId(), updatedReceiverBalance);

		TransferDto transferDto = mapper.fromTransferToTransferDto(transfer, transferReqDto.senderId());
		
		return transferDto;
	}

}
