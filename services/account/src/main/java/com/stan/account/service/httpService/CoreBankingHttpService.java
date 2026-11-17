package com.stan.account.service.httpService;

import com.stan.account.dto.request.CreateClientRequestDto;
import com.stan.account.dto.request.OpenAccountDto;
import com.stan.account.dto.request.ProfileDTO;
import com.stan.account.dto.response.CreateAccountApiResponseDto;
import com.stan.account.dto.response.CreateClientResponseDto;
import com.stan.account.dto.response.CreateCustomerResponseDto;
import org.springframework.http.ResponseEntity;

public interface CoreBankingHttpService {
    public CreateClientResponseDto createClient(CreateClientRequestDto reuest, String fineractTenant);
    ResponseEntity<CreateAccountApiResponseDto> createNubanAccount(ProfileDTO profileDTO, OpenAccountDto openAccountDto);
    ResponseEntity<CreateAccountApiResponseDto> createReservedAccount(ProfileDTO profileDTO, OpenAccountDto openAccountDto);
}
