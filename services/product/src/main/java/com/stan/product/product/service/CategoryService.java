package com.stan.product.product.service;


import com.stan.product.product.dto.request.CreateCategoryRequest;
import com.stan.product.product.dto.response.DefaultResponse;

public interface CategoryService {
     DefaultResponse createCategory(CreateCategoryRequest request);
}
