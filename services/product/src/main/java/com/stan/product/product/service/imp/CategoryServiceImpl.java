package com.stan.product.product.service.imp;

import com.stan.data_storage.service.RedisUtility;
import com.stan.product.product.dto.request.CreateCategoryRequest;
import com.stan.product.product.dto.request.UpdateCategoryRequest;
import com.stan.product.product.dto.response.CategoryDto;
import com.stan.product.product.dto.response.DefaultResponse;
import com.stan.product.product.entity.Category;
import com.stan.product.product.enums.ResponseStatus;
import com.stan.product.product.exception.AlreadyExistException;
import com.stan.product.product.exception.BadRequestException;
import com.stan.product.product.exception.NotFoundException;
import com.stan.product.product.mapper.CategoryMapper;
import com.stan.product.product.repository.CategoryRepository;
import com.stan.product.product.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final RedisUtility redisUtility;

    @Override
    public DefaultResponse<CategoryDto> createCategory(CreateCategoryRequest request) {
        DefaultResponse<CategoryDto> response = new DefaultResponse<>();
        log.info("Inside Create Category with request: {}", request);
        if (request.getCode() == null || request.getCode().isEmpty()) {
            throw new BadRequestException("Code cannot be empty");
        }
        Optional<Category> categoryCheck = categoryRepository.findByCode(request.getCode());
        if (categoryCheck.isPresent()) {
            throw new AlreadyExistException(ResponseStatus.ALREADY_EXIST.getCode(), "Category with Code " + request.getCode() + " " + ResponseStatus.ALREADY_EXIST.getMessage());
        }
        Category category = categoryMapper.mapCreateCategoryRequestToCategory(request);
        category = categoryRepository.save(category);
        String CAT_KEY = "category_" + category.getCode();
        try {
            redisUtility.save(CAT_KEY, category, Duration.ofMillis(10000));
            log.info("Saved Category with key: {} to Redis successfully", CAT_KEY);
        } catch (Exception e) {
            log.info("Unable to saved Category with key: {} to Redis successfully", CAT_KEY);
            e.printStackTrace();
        }

        CategoryDto categoryDto = categoryMapper.mapCategoryToCategoryDto(category);
        response.setStatus(ResponseStatus.SUCCESS.getCode());
        response.setMessage(ResponseStatus.SUCCESS.getMessage());
        response.setData(categoryDto);
        log.info("createCategory response...{}", response);
        return response;
    }


    @Override
    public DefaultResponse<List<CategoryDto>> getAllCategory() {
        DefaultResponse<List<CategoryDto>> response = new DefaultResponse<>();
        log.info("Inside getAllCategory");

        List<Category> categories = categoryRepository.findAll();

        if (categories.isEmpty()) {
            response.setStatus(ResponseStatus.NOT_FOUND.getCode());
            response.setMessage("No categories found");
            response.setData(Collections.emptyList());
            return response;
        }

        List<CategoryDto> categoryList = categories.stream()
            .filter(Objects::nonNull)
            .map(cat -> {
                CategoryDto categoryDto = categoryMapper.mapCategoryToCategoryDto(cat);
                return categoryDto != null ? categoryDto : new CategoryDto();
            })
            .toList();

        response.setStatus(ResponseStatus.SUCCESS.getCode());
        response.setMessage(ResponseStatus.SUCCESS.getMessage());
        response.setData(categoryList);
        log.info("getAllCategory response...{}", response);
        return response;
    }

    @Override
    public DefaultResponse<CategoryDto> getCategoryByCode(String categoryCode) {
        log.info("Inside getCategoryByCode with request categoryCode: {}", categoryCode);
        CategoryDto categoryDto = null;
        DefaultResponse<CategoryDto> response = new DefaultResponse<>();
        log.info("Inside getCategoryByCode");

        try {
            String CAT_KEY = "category_" + categoryCode;
            Category savedCategory = redisUtility.get(CAT_KEY, Category.class);
            categoryDto = categoryMapper.mapCategoryToCategoryDto(savedCategory);
            response.setStatus(ResponseStatus.SUCCESS.getCode());
            response.setMessage(ResponseStatus.SUCCESS.getMessage());
            response.setData(categoryDto);
            log.info("getCategoryByCode response from Redis...{}", response);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }

        Optional<Category> category = categoryRepository.findByCode(categoryCode);

        if (category.isEmpty()) {
            response.setStatus(ResponseStatus.NOT_FOUND.getCode());
            response.setMessage("No categories found");
            return response;
        }
        categoryDto = categoryMapper.mapCategoryToCategoryDto(category.orElseThrow());
        response.setStatus(ResponseStatus.SUCCESS.getCode());
        response.setMessage(ResponseStatus.SUCCESS.getMessage());
        response.setData(categoryDto);
        log.info("getCategoryByCode response...{}", response);
        return response;
    }

    @Override
    public DefaultResponse<CategoryDto> updateCategoryByCode(String categoryCode, UpdateCategoryRequest request) {
        DefaultResponse<CategoryDto> response = new DefaultResponse<>();
        log.info("Inside updateCategoryByCode with request: {}", request);
        if (categoryCode == null || request == null) {
            throw new BadRequestException("request cannot be empty");
        }
        Optional<Category> categoryCheck = categoryRepository.findByCode(categoryCode);
        if (categoryCheck.isEmpty()) {
            throw new NotFoundException(ResponseStatus.NOT_FOUND.getCode(), "Category with name " + request.getName() + " " + ResponseStatus.NOT_FOUND.getMessage());
        }
        Category category = categoryMapper.mapUpdateCategoryRequestToCategory(request);
        category = categoryRepository.save(category);
        String CAT_KEY = "category_" + category.getCode();
        try {
            redisUtility.save(CAT_KEY, category, Duration.ofMillis(10000));
            log.info("updated Category with key: {} to Redis successfully", CAT_KEY);
        } catch (Exception e) {
            log.info("Unable to update Category with key: {} to Redis successfully", CAT_KEY);
            log.info(e.getMessage());
        }

        CategoryDto categoryDto = categoryMapper.mapCategoryToCategoryDto(category);
        response.setStatus(ResponseStatus.SUCCESS.getCode());
        response.setMessage(ResponseStatus.SUCCESS.getMessage());
        response.setData(categoryDto);
        log.info("updateCategoryByCode response...{}", response);
        return response;
    }
}
