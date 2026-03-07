package com.stan.customer.service.impl;

import com.stan.customer.domain.Customer;
import com.stan.customer.dto.request.CustomerRequest;
import com.stan.customer.dto.response.CustomerResponse;
import com.stan.customer.dto.response.DefaultResponse;
import com.stan.customer.mapper.CustomerMapper;
import com.stan.customer.repository.CustomerRepository;
import com.stan.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public ResponseEntity<String> createCustomer(CustomerRequest customerRequest) {
        log.info("Inside CustomerServiceImpl::createCustomer with request " + customerRequest);
        try {
            Customer customer = customerRepository.save(customerMapper.mapCustomerrequestToCustomer(customerRequest));
            return ResponseEntity.ok("Customer created successfully");
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return null;
    }

    @Override
    public ResponseEntity<String> updateCustomer(String id, CustomerRequest request) {
        log.info("Inside CustomerServiceImpl::updateCustomer with request " + request);
        try {
            Optional<Customer> optionalCustomer = customerRepository.findById(id);
            if (optionalCustomer.isPresent()) {
                Customer customer = optionalCustomer.get();
                customer.setFirstName(request.firstName());
                customer.setLastName(request.lastName());
                customer.setEmail(request.email());
                customer.setAddress(request.address());
                customerRepository.save(customer);
                return ResponseEntity.ok("Customer updated successfully");
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return null;
    }

    @Override
    public DefaultResponse<List<CustomerResponse>> getCustomer() {
        log.info("Inside CustomerServiceImpl::getCustomer ");
        DefaultResponse<List<CustomerResponse>> defaultResponse = new DefaultResponse<>();
        defaultResponse.setStatus("01");
        defaultResponse.setMessage("Failed");
        List<CustomerResponse> customerResponseList = new ArrayList<>();
        try {
            List<Customer> customer = customerRepository.findAll();
            if (customer.isEmpty()) {
                defaultResponse.setStatus("01");
                defaultResponse.setMessage("No customers found");
            } else {
                defaultResponse.setStatus("00");
                defaultResponse.setMessage("Success");
                customerResponseList = customer.stream().map(customerMapper::mapCustomerToCustomerResponse).toList();
                defaultResponse.setData(customerResponseList);
            }

        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return defaultResponse;
    }

    @Override
    public DefaultResponse<CustomerResponse> getCustomerById(String id) {
        log.info("Inside CustomerServiceImpl::getCustomerById  ");
        DefaultResponse<CustomerResponse> defaultResponse = new DefaultResponse<>();
        try {
            Optional<Customer> customer = customerRepository.findById(id);
            if (customer.isPresent()) {
                defaultResponse.setStatus("00");
                defaultResponse.setMessage("Success");
                CustomerResponse customerResponse = customerMapper.mapCustomerToCustomerResponse(customer.get());
                defaultResponse.setData(customerResponse);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        defaultResponse.setStatus("01");
        defaultResponse.setMessage("Failed");
        return defaultResponse;
    }

    @Override
    public DefaultResponse<String> checkCustomerExistById(String id) {
        log.info("Inside CustomerServiceImpl::checkCustomerExistById  ");
        DefaultResponse<String> defaultResponse = new DefaultResponse<>();
        try {
            boolean customerExist = customerRepository.existsById(id);
            if (customerExist) {
                defaultResponse.setStatus("00");
                defaultResponse.setMessage("Success");
                defaultResponse.setData("Customer with id " + id + " already exist");
                return defaultResponse;
            } else {
                defaultResponse.setStatus("01");
                defaultResponse.setMessage("Failed");
                defaultResponse.setData("Customer with id " + id + " does not exist");
                return defaultResponse;
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return defaultResponse;
    }
}
