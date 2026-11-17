package com.stan.account.service.httpService.httpServiceImpl;


import com.google.gson.Gson;
import com.stan.account.dto.request.CreateClientRequestDto;
import com.stan.account.dto.request.OpenAccountDto;
import com.stan.account.dto.request.ProfileDTO;
import com.stan.account.dto.response.CreateAccountApiResponseDto;
import com.stan.account.dto.response.CreateClientResponseDto;
import com.stan.account.dto.response.CreateCustomerResponseDto;
import com.stan.account.mapper.AccountMapper;
import com.stan.account.service.httpService.CoreBankingHttpService;
import com.stan.account.utils.CoreBankingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;


@Service
@Slf4j
@RequiredArgsConstructor
public class CoreBankingHttpServiceImpl implements CoreBankingHttpService {
    private final RestTemplate restTemplate;
    private final AccountMapper accountMapper;
    Gson gson = new Gson();
    public CreateClientResponseDto createClient(CreateClientRequestDto request, String fineractTenant){
        CreateClientResponseDto response = new CreateClientResponseDto();
        try {
            String url = "";
            HttpEntity<String> httpEntity = new HttpEntity<>(gson.toJson(request), getHeader(fineractTenant));
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            if(responseEntity.getStatusCode().is2xxSuccessful()){
                response.setClientId(CoreBankingUtil.generateId());
                response.setOfficeId(CoreBankingUtil.generateId());
                response.setSavingsId(CoreBankingUtil.generateId());
                response.setResourceId(CoreBankingUtil.generateId());
                return response;

            }
        }catch (Exception e){
            log.error(e.getMessage());
            response.setClientId(CoreBankingUtil.generateId());
            response.setOfficeId(CoreBankingUtil.generateId());
            response.setSavingsId(CoreBankingUtil.generateId());
            response.setResourceId(CoreBankingUtil.generateId());
            return response;
        }
        response.setClientId(CoreBankingUtil.generateId());
        response.setOfficeId(CoreBankingUtil.generateId());
        response.setSavingsId(CoreBankingUtil.generateId());
        response.setResourceId(CoreBankingUtil.generateId());
        return response;
    }

    @Override
    public ResponseEntity<CreateAccountApiResponseDto> createNubanAccount(ProfileDTO profileDTO, OpenAccountDto openAccountDto) {
        HashMap<String, Object> data = accountMapper.buildNubandAccountRequest(profileDTO, openAccountDto);
        String url = "";

        log.info("Sending create nuban account request to => {} with payload => {}", url, data);
        return restTemplate.postForEntity(
            url,
            data,
            CreateAccountApiResponseDto.class
        );
    }

    @Override
    public ResponseEntity<CreateAccountApiResponseDto> createReservedAccount(ProfileDTO profileDTO, OpenAccountDto openAccountDto) {
        HashMap<String, Object> data = accountMapper.buildReservedAccountRequest(profileDTO, openAccountDto);
        String url = "";
        log.info("Sending create reserved account request to => {} with payload => {}", url, data);
        return restTemplate.postForEntity(
            url,
            data,
            CreateAccountApiResponseDto.class
        );
    }

    private HttpHeaders getHeader(String fineractTenant) {
        HttpHeaders headers = new HttpHeaders();
       // headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
