package com.stan.product.product.service.imp;


import com.stan.product.product.dto.request.*;
import com.stan.product.product.dto.response.DefaultResponse;
import com.stan.product.product.dto.response.PurchaseResponse;
import com.stan.product.product.entity.Category;
import com.stan.product.product.entity.FeeMapping;
import com.stan.product.product.entity.Product;
import com.stan.product.product.enums.Belt;
import com.stan.product.product.enums.FeeType;
import com.stan.product.product.enums.ProductType;
import com.stan.product.product.enums.ResponseStatus;
import com.stan.product.product.exception.BadRequestException;
import com.stan.product.product.exception.NotFoundException;
import com.stan.product.product.mapper.ProductMapper;
import com.stan.product.product.repository.CategoryRepository;
import com.stan.product.product.repository.FeeMappingRepository;
import com.stan.product.product.repository.ProductRepository;
import com.stan.product.product.service.ProductQueryService;
import com.stan.product.product.service.ProductService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final FeeMappingRepository feeMappingRepository;
    private final ProductQueryService productQueryService;

    @Override
    public DefaultResponse<?> createProduct(CreateProductRequest request) {
        log.info("Creating product with name " + request.getName());
        DefaultResponse<Product> response = new DefaultResponse<>();
        String categoryCode = request.getCategoryCode();
        String productCode = request.getCode();
        if (StringUtils.isEmpty(categoryCode) || StringUtils.isEmpty(productCode)) {
            throw new BadRequestException(ResponseStatus.BAD_REQUEST.getCode(),
                ResponseStatus.BAD_REQUEST.getMessage());
        }
        Optional<Category> category = categoryRepository.findByCode(categoryCode);
        if (category.isEmpty()) {
            throw new NotFoundException("Category not found");
        }
        Optional<Product> productCheck = productRepository.findByCode(request.getCode());
        if (productCheck.isPresent()) {
            throw new IllegalArgumentException("Product with code " + productCode + " already exists");
        }
        Product product = productMapper.mapCreateProductRequestToProduct(request, category.get());
        try {
            product = productRepository.save(product);
            FeeMapping feeMapping = createFeeMapping(request, product);
            product.setFee(feeMapping);
            response.setStatus(ResponseStatus.CREATED.getCode());
            response.setMessage(ResponseStatus.CREATED.getMessage());
            response.setData(product);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return response;
    }

    public FeeMapping createFeeMapping(CreateProductRequest request, Product product) {
        log.info("Creating createFeeMapping with name ");
        try {
            FeeMapping feeMapping = new FeeMapping();
            feeMapping.setFeeType(request.getFeeType());
            feeMapping.setPrice(request.getPrice());
            feeMapping.setProduct(product);
            feeMapping = feeMappingRepository.save(feeMapping);
            return feeMapping;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public DefaultResponse<?> fetchAllProductWithFilter(int page, int pageSize, ProductSearchCriteria productSearchCriteria) {
        log.info("Inside ProductServiceImpl::fetchAllProductWithFilter");
        ProductPage productPage = new ProductPage();
        productPage.setPageNo(page);
        productPage.setPageSize(pageSize);

        Page<Product> products = productQueryService.getAllProductWithFilter(productPage, productSearchCriteria);
        DefaultResponse<Map<String, Object>> defaultResponse = new DefaultResponse<>();

        if (productPage.getPageSize() > 50) {
            defaultResponse.setMessage("Maximum page size exceeded");
            defaultResponse.setStatus(ResponseStatus.FAILED.getCode());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("totalPage", products.getTotalPages());
        map.put("totalContent", products.getTotalElements());
        map.put("items", products.getContent());
        defaultResponse.setStatus(ResponseStatus.SUCCESS.getCode());
        defaultResponse.setMessage(ResponseStatus.SUCCESS.getMessage());
        defaultResponse.setData(map);

        log.info("Returning product list...{}", defaultResponse);
        return defaultResponse;
    }

    @Override
    public DefaultResponse<?> updateProduct(String code, UpdateProductRequest request) {
        log.info("Inside ProductServiceImpl::updateProduct with code: {}", code);

        DefaultResponse<Product> response = new DefaultResponse<>();

        try {
            // Null check for request
            if (request == null) {
                throw new BadRequestException("Request body cannot be null");
            }

            // Validate product and category code
            String categoryCode = request.getCategoryCode();
            if (StringUtils.isEmpty(categoryCode) || StringUtils.isEmpty(code)) {
                throw new BadRequestException("Product code and category code are mandatory");
            }

            // Fetch product
            Product product = productRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Product not found"));

            // Fetch category
            Category category = categoryRepository.findByCode(request.getCategoryCode())
                .orElseThrow(() -> new NotFoundException("Category not found"));

            // Update fields (only if request values are not null)
            if (request.getName() != null) product.setName(request.getName());
            if (request.getDescription() != null) product.setDescription(request.getDescription());
            if (request.getBelt() != null) product.setBelt(request.getBelt());

            product.setUpdatedAt(new Date());

            product.setCategory(category);

            // Save product
            Product savedProduct = productRepository.save(product);

            FeeMapping feeMapping = updateFeeMapping(request, product);
            savedProduct.setFee(feeMapping);

            response.setStatus(ResponseStatus.SUCCESS.getCode());
            response.setMessage(ResponseStatus.SUCCESS.getMessage());
            response.setData(savedProduct);
            return response;

        } catch (BadRequestException | NotFoundException e) {
            log.error("Validation error: {}", e.getMessage(), e);
            response.setStatus(ResponseStatus.FAILED.getCode());
            response.setMessage(e.getMessage());
            return response;

        } catch (Exception e) {
            log.error("Unexpected error occurred while updating product", e);
            response.setStatus(ResponseStatus.FAILED.getCode());
            response.setMessage(ResponseStatus.FAILED.getMessage());
            return response;
        }
    }

    public FeeMapping updateFeeMapping(UpdateProductRequest request, Product product) {
        log.info("updating FeeMapping with name ");
        try {
            Optional<FeeMapping> feeMappingCheck = feeMappingRepository.findOneByProduct(product);
            if (feeMappingCheck.isEmpty()) {
                throw new NotFoundException("FeeMapping not found");
            }
            FeeMapping feeMapping = feeMappingCheck.get();
            feeMapping.setFeeType(request.getFeeType());
            feeMapping.setPrice(request.getPrice());
            feeMapping.setProduct(product);
            feeMapping = feeMappingRepository.save(feeMapping);
            return feeMapping;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public DefaultResponse<?> makePurchase(List<PurchaseRequest> request) {
        log.info("Inside ProductServiceImpl::makePurchase with request: {}", request);
        if (request == null || request.isEmpty()) {
            throw new BadRequestException("Request body cannot be empty");
        }
        List<PurchaseResponse> purchaseResponses = new ArrayList<>();
        for (PurchaseRequest purchaseRequest : request) {
            String productCode = purchaseRequest.getProductCode();
            Product product = productRepository.findByCode(productCode)
                .orElseThrow(() -> new BadRequestException("Invalid product code: " + productCode));
            if (product.getFee() != null) {
                if (!FeeType.Dynamic.equals(product.getFee().getFeeType())) {
                    validateAmount(purchaseRequest.getAmount(), product);
                }
            }
            Category category = product.getCategory();
            if (category != null &&
                !ProductType.JINGLE.name().equalsIgnoreCase(category.getCode())) {
                boolean timeAvailable = checkTimeAvailability(product.getBelt());
                if (!timeAvailable) {
                    throw new BadRequestException("Product belt is not available");
                }
            }
            PurchaseResponse purchaseResponse =
                productMapper.mapProductToPurchaseResponse(product, purchaseRequest.getQuantity(), purchaseRequest.getAmount());

            purchaseResponses.add(purchaseResponse);
        }

        DefaultResponse<List<PurchaseResponse>> response = new DefaultResponse<>();
        response.setStatus(ResponseStatus.SUCCESS.getCode());
        response.setMessage(ResponseStatus.SUCCESS.getMessage());
        response.setData(purchaseResponses);

        log.info("response...{}", response);

        return response;
    }

    private boolean checkTimeAvailability(Belt belt) {
        return true;
    }


    private void validateAmount(BigDecimal amount, Product product) {
        FeeMapping fee = product.getFee();
        if (fee == null) {
            throw new BadRequestException("Fee mapping not ");
        }
        if (amount.compareTo(fee.getPrice()) != 0) {
            throw new BadRequestException("Amount not valid");
        }
    }



}
