package com.stan.profile.dto.request;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String token;
    private String username;
    private String password;
}
