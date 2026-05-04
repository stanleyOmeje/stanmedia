package com.stan.order.dto.request;

import com.stan.order.enums.Belt;
import java.math.BigDecimal;

public record OrderlineRequest(
    Long id,
    String productCode,
    BigDecimal amount,
    double quantity,
    Belt belt
) {
}
