package com.stan.payment.dto.orderdto.response;

import com.stan.payment.enums.PaymentMethod;

import java.math.BigDecimal;

public record OrderResponse(
    Long id,
    String reference,
    BigDecimal amount,
    PaymentMethod paymentMethod,
    Long customerId
) {
}
