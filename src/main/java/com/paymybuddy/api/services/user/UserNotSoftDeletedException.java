package com.paymybuddy.api.services.user;

public class UserNotSoftDeletedException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public UserNotSoftDeletedException(String message) {
		super(message);
	}

}
