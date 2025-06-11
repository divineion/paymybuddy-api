package com.paymybuddy.api.services.user;

import org.springframework.stereotype.Service;

import com.paymybuddy.api.constants.ApiMessages;
import com.paymybuddy.api.exceptions.SelfRelationException;
import com.paymybuddy.api.model.User;

@Service
public class UserValidator {

	public boolean validateUserCanAddAsBeneficiary(int currentUserId, User targetUser) throws SelfRelationException {
		if (targetUser.getId() == currentUserId) {
			throw new SelfRelationException(ApiMessages.SELF_RELATION);
		}
		return true;
	}
	
}
