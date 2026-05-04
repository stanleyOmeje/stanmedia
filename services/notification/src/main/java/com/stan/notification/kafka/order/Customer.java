package com.stan.notification.kafka.order;

public record Customer(
    long id,
    String firstName,
    String lastName,
    String email
) {
}
