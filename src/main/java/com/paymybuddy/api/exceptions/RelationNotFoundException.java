package com.paymybuddy.api.exceptions;

/**
 * Exception thrown when a relation (beneficiary) is not found in the user's list of relations.
 * <p>
 * This exception is typically used when attempting to remove a beneficiary
 * who does not exist in the current user's relation list.
 * </p>
 */
public class RelationNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public RelationNotFoundException(String message) {
		super(message);
	}

}
