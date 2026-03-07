package com.stan.customer.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Setter
@Getter
@Validated
public class Address {
    private String street;
    private String houseNumber;
    private String zipCode;
}

