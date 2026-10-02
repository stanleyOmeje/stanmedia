package com.stan.gateway.service;

import com.stan.gateway.dto.request.CreateProfileRequest;
import com.stan.gateway.dto.response.AuthenticationData;
import com.stan.gateway.dto.response.CreateProfileResponse;

public interface ProfileService {
    CreateProfileResponse createProfile(CreateProfileRequest createProfileRequest,AuthenticationData authenticationData);
    boolean isExistingProfile(String email);
}
