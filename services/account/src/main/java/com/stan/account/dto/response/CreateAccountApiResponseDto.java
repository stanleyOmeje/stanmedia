package com.stan.account.dto.response;

import lombok.Data;

@Data
public class CreateAccountApiResponseDto {

    private CreateAccountApiResponseData data;

    private String message;

    private String status;
}
