package com.stan.order.dto.request;

import com.stan.order.enums.Belt;

import java.math.BigDecimal;

public record PurchaseRequest(
     String productCode,
     BigDecimal amount,
     double quantity,
     Belt belt
) {
}
