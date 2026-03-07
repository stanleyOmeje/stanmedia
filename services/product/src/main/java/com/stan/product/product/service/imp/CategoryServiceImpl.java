package com.stan.product.product.service.imp;

import com.stan.product.product.dto.request.CreateCategoryRequest;
import com.stan.product.product.dto.response.CategoryDto;
import com.stan.product.product.dto.response.DefaultResponse;
import com.stan.product.product.entity.Category;
import com.stan.product.product.enums.ResponseStatus;
import com.stan.product.product.exception.BadRequestException;
import com.stan.product.product.mapper.CategoryMapper;
import com.stan.product.product.repository.CategoryRepository;
import com.stan.product.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public DefaultResponse createCategory(CreateCategoryRequest request) {
        DefaultResponse<CategoryDto> response = new DefaultResponse<>();
        log.info("Inside Create Category with request: {}", request);
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new BadRequestException("Name cannot be empty");
        }
        Category category = categoryMapper.mapCreateCategoryRequestToCategory(request);
        try {
            Optional<Category> categoryCheck = categoryRepository.findByName(request.getName());
            if (categoryCheck.isPresent()) {
                throw new IllegalArgumentException("Category with name " + request.getName() + " already exists");
            }
            category = categoryRepository.save(category);
        } catch (Exception e) {
            e.printStackTrace();
        }
        CategoryDto categoryDto = categoryMapper.mapCategoryToCategoryDto(category);
        response.setStatus(ResponseStatus.SUCCESS.getCode());
        response.setMessage(ResponseStatus.SUCCESS.getMessage());
        response.setData(categoryDto);
        log.info("categoryDto...{}", categoryDto);
        return response;
    }
}
