package com.stan.profile.controller;

import com.stan.profile.dto.request.LoginRequest;
import com.stan.profile.dto.request.RegisterRequest;
import com.stan.profile.dto.response.DefaultResponse;
import com.stan.profile.dto.response.LoginResponse;
import com.stan.profile.dto.response.RegisterResponse;
import com.stan.profile.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<DefaultResponse<RegisterResponse>> register(@RequestBody RegisterRequest request) {
        log.info("Inside AuthController::Register with request: {}", request);
        DefaultResponse<RegisterResponse> response =
            authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<DefaultResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        log.info("Inside AuthController::login with request: {}", request);
        DefaultResponse<LoginResponse> response =
            authService.login(request);
        return ResponseEntity.ok(response);
    }
}
