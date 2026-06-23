package com.stan.profile.service;

import com.stan.profile.dto.request.LoginRequest;
import com.stan.profile.dto.request.RegisterRequest;
import com.stan.profile.dto.response.DefaultResponse;
import com.stan.profile.dto.response.LoginResponse;
import com.stan.profile.dto.response.RegisterResponse;

public interface AuthService {
    DefaultResponse<RegisterResponse> register(RegisterRequest request);

    DefaultResponse<LoginResponse> login(LoginRequest request);
}
