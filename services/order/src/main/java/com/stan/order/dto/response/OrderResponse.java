package com.stan.order.dto.response;


import com.stan.order.enums.PaymentMethod;

import java.math.BigDecimal;

public record OrderResponse(
    Long id,
    String reference,
    BigDecimal amount,
    PaymentMethod paymentMethod,
    Long customerId
) {
}
