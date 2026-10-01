package com.stan.gateway.service.http;

import com.stan.gateway.dto.response.AuthenticationResponse;
import com.stan.gateway.enums.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import com.google.gson.Gson;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileHttp {

    @Value("${app.config.authenticate-url}")
    private String authenticateUrl;

    private final RestTemplate restTemplate;
    Gson gson = new Gson();

    public AuthenticationResponse authenticate(String token) {
        AuthenticationResponse response = new AuthenticationResponse();
        try{
            log.info("token...{} and authenticateUrl...{}", token,authenticateUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            log.info("Inside ProfileHttp::authenticate with url ...{}", authenticateUrl);
            HttpEntity<String> httpEntity = new HttpEntity<>(headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(authenticateUrl, HttpMethod.GET, httpEntity, String.class );
            log.info("Raw response from authenticate is: {}", responseEntity);
            response = gson.fromJson(responseEntity.getBody(), AuthenticationResponse.class);
            log.info("authentication response: {}", response);
            return response;
        } catch (Exception e) {
            response.setStatus(ResponseStatus.FAILED.getCode());
            response.setMessage("Couldn't get authentication");
            return response;
        }

    }


}
