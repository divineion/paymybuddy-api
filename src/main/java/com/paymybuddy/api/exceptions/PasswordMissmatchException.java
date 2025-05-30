package com.paymybuddy.api.exceptions;

public class PasswordMissmatchException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PasswordMissmatchException(String message) {
		super(message);
	}

}
