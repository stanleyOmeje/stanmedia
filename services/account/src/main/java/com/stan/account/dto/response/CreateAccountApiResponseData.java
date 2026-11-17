package com.stan.account.dto.response;

import lombok.Data;

@Data
public class CreateAccountApiResponseData {

    private String accountNumber;

    private String bankName;

    private String accountName;

    private String bankCode;
}
