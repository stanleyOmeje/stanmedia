package com.stan.order.productHelper;

import com.stan.order.dto.response.DefaultResponse;
import com.stan.order.dto.request.PurchaseRequest;
import com.stan.order.exceptions.NotFoundException;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductHttp {
    @Value("${app.config.product-url}")
    private String url;
    private final RestTemplate restTemplate;

    public DefaultResponse<List<PurchaseResponse>> purchaseProduct(List<PurchaseRequest> request) {

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<List<PurchaseRequest>> requestEntity = new HttpEntity<>(request, headers);
        String serviceUrl = String.format("%s/%s", url,"purchase");

        ParameterizedTypeReference<DefaultResponse<List<PurchaseResponse>>> responseType =
            new ParameterizedTypeReference<>() {
            };
        ResponseEntity<DefaultResponse<List<PurchaseResponse>>> response =
            restTemplate.exchange(serviceUrl, HttpMethod.POST, requestEntity, responseType);
        if (response.getStatusCode().isError()) {
            throw new NotFoundException("01", "Could not make purchase");
        }
        return response.getBody();
    }

}
