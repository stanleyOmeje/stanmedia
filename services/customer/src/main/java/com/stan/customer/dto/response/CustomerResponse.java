package com.stan.customer.dto.response;

import com.stan.customer.domain.Address;

public record CustomerResponse(
    String firstName,
    String lastName,
    String email,
    Address address
) {
}
