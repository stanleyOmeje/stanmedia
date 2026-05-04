package com.stan.order.paymentHelper.dto;




import com.stan.order.customerHelper.CustomerResponse;
import com.stan.order.enums.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
    BigDecimal amount,
    PaymentMethod paymentMethod,
    long orderId,
    String orderReference,
    CustomerResponse customer
) {
}
