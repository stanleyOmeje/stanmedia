package com.stan.data_storage.dto.response;

public record CustomerResponse(
    String firstName,
    String lastName,
    String email,
    Address address
) {
}
