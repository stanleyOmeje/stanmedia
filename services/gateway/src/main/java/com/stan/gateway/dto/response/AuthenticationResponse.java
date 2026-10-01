package com.stan.gateway.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticationResponse {
    private String status;
    private String message;
    public AuthenticationData data;

}
