package com.stan.product.product.dto.response;

import lombok.Data;

@Data
public class CategoryDto {
    private Long id;
    private String name;
    private String code;
    private String description;
}
