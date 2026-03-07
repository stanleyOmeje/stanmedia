package com.stan.product.product.dto.request;


import com.stan.product.product.enums.Belt;
import com.stan.product.product.enums.FeeType;
import com.stan.product.product.enums.ProductType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductSearchCriteria {
    private String name;
    private String code;
    private String description;
    private ProductType productType;
    private Belt belt;
    private FeeType feeType;
    private BigDecimal price;
    private String categoryCode;
}
