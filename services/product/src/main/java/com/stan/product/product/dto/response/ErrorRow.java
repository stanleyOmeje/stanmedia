package com.stan.product.product.dto.response;

import lombok.Data;

@Data
public class ErrorRow {
    private int rowNumber;
    private String code;
    private String reason;
}
