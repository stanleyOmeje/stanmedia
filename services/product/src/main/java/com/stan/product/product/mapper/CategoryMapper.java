package com.stan.product.product.mapper;


import com.stan.product.product.dto.request.CreateCategoryRequest;
import com.stan.product.product.dto.response.CategoryDto;
import com.stan.product.product.entity.Category;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CategoryMapper {

    public Category mapCreateCategoryRequestToCategory(CreateCategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setCode(request.getCode());
        category.setDescription(request.getDescription());
        category.setCreatedAt(new Date());
        return category;
    }

    public CategoryDto mapCategoryToCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCode(category.getCode());
        categoryDto.setId((long)category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());
        return categoryDto;
    }
}
