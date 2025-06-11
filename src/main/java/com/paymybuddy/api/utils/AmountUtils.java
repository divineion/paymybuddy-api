package com.paymybuddy.api.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.paymybuddy.api.constants.TransferSettings;

public class AmountUtils {
	public static BigDecimal round(BigDecimal amount) {
		return amount.setScale(2, RoundingMode.HALF_UP);
	}
	
	public static BigDecimal calculateAmountWithFees(BigDecimal amount) {
		BigDecimal fees = TransferSettings.TRANSFER_FEES;
		return amount.add(amount.multiply(fees));
	}
}
