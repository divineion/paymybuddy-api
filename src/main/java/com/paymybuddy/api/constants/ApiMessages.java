package com.paymybuddy.api.constants;

public class ApiMessages {
	public static final String INVALID_CREDENTIALS = "Invalid credentials";
	public static final String ROLE_NOT_FOUND = "Role not found during user initialization: ";
	public static final String FORBIDDEN_ACCESS = "Forbidden access";
	public static final String INVALID_REQUEST_FORMAT = "Invalid request format";
	public static final String INVALID_METHOD_ARGUMENT = "Invalid method argument";
	
	public static final String USER_NOT_FOUND = "No user found  with id: ";
	public static final String USER_NOT_SOFT_DELETED = "User must be soft-deleted first";
	public static final String USER_ALREADY_SOFT_DELETED = "This user has already been soft-deleted.";
	public static final String USER_DELETION_NOT_ALLOWED = "User must have been soft-deleted for at least 6 months before deletion";
	
	public static final String PASSWORD_MISMATCH = "The provided password doesn't match the user's current password";
	public static final String SAME_PASSWORD = "The new password must be different from the old one";
	public static final String PASSWORD_SUCCESSFULLY_UPDATED = "The user password has been updated";
	
	public static final String EMAIL_ALREADY_EXISTS = "There is an account with that email address: ";
	public static final String EMAIL_NOT_FOUND = "The provided email does not match any user: ";
	public static final String EMAIL_SUCCESSFULLY_UPDATED = "The user email has been updated";
	public static final String SAME_EMAIL = "The new email must be different from the old one";;
	
	public static final String RELATION_ALREADY_EXISTS = "The target user is already one of the current user's beneficiaries: ";
	public static final String RELATION_NOT_FOUND = "The target user is not one of the current user's beneficiaries.";
	public static final String SELF_RELATION = "The user cannot add himself as a beneficiary.";
	
	public static final String INSUFFICIENT_BALANCE = "The user account balance is insufficient for this transaction.";
	public static final String INSUFFICIENT_AMOUNT = "The transfered amount cannot be less than 1€.";
}
