package com.paymybuddy.api.services.user;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.paymybuddy.api.exceptions.EmailNotFoundException;
import com.paymybuddy.api.exceptions.SelfRelationException;
import com.paymybuddy.api.exceptions.UserNotFoundException;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.model.dto.BeneficiaryDto;
import com.paymybuddy.api.model.dto.EmailRequestDto;
import com.paymybuddy.api.model.dto.TransferDto;
import com.paymybuddy.api.model.dto.TransferPageDto;
import com.paymybuddy.api.model.dto.UserDto;
import com.paymybuddy.api.repositories.UserRepository;
import com.paymybuddy.api.services.transfer.TransferMapper;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final UserMapper mapper;
	private final TransferMapper transferMapper;
	private final UserValidator validator;

	private UserService(UserRepository userRepository, UserMapper mapper, TransferMapper transferMapper, UserValidator validator) {
		this.userRepository = userRepository;
		this.mapper = mapper;
		this.transferMapper = transferMapper;
		this.validator = validator;
	}

	public UserDto findUserById(int id) throws UserNotFoundException {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("no user found  with id " + id));
		UserDto userDto = mapper.fromUserToUserDto(user);

		return userDto;
	}

	public TransferPageDto findUserTransferPageInfo(int id) throws UserNotFoundException {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("no user found  with id " + id));

		List<BeneficiaryDto> beneficiaries = user.getBeneficiaries().stream()
				.map(mapper::fromUserToBeneficiaryDto)
				.sorted(Comparator.comparing(BeneficiaryDto::username))
				.toList();

		List<TransferDto> receivedTransfers = user.getReceivedTransfers().stream()
				.map(transfer -> transferMapper.fromTransferToTransferDto(transfer, id))
				.sorted(Comparator.comparing(TransferDto::date).reversed())
				.toList();

		List<TransferDto> sentTransfers = user.getSentTransfers().stream()
				.map(transfer -> transferMapper.fromTransferToTransferDto(transfer, id))
				.sorted(Comparator.comparing(TransferDto::date).reversed())
				.toList();

		TransferPageDto userTransferInfo = new TransferPageDto(user.getId(), beneficiaries, user.getBalance(),
				sentTransfers, receivedTransfers);

		return userTransferInfo;
	}
	
	// POST REQUESTS
	
	//add a relation
	// first of all verify the provided email address exists
	public UserDto addBeneficiary(int id, EmailRequestDto emailDto) throws EmailNotFoundException, SelfRelationException {
		String email = emailDto.email();
		User searchedUser = validator.validateEmailExists(id, email);
			return mapper.fromUserToUserDto(searchedUser);
	}

	
	
}
