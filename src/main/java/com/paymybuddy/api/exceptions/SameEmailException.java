package com.paymybuddy.api.exceptions;

public class SameEmailException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SameEmailException(String message) {
		super(message);
	}
}
