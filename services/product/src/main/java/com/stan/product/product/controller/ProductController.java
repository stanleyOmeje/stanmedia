package com.stan.product.product.controller;


import com.stan.product.product.config.ApplicationUrl;
import com.stan.product.product.dto.request.CreateProductRequest;
import com.stan.product.product.dto.request.ProductSearchCriteria;
import com.stan.product.product.dto.request.PurchaseRequest;
import com.stan.product.product.dto.request.UpdateProductRequest;
import com.stan.product.product.dto.response.DefaultResponse;
import com.stan.product.product.enums.Belt;
import com.stan.product.product.enums.FeeType;
import com.stan.product.product.enums.ProductType;
import com.stan.product.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RequestMapping(ApplicationUrl.PRODUCT_BASE_URL)
@RestController
public class ProductController {
    private final ProductService productService;
    @PostMapping
    public ResponseEntity<DefaultResponse> createProduct(@RequestBody CreateProductRequest request){
        log.info("Creating product with name " + request.getName());
        DefaultResponse response = new DefaultResponse();
        try{
            response = productService.createProduct(request);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{code}")
    public ResponseEntity<DefaultResponse> updateProduct(@PathVariable String code, @RequestBody UpdateProductRequest request){
        log.info("Creating product with name " + request.getName());
        DefaultResponse response = new DefaultResponse();
        response = productService.updateProduct(code, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<DefaultResponse> fetchAllProduct(
        @RequestParam(required = false, defaultValue = "0", value = "page") int page,
        @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize,
        @RequestParam(value = "code", required = false) String code,
        @RequestParam(value = "description", required = false) String description,
        @RequestParam(value = "productType", required = false) ProductType productType,
        @RequestParam(required = false, value = "categoryCode") String categoryCode,
        @RequestParam(required = false, value = "belt") Belt belt,
        @RequestParam(required = false, value = "price") BigDecimal price,
        @RequestParam(required = false, value = "feeType") FeeType feeType

    ) {
        log.info("Fetching all products with filter");
        ProductSearchCriteria pSearchCriteria = new ProductSearchCriteria();
        pSearchCriteria.setCode(code);
        pSearchCriteria.setDescription(description);
        pSearchCriteria.setProductType(productType);
        pSearchCriteria.setCategoryCode(categoryCode);
        pSearchCriteria.setBelt(belt);
        pSearchCriteria.setPrice(price);
        pSearchCriteria.setFeeType(feeType);

        return ResponseEntity.ok(productService.fetchAllProductWithFilter(
            page, pageSize, pSearchCriteria));
    }

    @PostMapping("/purchase")
    public ResponseEntity<DefaultResponse> purchaseProduct(@RequestBody List<PurchaseRequest> request){
        log.info("purchasing product with request " + request);
        DefaultResponse response = new DefaultResponse();
        response = productService.makePurchase(request);
        return ResponseEntity.ok(response);
   }
}
