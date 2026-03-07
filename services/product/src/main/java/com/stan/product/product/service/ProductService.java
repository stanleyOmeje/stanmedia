package com.stan.product.product.service;



import com.stan.product.product.dto.request.CreateProductRequest;
import com.stan.product.product.dto.request.ProductSearchCriteria;
import com.stan.product.product.dto.request.PurchaseRequest;
import com.stan.product.product.dto.request.UpdateProductRequest;
import com.stan.product.product.dto.response.DefaultResponse;

import java.util.List;

public interface ProductService {
    DefaultResponse createProduct(CreateProductRequest request);
    DefaultResponse fetchAllProductWithFilter(int page, int pageSize,
                                                              ProductSearchCriteria productSearchCriteria);
    DefaultResponse updateProduct(String code, UpdateProductRequest request);

    DefaultResponse makePurchase(List<PurchaseRequest> request);

}
