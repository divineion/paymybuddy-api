package com.paymybuddy.api.constants;

public class ApiMessages {
	public static final String USER_NOT_FOUND = "No user found  with id: ";
	public static final String EMAIL_NOT_FOUND = "The provided email does not match any user: ";
	
	public static final String RELATION_ALREADY_EXISTS = "The target user is already one of the current user's beneficiaries: ";
	public static final String SELF_RELATION = "The user cannot add himself as a beneficiary.";
	public static final String EMAIL_ALREADY_EXISTS = "There is an account with that email adress: ";
	public static final String INSUFFICIENT_BALANCE = "The user account balance is insufficient for this transaction";
}
