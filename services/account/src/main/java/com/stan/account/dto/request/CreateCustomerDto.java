package com.stan.account.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@Data
public class CreateCustomerDto {

    private String firstName;

    private String lastName;

    private String fullName;

    private String otherName = StringUtils.EMPTY;

    private String alias;

    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date dateOfBirth;

    private String bvn;

    private String gender;

    private String clientType;

    @NotNull
    private String phone;

    @NotNull
    private String phoneCountryCode;

    private String fineractTenant;

    private String city;
    private String state;
}
