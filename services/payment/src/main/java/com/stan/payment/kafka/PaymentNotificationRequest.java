package com.stan.payment.kafka;

import com.stan.payment.enums.PaymentMethod;

import java.math.BigDecimal;

public record PaymentNotificationRequest(
    String orderReference,
    BigDecimal amount,
    PaymentMethod paymentMethod,
    String customerFirstName,
    String customerLastName,
    String customerEmail

) {
}
