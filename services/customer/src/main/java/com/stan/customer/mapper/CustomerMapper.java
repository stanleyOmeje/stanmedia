package com.stan.customer.mapper;

import com.stan.customer.dto.request.CustomerRequest;
import com.stan.customer.dto.response.CustomerResponse;
import com.stan.customer.entity.Address;
import com.stan.customer.entity.Customer;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CustomerMapper {

    public Customer mapCustomerCreaterequestToCustomer(CustomerRequest request, Address address) {
        Customer customer = new Customer();
        customer.setFirstName(request.firstName());
        customer.setLastName(request.lastName());
        customer.setEmail(request.email());
        customer.setAddress(address);
        return customer;
    }

    public Customer mapCustomerRequestToCustomer(CustomerRequest request) {
        Customer customer = new Customer();
        customer.setFirstName(request.firstName());
        customer.setLastName(request.lastName());
        customer.setEmail(request.email());
        customer.setCreatedAt(new Date());
        return customer;
    }

    public CustomerResponse mapCustomerToCustomerResponse(Customer customer) {
        com.stan.customer.domain.Address addres = new com.stan.customer.domain.Address();
        addres.setHouseNumber(customer.getAddress().getHouseNumber());
        addres.setStreet(customer.getAddress().getStreet());
        addres.setZipCode(customer.getAddress().getZipCode());
        return new CustomerResponse(
            customer.getFirstName(),
            customer.getLastName(),
            customer.getEmail(),
            addres
        );
    }
}
