package com.stan.gateway.dto.response;

import lombok.Data;

@Data
public class AuthenticationData {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
