package com.stan.account.controller;

import com.stan.account.config.ApplicationUrl;
import com.stan.account.dto.request.CreateCustomerDto;
import com.stan.account.dto.request.OpenAccountDto;
import com.stan.account.dto.response.CreateCustomerResponseDto;
import com.stan.account.dto.response.DefaultResponse;
import com.stan.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(ApplicationUrl.ACCOUNT_BASE_URL)
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<DefaultResponse<CreateCustomerResponseDto>> openAccount(@Valid @RequestBody OpenAccountDto request) {
        log.info("Inside Open Account with request: {}", request);
        DefaultResponse<CreateCustomerResponseDto> response = accountService.openAccount(request);
        return ResponseEntity.ok(response);
    }

}
