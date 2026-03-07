package com.stan.product.product.controller;


import com.stan.product.product.config.ApplicationUrl;
import com.stan.product.product.dto.request.CreateCategoryRequest;
import com.stan.product.product.dto.response.DefaultResponse;
import com.stan.product.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(ApplicationUrl.CATEGORY_BASE_URL)
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<DefaultResponse<?>> createCategory(@RequestBody CreateCategoryRequest request
                                                             ) {
        DefaultResponse<?> response = new DefaultResponse<>();
        try {
            response = categoryService.createCategory(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

}
