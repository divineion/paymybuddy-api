package com.paymybuddy.api.exceptions;

public class UserDeletionNotAllowedException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UserDeletionNotAllowedException(String message) {
		super(message);
	}
}
