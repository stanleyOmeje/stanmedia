package com.stan.account.service.impl;

import com.stan.account.dto.request.OpenAccountDto;
import com.stan.account.dto.response.CreateCustomerResponseDto;
import com.stan.account.dto.response.DefaultResponse;
import com.stan.account.service.AccountService;
import org.springframework.stereotype.Service;


@Service
public class AccountServiceImpl implements AccountService {
    @Override
    public DefaultResponse<CreateCustomerResponseDto> openAccount(OpenAccountDto request) {
        return null;
    }
}
