package com.stan.customer.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Customer {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private Address address;
}
