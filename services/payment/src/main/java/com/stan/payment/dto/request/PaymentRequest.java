package com.stan.payment.dto.request;

import com.stan.payment.enums.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
    BigDecimal amount,
    PaymentMethod paymentMethod,
    long orderId,
    String orderReference,
    Customer customer
) {
}
