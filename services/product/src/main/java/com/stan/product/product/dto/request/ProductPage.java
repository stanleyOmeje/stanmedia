package com.stan.product.product.dto.request;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class ProductPage {
    private int pageNo = 0;
    private int pageSize = 10;
    private Sort.Direction direction = Sort.Direction.DESC;
    private String sortBy= "id";
}
