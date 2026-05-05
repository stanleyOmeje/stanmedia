package com.stan.order.dto.request;



import com.stan.order.enums.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(
//    String reference,
    BigDecimal amount,
    PaymentMethod paymentMethod,
    String email,
    List<PurchaseRequest> purchaseRequest
) {
}
