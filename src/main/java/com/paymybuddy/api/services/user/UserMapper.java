package com.paymybuddy.api.services.user;

import java.math.BigDecimal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.paymybuddy.api.constants.ApiMessages;
import com.paymybuddy.api.exceptions.RoleNotFoundException;
import com.paymybuddy.api.model.Role;
import com.paymybuddy.api.model.RoleName;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.repositories.RoleRepository;
import com.paymybuddy.api.services.dto.BeneficiaryDto;
import com.paymybuddy.api.services.dto.UserAccountDto;
import com.paymybuddy.api.services.dto.UserDto;

@Service
public class UserMapper {

	private final PasswordEncoder passwordEncoder;
	private final RoleRepository roleRepository;

	UserMapper(PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
	}

	public UserDto fromUserToUserDto(User user) {
		return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getBalance());
	}

	public User fromUserDtoToUserReferenceOnly(UserDto sender) {
		return User.referenceOnly(sender.id(), sender.username(), sender.email(), sender.balance());
	}

	public BeneficiaryDto fromUserToBeneficiaryDto(User user) {
		return new BeneficiaryDto(user.getId(), user.getUsername());
	}

	public User fromUserAccountDtoToUser(UserAccountDto accountDto) throws RoleNotFoundException {
		RoleName defautRole = RoleName.ROLE_USER;
		
		Role role = roleRepository.findByName(defautRole)
				.orElseThrow(() -> new RoleNotFoundException(ApiMessages.ROLE_NOT_FOUND + defautRole));
		
		return User.forInitialData(null, accountDto.username(), accountDto.email(), BigDecimal.ZERO,
				passwordEncoder.encode(accountDto.password()), role);
	}
}