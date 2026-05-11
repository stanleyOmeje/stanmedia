package com.stan.order.productHelper;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class GrandPurchaseResponse {
    private BigDecimal grandTotal;
    private List<PurchaseResponse> purchaseResponses = new ArrayList<>();
}
