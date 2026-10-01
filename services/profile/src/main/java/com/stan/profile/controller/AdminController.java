package com.stan.profile.controller;


import com.stan.profile.dto.request.AuthenticationRequest;
import com.stan.profile.dto.request.RegisterRequest;
import com.stan.profile.dto.response.DefaultResponse;
import com.stan.profile.dto.response.RegisterResponse;
import com.stan.profile.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/profile")
@RestController
public class AdminController {

    private final UserProfileService userProfileService;

    @PostMapping("/authenticate")
    public ResponseEntity<DefaultResponse<RegisterResponse>> authenticateUser(@RequestBody AuthenticationRequest request) {
        log.info("Inside AdminController::authenticateUser with request: {}", request);
        DefaultResponse<RegisterResponse> response =
            userProfileService.authenticateUser(request);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/authenticate")
    public ResponseEntity<DefaultResponse<RegisterResponse>> authenticateUserWithoutRequest() {
        log.info("Inside AdminController::authenticateUser ");
        DefaultResponse<RegisterResponse> response =
            userProfileService.authenticateUser();
        return ResponseEntity.ok(response);
    }
}
