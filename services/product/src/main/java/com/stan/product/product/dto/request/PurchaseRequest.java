package com.stan.product.product.dto.request;

import lombok.Data;

@Data
public class PurchaseRequest {
    private String productCode;
    private double quantity;
}
