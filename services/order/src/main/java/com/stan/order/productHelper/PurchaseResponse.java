package com.stan.order.productHelper;

import java.math.BigDecimal;

public record PurchaseResponse(
    String name,
     String description,
     BigDecimal price,
    String categoryName,
     double quantity,
     BigDecimal totalPrice
) {
}
