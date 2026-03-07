package com.stan.product.product.mapper;


import com.stan.product.product.dto.request.CreateProductRequest;
import com.stan.product.product.dto.response.ProductDto;
import com.stan.product.product.dto.response.PurchaseResponse;
import com.stan.product.product.entity.Category;
import com.stan.product.product.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public ProductDto mapProductToProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getFee().getPrice());
        productDto.setCategoryName(product.getCategory().getName());
        productDto.setId(product.getId());
        return productDto;
    }

    public Product mapCreateProductRequestToProduct(CreateProductRequest request, Category category) {
        Product product = new Product();
        product.setName(request.getName());
        product.setCode(request.getCode());
        product.setDescription(request.getDescription());
        product.setAvailableQuantity(request.getAvailableQuantity());
        product.setProductType(request.getProductType());
        product.setBelt(request.getBelt());
        product.setCategory(category);
        return product;

    }

    public PurchaseResponse mapProductToPurchaseResponse(Product product) {
        PurchaseResponse purchaseResponse = new PurchaseResponse();
        purchaseResponse.setName(product.getName());
        purchaseResponse.setDescription(product.getDescription());
        purchaseResponse.setPrice(product.getFee().getPrice());
        purchaseResponse.setCategoryName(product.getCategory().getName());

      return purchaseResponse;
    }
}
