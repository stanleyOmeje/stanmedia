package com.stan.data_storage.mapper;


import com.stan.data_storage.dto.request.CustomerRequest;
import com.stan.data_storage.dto.response.Address;
import com.stan.data_storage.dto.response.CustomerResponse;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CustomerMapper {

    public CustomerResponse mapCustomerRequestToCustomerResponse(CustomerRequest customerRequest) {
        if(customerRequest == null) {
            return null;
        }
        Address address = new Address();
        address.setHouseNumber(customerRequest.houseNumber());
        address.setStreet(customerRequest.street());
        address.setZipCode(customerRequest.zipCode());
        return new CustomerResponse(
            customerRequest.firstName(),
            customerRequest.lastName(),
            customerRequest.email(),
            address
        );
    }
}
