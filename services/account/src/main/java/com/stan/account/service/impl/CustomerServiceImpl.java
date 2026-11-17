package com.stan.account.service.impl;

import com.stan.account.dto.request.CreateClientRequestDto;
import com.stan.account.dto.request.CreateCustomerDto;
import com.stan.account.dto.response.CreateClientResponseDto;
import com.stan.account.dto.response.CreateCustomerResponseDto;
import com.stan.account.dto.response.DefaultResponse;
import com.stan.account.entity.Profile;
import com.stan.account.enums.ResponseStatus;
import com.stan.account.factory.CoreBankingFactory;
import com.stan.account.mapper.CustomerMapper;
import com.stan.account.repository.ProfileRepository;
import com.stan.account.service.CustomerService;
import com.stan.account.service.httpService.CoreBankingHttpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {
    private final ProfileRepository profileRepository;
    private final CoreBankingHttpService coreBankingHttpService;
    private final CustomerMapper customerMapper;
    private final CoreBankingFactory coreBankingFactory;

    @Override
    public DefaultResponse<CreateCustomerResponseDto> createCustomer(CreateCustomerDto request) {
        log.info("Inside Create customer with request: {}", request);
        DefaultResponse<CreateCustomerResponseDto> response = new DefaultResponse<>();
        CreateCustomerResponseDto createCustomerResponseDto = new CreateCustomerResponseDto();
        response.setStatus(ResponseStatus.FAILED.getCode());
        response.setMessage(ResponseStatus.FAILED.getMessage());

        boolean existsCustomer = profileRepository.existsByEmail(request.getEmail());
        if (existsCustomer) {
            response.setStatus(ResponseStatus.ALREADY_EXIST.getCode());
            response.setMessage("Customer " + ResponseStatus.ALREADY_EXIST.getMessage());
            return response;
        }
        try {
            CreateClientRequestDto createClientRequestDto = coreBankingFactory.buildCreateClientRequest(request, "");
            CreateClientResponseDto createClientResponseDto = coreBankingHttpService.createClient(createClientRequestDto, "");
            if (createClientResponseDto != null) {
                Profile profile = createProfile(request);
                createCustomerResponseDto.setProfileId(String.valueOf(profile.getId()));
                response.setStatus(ResponseStatus.CREATED.getCode());
                response.setMessage(ResponseStatus.CREATED.getMessage());
                response.setData(createCustomerResponseDto);
                return response;
            }
        } catch (Exception e) {
            log.error("Error creating profile ...{}", e.getMessage());
        }
        return response;
    }

    private Profile createProfile(CreateCustomerDto request) {
        Profile profile = customerMapper.mapCustomerToProfile(request);
        profileRepository.save(profile);
        return profile;
    }
}
