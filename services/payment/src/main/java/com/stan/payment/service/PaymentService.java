package com.stan.payment.service;

import com.stan.payment.dto.request.PaymentRequest;
import com.stan.payment.dto.response.CreatePaymentResponse;
import com.stan.payment.dto.response.DefaultResponse;


public interface PaymentService {
    DefaultResponse<CreatePaymentResponse> makePayment(PaymentRequest paymentRequest);
}
