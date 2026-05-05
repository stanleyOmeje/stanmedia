package com.stan.payment.service.http;

import com.stan.payment.dto.orderdto.response.OrderResponse;
import com.stan.payment.dto.response.DefaultResponse;
import com.stan.payment.enums.PaymentMethod;
import com.stan.payment.enums.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@Service
@Component
public class OrderHttpService {

    @Value("${app.config.order-url}")
    private String orderUrl;

    private final RestTemplate restTemplate;

    public DefaultResponse<OrderResponse> getOrderByReference(String reference) {
        DefaultResponse<OrderResponse> defaultResponse = new DefaultResponse<>();
        try{
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        String url = String.format("%s/%s/%s", orderUrl, "order-reference", reference);
        log.info("Inside OrderController::getOrder getting order with url ...{}", url);
        ParameterizedTypeReference<DefaultResponse<OrderResponse>> responseType = new ParameterizedTypeReference<DefaultResponse<OrderResponse>>() {};
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<DefaultResponse<OrderResponse>> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, responseType );
        defaultResponse = response.getBody();
        log.info("getOrder response: {}", defaultResponse);
        return defaultResponse;
        } catch (Exception e) {
            defaultResponse.setStatus(ResponseStatus.FAILED.getCode());
            defaultResponse.setMessage("Couldn't get order");
           return defaultResponse;
        }

    }
}
