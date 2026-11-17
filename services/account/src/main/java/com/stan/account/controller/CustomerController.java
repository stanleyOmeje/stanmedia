package com.stan.account.controller;

import com.stan.account.config.ApplicationUrl;
import com.stan.account.dto.request.CreateCustomerDto;
import com.stan.account.dto.response.CreateCustomerResponseDto;
import com.stan.account.dto.response.DefaultResponse;
import com.stan.account.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(ApplicationUrl.CUSTOMER_BASE_URL)
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<DefaultResponse<CreateCustomerResponseDto>> createCustomer(@RequestBody CreateCustomerDto request) {
        log.info("Inside Create customer with request: {}", request);
        DefaultResponse<CreateCustomerResponseDto> response = customerService.createCustomer(request);
        return ResponseEntity.ok(response);
    }
}
