package com.tin.service;

import com.paypal.base.rest.PayPalRESTException;

public interface PaymentService {

	com.paypal.api.payments.Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;
	
	com.paypal.api.payments.Payment createPayment(
			Double total,
			String currency,
			String method,
			String intent,
			String description,
			String cancelUrl,
			String successUrl) throws PayPalRESTException;	
}
