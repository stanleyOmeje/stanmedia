package com.stan.account.dto.request;

import lombok.Data;

@Data
public class CreateFieldDto {

    private String fieldValue;
    private String fieldName;
    private String clientId;
    private String savingsId;
    private String fineractTenant;
}
