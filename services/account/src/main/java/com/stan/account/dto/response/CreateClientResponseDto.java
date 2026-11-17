package com.stan.account.dto.response;

import lombok.Data;

@Data
public class CreateClientResponseDto {

    private Long officeId;

    private Long clientId;

    private Long savingsId;

    private Long resourceId;
}
