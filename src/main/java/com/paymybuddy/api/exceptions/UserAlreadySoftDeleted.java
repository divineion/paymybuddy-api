package com.paymybuddy.api.exceptions;

public class UserAlreadySoftDeleted extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public UserAlreadySoftDeleted(String message) {
		super(message);
	}

}
