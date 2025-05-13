package com.paymybuddy.api.services.finder;

import org.springframework.stereotype.Service;

import com.paymybuddy.api.exceptions.UserNotFoundException;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.model.dto.UserDto;
import com.paymybuddy.api.repositories.UserRepository;
import com.paymybuddy.api.services.mapper.UserMapper;

@Service
public class UserFinderService {

	private final UserRepository userRepository;
	private final UserMapper mapper;

	private UserFinderService(UserRepository userRepository, UserMapper mapper) {
		this.userRepository = userRepository;
		this.mapper = mapper;
	}

	public UserDto findUserById(int id) throws UserNotFoundException {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("no user found  with id " + id));
		UserDto userDto = mapper.fromUserToUserDto(user);

		return userDto;
	}


}
