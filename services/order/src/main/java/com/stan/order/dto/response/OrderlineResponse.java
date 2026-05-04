package com.stan.order.dto.response;

import com.stan.order.enums.Belt;

import java.math.BigDecimal;

public record OrderlineResponse(
    long id,
     String productCode,
    BigDecimal amount,
    double quantity,
    Belt belt
) {
}
