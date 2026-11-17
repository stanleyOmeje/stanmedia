package com.stan.account.service;

import com.stan.account.dto.request.OpenAccountDto;
import com.stan.account.dto.response.CreateCustomerResponseDto;
import com.stan.account.dto.response.DefaultResponse;
import jakarta.validation.Valid;

public interface AccountService {
    DefaultResponse<CreateCustomerResponseDto> openAccount(@Valid OpenAccountDto request);
}
