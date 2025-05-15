package com.paymybuddy.api.services.user;

import org.springframework.stereotype.Service;

import com.paymybuddy.api.exceptions.EmailNotFoundException;
import com.paymybuddy.api.exceptions.SelfRelationException;
import com.paymybuddy.api.model.User;
import com.paymybuddy.api.repositories.UserRepository;

@Service
public class UserValidator {
	private final UserRepository repository;
	
	private UserValidator(UserRepository repository) {
		this.repository = repository;
	}

	public User validateEmailExists(int id, String email) throws EmailNotFoundException, SelfRelationException {
		User user = repository.findByActiveEmail(email).orElseThrow(() -> new EmailNotFoundException("the provided email doesn't match any user"));
		if (user.getId() == id) {
			throw new SelfRelationException("You cannot add yourself as a beneficiary.");
		}
		return user;
	}
	
}
