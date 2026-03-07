package com.stan.customer.service;

import com.stan.customer.dto.request.CustomerRequest;
import com.stan.customer.dto.response.CustomerResponse;
import com.stan.customer.dto.response.DefaultResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

public interface CustomerService {
    ResponseEntity<String> createCustomer(@Valid CustomerRequest customerRequest);

    ResponseEntity<String> updateCustomer(String id, @Valid CustomerRequest customerRequest);

    DefaultResponse getCustomer();

    public DefaultResponse<CustomerResponse> getCustomerById(String id);

    DefaultResponse checkCustomerExistById(String id);
}
