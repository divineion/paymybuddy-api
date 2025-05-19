package com.paymybuddy.api.constants;

public class ApiMessages {
	public static final String USER_NOT_FOUND = "No user found  with id: ";
	public static final String EMAIL_NOT_FOUND = "The provided email does not match any user: ";
	
	public static final String RELATION_ALREADY_EXISTS = "The target user is already one of the current user's beneficiaries: ";
	public static final String SELF_RELATION = "The user cannot add himself as a beneficiary.";
}
