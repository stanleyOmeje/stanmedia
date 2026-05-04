package com.stan.order.customerHelper;

public record CustomerResponse(
    String id,
    String firstName,
    String lastName,
    String email
) {
}
