/*
 * Copyright (c) 2023 Payoneer Germany GmbH. All rights reserved.
 */
package eu.qwan.vender;

public class UnsupportedPaymentMethodException extends RuntimeException {

	UnsupportedPaymentMethodException(String message) {
		super(message);
	}
}
