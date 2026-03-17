package com.stan.customer.dto.request;

import com.stan.customer.domain.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest(
    @NotBlank (message = "Customer first name is required")
    String firstName,
    @NotNull (message = "Customer last name is required")
    String lastName,
    @Email(message = "Email not valid")
    String email,
    String street,
    String houseNumber,
    String zipCode
) {
}
