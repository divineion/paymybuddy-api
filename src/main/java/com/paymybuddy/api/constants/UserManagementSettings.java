package com.paymybuddy.api.constants;

import java.time.LocalDateTime;

/**
 * Configuration settings for user management rules.
 * <p>
 * Provides constants and helper methods.
 */
public class UserManagementSettings {
    /**
     * The minimum number of months that must elapse between
     * a user's soft deletion and their permanent deletion.
     */
	public static final int MIN_MONTHS_BETWEEN_SOFT_DELETE_AND_DELETE = 6;
	
	/**
     * Returns the threshold date before which a user must have been soft-deleted
     * in order to be eligible for permanent deletion.
     * <p>
     * This is calculated as the current date and time minus the minimum
     * required number of months defined by {@link #MIN_MONTHS_BETWEEN_SOFT_DELETE_AND_DELETE}.
     *
     * @return a {@link LocalDateTime} representing the earliest allowed soft deletion date 
     *         for permanent deletion eligibility
     */
	public static LocalDateTime getMinSoftDeleteDate() {
		return LocalDateTime.now().minusMonths(MIN_MONTHS_BETWEEN_SOFT_DELETE_AND_DELETE);
	}
}
