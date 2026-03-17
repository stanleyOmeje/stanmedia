package com.stan.product.product.dto.request;


import com.stan.product.product.enums.Belt;
import com.stan.product.product.enums.FeeType;
import com.stan.product.product.enums.ProductType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateProductRequest {
    private String name;
    @NotBlank(message = "product code cannot be blank")
    private String code;
    private String description;
    private Belt belt;
    private FeeType feeType;
    private BigDecimal price;
    private String categoryCode;
}
