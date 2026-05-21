package com.stan.product.product.service;


import com.stan.product.product.dto.request.CreateCategoryRequest;
import com.stan.product.product.dto.request.UpdateCategoryRequest;
import com.stan.product.product.dto.response.CategoryDto;
import com.stan.product.product.dto.response.DefaultResponse;

import java.util.List;

public interface CategoryService {
     DefaultResponse createCategory(CreateCategoryRequest request);

    DefaultResponse<List<CategoryDto>>  getAllCategory();

    DefaultResponse<CategoryDto> getCategoryByCode(String categoryCode);

    DefaultResponse<CategoryDto> updateCategoryByCode(String categoryCode, UpdateCategoryRequest request);
}
