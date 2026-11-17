package com.stan.account.dto.response;

import lombok.Data;

@Data
public class OpenAccountResponseDto {

    private String accountName;

    private String accountNumber;

    private String bankCode;

    private String currency;

    private String accountOpeningDate;
    private String bankName;
}
