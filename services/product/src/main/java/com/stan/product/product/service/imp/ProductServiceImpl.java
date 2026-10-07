package com.stan.product.product.service.imp;


import com.stan.product.product.dto.request.*;
import com.stan.product.product.dto.response.*;
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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
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
            feeMapping.setCreatedAt(new Date());
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
        GrandPurchaseResponse grandPurchaseResponse = new GrandPurchaseResponse();
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

        BigDecimal grandAmount = purchaseResponses.stream()
            .map(response -> response.getTotalPrice() == null
                ? BigDecimal.ZERO
                : response.getTotalPrice())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        log.info("grandAmount...{}", grandAmount);
        grandPurchaseResponse.setPurchaseResponses(purchaseResponses);
        grandPurchaseResponse.setGrandTotal(grandAmount);

        DefaultResponse<GrandPurchaseResponse> response = new DefaultResponse<>();
        response.setStatus(ResponseStatus.SUCCESS.getCode());
        response.setMessage(ResponseStatus.SUCCESS.getMessage());
        response.setData(grandPurchaseResponse);

        log.info("response...{}", response);

        return response;
    }

    private boolean checkTimeAvailability(Belt belt) {
        return true;
    }


    private void validateAmount(BigDecimal amount, Product product) {
        FeeMapping fee = product.getFee();
        if (fee == null) {
            throw new BadRequestException("Fee mapping not Present");
        }
        if (amount.compareTo(fee.getPrice()) != 0) {
            throw new BadRequestException("Amount not valid for " + product.getCode() + ". Valid amount is " + fee.getPrice());
        }
    }

//    @Override
//    public DefaultResponse createBulkProduct(MultipartFile file) throws IOException {
//        log.info("Inside ProductServiceImpl:: createBulkProduct");
//        DefaultResponse response = new DefaultResponse();
//        try {
//            List<CreateProductRequest> requests = buildBulkProduct(file);
//            if (requests == null || requests.isEmpty()) {
//                throw new BadRequestException("Request body cannot be empty");
//            }
//            BulkProductResponse bulkProductResponse = processBulkProduct(requests);
//            if (bulkProductResponse == null || bulkProductResponse.getSuccessCount() == 0) {
//                response.setStatus(ResponseStatus.FAILED.getCode());
//                response.setMessage(ResponseStatus.FAILED.getMessage());
//                log.info("createBulkProduct ...{}", response);
//                return response;
//            }
//            response.setStatus(ResponseStatus.SUCCESS.getCode());
//            response.setMessage(ResponseStatus.SUCCESS.getMessage());
//            response.setData(bulkProductResponse);
//            log.info("createBulkProduct ...{}", response);
//            return response;
//        }catch (Exception e) {
//            log.error(e.getMessage());
//            response.setStatus(ResponseStatus.BAD_REQUEST.getCode());
//            response.setMessage(e.getMessage());
//            return response;
//        }
//    }
//
//    public List<CreateProductRequest> buildBulkProduct(MultipartFile file) throws IOException {
//        log.info("Inside ProductServiceImpl:: buildBulkProduct");
//        List<CreateProductRequest> createProductRequestList = new ArrayList<>();
//        try (var parser = CSVFormat.DEFAULT
//            .builder()
//            .setHeader()
//            .setSkipHeaderRecord(true)
//            .setTrim(true)
//            .setAllowMissingColumnNames(true)
//            .build()
//            .parse(new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)))) {
//            for (var row : parser) {
//                try {
//                    String name = row.get("name");
//                    log.info("name...{}", name);
//                    String code = row.get("code");
//                    log.info("code...{}", code);
//                    String description = row.get("description");
//                    log.info("description...{}", description);
//                    String belt = row.get("belt");
//                    log.info("belt...{}", belt);
//                    String feeType = row.get("feeType");
//                    log.info("feeType...{}", feeType);
//                    BigDecimal price = new BigDecimal(row.get("price").replace(",", "").trim());
//                    log.info("price...{}", price);
//                    String categoryCode = row.get("categoryCode").trim();
//                    log.info("categoryCode...{}", categoryCode);
//
//                    CreateProductRequest createProductRequest = new CreateProductRequest();
//                    createProductRequest.setName(name);
//                    createProductRequest.setCode(code);
//                    createProductRequest.setDescription(description);
//                    createProductRequest.setBelt(Belt.valueOf(belt));
//                    createProductRequest.setFeeType(FeeType.valueOf(feeType));
//                    createProductRequest.setPrice(price);
//                    createProductRequest.setCategoryCode(categoryCode);
//
//                    createProductRequestList.add(createProductRequest);
//
//                } catch (Exception e) {
//                    log.warn("Errow occur building product ...{}", e.getMessage(), e);
//                   throw new BadRequestException(e.getMessage() + " row "+row.getRecordNumber() );
//                }
//            }
//        }
//        log.info("createProductRequestList ...{}", createProductRequestList);
//        return createProductRequestList;
//    }
//
//    public BulkProductResponse processBulkProduct(List<CreateProductRequest> requests) {
//        log.info("Inside ProductServiceImpl:: processBulkProduct");
//        List<ErrorRow> errors = new ArrayList<>();
//        int successCount = 0;
//        for (int i = 0; i < requests.size(); i++) {
//            try {
//                createProduct(requests.get(i));
//                successCount++;
//            } catch (Exception e) {
//                ErrorRow errorRow = new ErrorRow();
//                errorRow.setCode(requests.get(i).getCode());
//                errorRow.setRowNumber(i);
//                errorRow.setReason(e.getMessage());
//                errors.add(errorRow);
//            }
//        }
//        return BulkProductResponse.builder()
//            .rowCount(requests.size())
//            .successCount(successCount)
//            .failureCount(errors.size())
//            .errors(errors)
//            .build();
//    }

    @Override
    public DefaultResponse createBulkProduct(MultipartFile file) throws IOException {
        log.info("Inside ProductServiceImpl::createBulkProduct");

        DefaultResponse response = new DefaultResponse();

        try {
            if (file == null || file.isEmpty()) {
                throw new BadRequestException("CSV file cannot be null or empty");
            }

            List<CreateProductRequest> requests = buildBulkProduct(file);

            if (requests.isEmpty()) {
                throw new BadRequestException("CSV file contains no valid product records");
            }

            BulkProductResponse bulkProductResponse = processBulkProduct(requests);

            if (bulkProductResponse == null || bulkProductResponse.getSuccessCount() == 0) {
                response.setStatus(ResponseStatus.FAILED.getCode());
                response.setMessage(ResponseStatus.FAILED.getMessage());
                response.setData(bulkProductResponse);

                log.info("createBulkProduct failed: {}", response);
                return response;
            }

            response.setStatus(ResponseStatus.SUCCESS.getCode());
            response.setMessage(ResponseStatus.SUCCESS.getMessage());
            response.setData(bulkProductResponse);

            log.info(
                "createBulkProduct completed: total={}, success={}, failed={}",
                bulkProductResponse.getRowCount(),
                bulkProductResponse.getSuccessCount(),
                bulkProductResponse.getFailureCount()
            );

            return response;

        } catch (BadRequestException e) {
            log.warn("Bulk product request failed: {}", e.getMessage());

            response.setStatus(ResponseStatus.BAD_REQUEST.getCode());
            response.setMessage(
                e.getMessage() != null ? e.getMessage() : "Invalid bulk product request"
            );

            return response;

        } catch (Exception e) {
            log.error("Unexpected error while creating bulk products", e);

            response.setStatus(ResponseStatus.BAD_REQUEST.getCode());
            response.setMessage(
                e.getMessage() != null ? e.getMessage() : "Unable to process bulk product request"
            );

            return response;
        }
    }

    public List<CreateProductRequest> buildBulkProduct(MultipartFile file) throws IOException {
        log.info("Inside ProductServiceImpl::buildBulkProduct");

        if (file == null || file.isEmpty()) {
            throw new BadRequestException("CSV file cannot be null or empty");
        }

        List<CreateProductRequest> requests = new ArrayList<>();

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
            .setHeader()
            .setSkipHeaderRecord(true)
            .setTrim(true)
            .setAllowMissingColumnNames(false)
            .build();

        try (
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                    file.getInputStream(),
                    StandardCharsets.UTF_8
                )
            );
            CSVParser parser = csvFormat.parse(reader)
        ) {

            for (CSVRecord row : parser) {
                try {
                    CreateProductRequest request = buildProductRequest(row);
                    requests.add(request);

                } catch (Exception e) {
                    log.warn(
                        "Error building product at CSV row {}: {}",
                        row.getRecordNumber(),
                        e.getMessage(),
                        e
                    );

                    throw new BadRequestException(
                        "Invalid product at row "
                            + row.getRecordNumber()
                            + ": "
                            + safeMessage(e)
                    );
                }
            }
        }

        log.info("Successfully built {} product requests", requests.size());

        return requests;
    }

    private CreateProductRequest buildProductRequest(CSVRecord row) {

        String name = getRequiredValue(row, "name");
        String code = getRequiredValue(row, "code");
        String description = getOptionalValue(row, "description");
        String beltValue = getRequiredValue(row, "belt");
        String feeTypeValue = getRequiredValue(row, "feeType");
        String priceValue = getRequiredValue(row, "price");
        String categoryCode = getRequiredValue(row, "categoryCode");

        BigDecimal price = parsePrice(priceValue, "price");

        Belt belt = parseBelt(beltValue);
        FeeType feeType = parseFeeType(feeTypeValue);

        CreateProductRequest request = new CreateProductRequest();

        request.setName(name);
        request.setCode(code);
        request.setDescription(description);
        request.setBelt(belt);
        request.setFeeType(feeType);
        request.setPrice(price);
        request.setCategoryCode(categoryCode);

        return request;
    }

    private String getRequiredValue(CSVRecord row, String columnName) {

        if (!row.isMapped(columnName)) {
            throw new BadRequestException(
                "Missing required CSV column: " + columnName
            );
        }

        String value = row.get(columnName);

        if (value == null || value.isBlank()) {
            throw new BadRequestException(
                "Column '" + columnName + "' cannot be empty"
            );
        }

        return value.trim();
    }

    private String getOptionalValue(CSVRecord row, String columnName) {

        if (!row.isMapped(columnName)) {
            return null;
        }

        String value = row.get(columnName);

        return value == null || value.isBlank()
            ? null
            : value.trim();
    }

    private BigDecimal parsePrice(String value, String fieldName) {

        try {
            return new BigDecimal(value.replace(",", "").trim());
        } catch (NumberFormatException e) {
            throw new BadRequestException(
                "Invalid " + fieldName + " value: " + value
            );
        }
    }

    private Belt parseBelt(String value) {

        try {
            return Belt.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(
                "Invalid belt value: " + value
            );
        }
    }

    private FeeType parseFeeType(String value) {

        try {
            return FeeType.valueOf(value.trim());
//            return FeeType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(
                "Invalid feeType value: " + value
            );
        }
    }

    public BulkProductResponse processBulkProduct(
        List<CreateProductRequest> requests
    ) {
        log.info("Inside ProductServiceImpl::processBulkProduct");

        if (requests == null || requests.isEmpty()) {
            return BulkProductResponse.builder()
                .rowCount(0)
                .successCount(0)
                .failureCount(0)
                .errors(Collections.emptyList())
                .build();
        }

        List<ErrorRow> errors = new ArrayList<>();
        int successCount = 0;

        for (int i = 0; i < requests.size(); i++) {

            CreateProductRequest request = requests.get(i);

            if (request == null) {
                ErrorRow errorRow = new ErrorRow();
                errorRow.setRowNumber(i + 1);
                errorRow.setReason("Product request cannot be null");

                errors.add(errorRow);
                continue;
            }

            try {
                createProduct(request);
                successCount++;

            } catch (Exception e) {

                ErrorRow errorRow = new ErrorRow();
                errorRow.setCode(request.getCode());
                errorRow.setRowNumber(i + 1);
                errorRow.setReason(safeMessage(e));

                errors.add(errorRow);

                log.warn(
                    "Failed to create product at row {}. code={}, reason={}",
                    i + 1,
                    request.getCode(),
                    e.getMessage()
                );
            }
        }

        return BulkProductResponse.builder()
            .rowCount(requests.size())
            .successCount(successCount)
            .failureCount(errors.size())
            .errors(errors)
            .build();
    }

    private String safeMessage(Exception e) {
        return e.getMessage() != null
            ? e.getMessage()
            : e.getClass().getSimpleName();
    }
}


