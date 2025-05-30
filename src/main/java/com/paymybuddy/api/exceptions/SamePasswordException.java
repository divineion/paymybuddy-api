package com.paymybuddy.api.exceptions;

public class SamePasswordException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SamePasswordException(String message) {
		super(message);
	}
}
