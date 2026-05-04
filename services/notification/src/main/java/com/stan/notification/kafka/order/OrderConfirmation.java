package com.stan.notification.kafka.order;

import com.stan.notification.enums.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation (
    String orderReference,
    BigDecimal amount,
    PaymentMethod paymentMethod,
    Customer customer,
    List<Product> product

){
}
