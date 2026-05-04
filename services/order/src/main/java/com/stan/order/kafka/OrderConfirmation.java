package com.stan.order.kafka;



import com.stan.order.customerHelper.CustomerResponse;
import com.stan.order.enums.PaymentMethod;
import com.stan.order.productHelper.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
    String orderReference,
    BigDecimal totalAmount,
    PaymentMethod paymentMethod,
    CustomerResponse customer,
    List<PurchaseResponse> products

) {
}
