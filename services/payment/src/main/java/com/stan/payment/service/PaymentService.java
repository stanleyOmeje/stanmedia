package com.stan.payment.service;

import com.stan.payment.dto.request.PaymentRequest;

public interface PaymentService {
    Long makePayment(PaymentRequest paymentRequest);
}
