package com.stan.product.product.dto.request;

import com.stan.product.product.enums.Belt;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseRequest {
    private String productCode;
    private BigDecimal amount;
    private double quantity;
    private Belt belt;
}
