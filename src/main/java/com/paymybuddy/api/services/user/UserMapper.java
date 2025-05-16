package com.paymybuddy.api.services.user;

import org.springframework.stereotype.Service;

import com.paymybuddy.api.model.User;
import com.paymybuddy.api.model.dto.BeneficiaryDto;
import com.paymybuddy.api.model.dto.UserDto;

@Service
public class UserMapper {
	
	public UserDto fromUserToUserDto(User user) {
		return new UserDto(user.getId(), user.getUsername(), user.getEmail());
	}
	
	public BeneficiaryDto fromUserToBeneficiaryDto(User user) {
		return new BeneficiaryDto(user.getId(), user.getUsername());
	}
}