package com.stan.product.product.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
public class BulkProductResponse {
    private int rowCount;
    private int successCount;
    private int failureCount;
    private List<ErrorRow> errors = new ArrayList<>();

}
