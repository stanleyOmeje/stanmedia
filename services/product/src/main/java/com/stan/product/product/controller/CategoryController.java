package com.stan.product.product.controller;


import com.stan.product.product.config.ApplicationUrl;
import com.stan.product.product.dto.request.CreateCategoryRequest;
import com.stan.product.product.dto.request.UpdateCategoryRequest;
import com.stan.product.product.dto.response.CategoryDto;
import com.stan.product.product.dto.response.DefaultResponse;
import com.stan.product.product.enums.ResponseStatus;
import com.stan.product.product.exception.AlreadyExistException;
import com.stan.product.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(ApplicationUrl.CATEGORY_BASE_URL)
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<DefaultResponse<?>> createCategory(@RequestBody CreateCategoryRequest request
    ) {
        DefaultResponse<?> response = categoryService.createCategory(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<DefaultResponse<List<CategoryDto>>> getAllCategory() {
        DefaultResponse<List<CategoryDto>>  response = categoryService.getAllCategory();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{categoryCode}")
    public ResponseEntity<DefaultResponse<CategoryDto>> getCategoryByCode(@PathVariable String categoryCode) {
        DefaultResponse<CategoryDto>  response = categoryService.getCategoryByCode(categoryCode);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{categoryCode}")
    public ResponseEntity<DefaultResponse<CategoryDto>> updateCategoryByCode(@PathVariable String categoryCode,
                                                                          @RequestBody UpdateCategoryRequest request) {
        DefaultResponse<CategoryDto>  response = categoryService.updateCategoryByCode(categoryCode, request);
        return ResponseEntity.ok(response);
    }
}
