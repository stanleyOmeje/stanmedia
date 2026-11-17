package com.stan.account.dto.request;

import lombok.Data;

@Data
public class CreateClientRequestDto {

    private String firstname;

    private String lastname;

    private String middlename;

    private String officeId;

    private String dateFormat;

    private String externalId;

    private Integer clientTypeId;

    private Integer genderId;

    private Integer savingsProductId;

    private String active;

    private String dateOfBirth;

    private String mobileNo;

    private String locale;

    private String activationDate;
    private String legalFormId;
}
