package com.paymybuddy.api.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AmountRounder {
	public static BigDecimal round(BigDecimal amount) {
		return amount.setScale(2, RoundingMode.HALF_UP);
	}
}
