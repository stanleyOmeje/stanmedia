package com.stan.product.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateCategoryRequest {
    private String name;
    @NotBlank(message = "category code cannot be blank")
    private String code;
    private String description;
}
