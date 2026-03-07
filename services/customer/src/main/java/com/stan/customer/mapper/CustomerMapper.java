package com.stan.customer.mapper;

import com.stan.customer.domain.Customer;
import com.stan.customer.dto.request.CustomerRequest;
import com.stan.customer.dto.response.CustomerResponse;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer mapCustomerrequestToCustomer(CustomerRequest request) {
        Customer customer = new Customer();
        customer.setFirstName(request.firstName());
        customer.setLastName(request.lastName());
        customer.setEmail(request.email());
        customer.setAddress(request.address());
        return customer;
    }

    public CustomerResponse mapCustomerToCustomerResponse(Customer customer) {
        return new CustomerResponse(
            customer.getFirstName(),
            customer.getLastName(),
            customer.getEmail(),
            customer.getAddress()
        );
    }
}
