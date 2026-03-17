package com.stan.customer.service;

import com.stan.customer.dto.request.CustomerRequest;
import com.stan.customer.dto.response.CustomerResponse;
import com.stan.customer.dto.response.DefaultResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

public interface CustomerService {
    DefaultResponse createCustomer(@Valid CustomerRequest customerRequest);

    DefaultResponse  updateCustomer(String email, @Valid CustomerRequest customerRequest);

    DefaultResponse getCustomer();

    public DefaultResponse<CustomerResponse> getCustomerById(long id);

    DefaultResponse checkCustomerExistById(long id);
}
