package com.paymybuddy.api.exceptions;

public class ForbiddenAccessException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ForbiddenAccessException(String message) {
		super(message);
	}

}
