package com.stan.order.dto.response;


import com.stan.order.enums.PaymentMethod;

import java.math.BigDecimal;

public record CreateOrderResponse(
    String orderReference
) {
}
