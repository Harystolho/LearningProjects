package com.harystolho.bh;

import java.math.BigDecimal;

public class Money {

	private final BigDecimal amount;

	public Money(BigDecimal amount) throws InvalidMonetaryValueException {
		if (amount.compareTo(BigDecimal.ZERO) == -1)
			throw new InvalidMonetaryValueException();

		this.amount = amount;
	}

	/**
	 * 
	 * @param other
	 * @return
	 * @throws InvalidMonetaryValueException if the new value is not valid. If you
	 *                                       try to subtract 10 from 5 it would
	 *                                       result in -5 that is not a valid value
	 *                                       and would throw this error.
	 */
	public Money subtract(Money other) throws NotEnoughMoneyException {
		try {
			return new Money(amount.subtract(other.amount));
		} catch (InvalidMonetaryValueException e) {
			throw new NotEnoughMoneyException();
		}
	}

	public boolean isLessThan(Money other) {
		return this.amount.compareTo(other.amount) == -1;
	}

	public static class InvalidMonetaryValueException extends RuntimeException {
		public InvalidMonetaryValueException() {
			super("The money amount has to be greater or equal to 0");
		}
	}

	public static class NotEnoughMoneyException extends RuntimeException {
	}

}
