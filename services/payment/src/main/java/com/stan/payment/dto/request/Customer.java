package com.stan.payment.dto.request;

import org.springframework.validation.annotation.Validated;

@Validated
public record Customer(
    String firstName,
    String lastName,
    String email
) {
}
