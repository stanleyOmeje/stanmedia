package com.stan.profile.service;

import com.stan.profile.dto.request.AuthenticationRequest;
import com.stan.profile.dto.response.DefaultResponse;
import com.stan.profile.dto.response.RegisterResponse;

public interface UserProfileService {
    DefaultResponse<RegisterResponse> authenticateUser(AuthenticationRequest request);

    DefaultResponse<RegisterResponse> authenticateUser();
}
