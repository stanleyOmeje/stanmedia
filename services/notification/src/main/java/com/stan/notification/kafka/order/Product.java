package com.stan.notification.kafka.order;

import java.math.BigDecimal;

public record Product (
     Long productId,
     String name,
     String description,
     BigDecimal price,
     double quantity
){
}
