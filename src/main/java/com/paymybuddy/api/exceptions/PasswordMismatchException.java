package com.paymybuddy.api.exceptions;

public class PasswordMismatchException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PasswordMismatchException(String message) {
		super(message);
	}

}
