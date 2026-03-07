package com.stan.product.product.dto.request;


import com.stan.product.product.enums.Belt;
import com.stan.product.product.enums.FeeType;
import com.stan.product.product.enums.ProductType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateProductRequest {
    private String name;
    private String description;
    private Double availableQuantity;
    private ProductType productType;
    private Belt belt;
    private FeeType feeType;
    private BigDecimal price;
    private String categoryCode;
}
