package com.stan.product.product.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseResponse {
    private String name;
    private String description;
    private BigDecimal price;
    private String categoryName;
}
