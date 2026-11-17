package com.stan.account.service;

import com.stan.account.dto.request.CreateCustomerDto;
import com.stan.account.dto.response.CreateCustomerResponseDto;
import com.stan.account.dto.response.DefaultResponse;
import org.springframework.web.bind.annotation.RequestBody;

public interface CustomerService {
    DefaultResponse<CreateCustomerResponseDto> createCustomer(CreateCustomerDto request);
}
