package com.paymybuddy.api.services.user;

import java.math.BigDecimal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.paymybuddy.api.model.User;
import com.paymybuddy.api.model.dto.BeneficiaryDto;
import com.paymybuddy.api.model.dto.UserAccountDto;
import com.paymybuddy.api.model.dto.UserDto;

@Service
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
	
	public UserDto fromUserToUserDto(User user) {
		return new UserDto(user.getId(), user.getUsername(), user.getEmail());
	}
	
	public BeneficiaryDto fromUserToBeneficiaryDto(User user) {
		return new BeneficiaryDto(user.getId(), user.getUsername());
	}

	public User fromUserAccountDtoToUser(UserAccountDto accountDto) {
		return User.forInitialData(null, accountDto.username(), accountDto.email(), BigDecimal.ZERO, passwordEncoder.encode(accountDto.password()));
	}
}