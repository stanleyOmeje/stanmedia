package com.stan.customer.service.impl;


import com.stan.customer.dto.request.CustomerRequest;
import com.stan.customer.dto.response.CustomerResponse;
import com.stan.customer.dto.response.DefaultResponse;
import com.stan.customer.entity.Address;
import com.stan.customer.entity.Customer;
import com.stan.customer.enums.ResponseStatus;
import com.stan.customer.exceptions.AlreadyExistException;
import com.stan.customer.exceptions.NotFoundException;
import com.stan.customer.mapper.CustomerMapper;
import com.stan.customer.repository.AddressRepository;
import com.stan.customer.repository.CustomerRepository;
import com.stan.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final AddressRepository addressRepository;

    @Override
    public DefaultResponse<?> createCustomer(CustomerRequest customerRequest) {
        DefaultResponse<Customer> response = new DefaultResponse<>();
        log.info("Inside CustomerServiceImpl::createCustomer with request " + customerRequest);
        try {
            Optional<Customer> customerCheck = customerRepository.findByEmail(customerRequest.email());
            if (customerCheck.isPresent()){
                throw new AlreadyExistException(ResponseStatus.ALREADY_EXIST.getCode(), "Customer "+
                    ResponseStatus.ALREADY_EXIST.getMessage());
            }
            Customer customer = customerRepository.save(customerMapper.mapCustomerRequestToCustomer(customerRequest));
            Address address = createAddress(customerRequest, customer);
            customer.setAddress(address);
            customerRepository.save(customer);
            response.setStatus(ResponseStatus.CREATED.getCode());
            response.setMessage(ResponseStatus.CREATED.getMessage());
            response.setData(customer);
            log.info("response...{}", response);
            return response;
        } catch (AlreadyExistException e){
            throw new AlreadyExistException(ResponseStatus.ALREADY_EXIST.getCode(), "Customer "+
                ResponseStatus.ALREADY_EXIST.getMessage());
        }
        catch (Exception e) {
            log.info(e.getMessage());
        }
        return null;
    }

    public Address createAddress(CustomerRequest customerRequest, Customer customer) {
        log.info("Inside CustomerServiceImpl::createAddress with request " + customerRequest);
        try {
            Address address = new Address();
            address.setStreet(customerRequest.street());
            address.setHouseNumber(customerRequest.houseNumber());
            address.setZipCode(customerRequest.zipCode());
            address.setCreatedAt(new Date());
            address.setCustomer(customer);
            address = addressRepository.save(address);
            return address;
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return null;
    }

    @Override
    public DefaultResponse<?> updateCustomer(String email, CustomerRequest request) {
        log.info("Inside CustomerServiceImpl::updateCustomer with request " + request);
        DefaultResponse<Customer> response = new DefaultResponse<>();
        response.setStatus(ResponseStatus.FAILED.getCode());
        response.setMessage(ResponseStatus.FAILED.getMessage());
        try {
            Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
            if (optionalCustomer.isPresent()) {
                Customer customer = optionalCustomer.get();
                customer.setFirstName(request.firstName());
                customer.setLastName(request.lastName());
                customer.setEmail(request.email());
                customer.setUpdatedAt(new Date());

                Address address = updateAddress(request, customer);
                customer.setAddress(address);

                customerRepository.save(customer);
                response.setStatus(ResponseStatus.CREATED.getCode());
                response.setMessage(ResponseStatus.CREATED.getMessage());
                response.setData(customer);

                log.info("response...{}", response);
                return response;
            }else {
                response.setStatus(ResponseStatus.NOT_FOUND.getCode());
                response.setMessage(ResponseStatus.NOT_FOUND.getMessage());
                return response;
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return response;
    }


    public Address updateAddress(CustomerRequest customerRequest, Customer customer) {
        log.info("Inside CustomerServiceImpl::updateAddress with request " + customerRequest);
        try {

            Optional<Address> addressCheck = addressRepository.findByCustomer(customer);
            if (addressCheck.isEmpty()) {
               throw  new NotFoundException(ResponseStatus.NOT_FOUND.getCode(), ResponseStatus.NOT_FOUND.getMessage());
            }
            Address address = addressCheck.get();
            address.setStreet(customerRequest.street());
            address.setHouseNumber(customerRequest.houseNumber());
            address.setZipCode(customerRequest.zipCode());
            address.setUpdatedAt(new Date());
            address.setCustomer(customer);
            address = addressRepository.save(address);
            return address;
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
        log.info("response...{}", defaultResponse);
        return defaultResponse;
    }

    @Override
    public DefaultResponse<CustomerResponse> getCustomerById(long id) {
        log.info("Inside CustomerServiceImpl::getCustomerById  ");
        DefaultResponse<CustomerResponse> defaultResponse = new DefaultResponse<>();
        try {
            Optional<Customer> customer = customerRepository.findById(id);
            if (customer.isPresent()) {
                defaultResponse.setStatus("00");
                defaultResponse.setMessage("Success");
                CustomerResponse customerResponse = customerMapper.mapCustomerToCustomerResponse(customer.get());
                defaultResponse.setData(customerResponse);

                log.info("response...{}", defaultResponse);
                return defaultResponse;
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        defaultResponse.setStatus("01");
        defaultResponse.setMessage("Failed");
        log.info("response...{}", defaultResponse);
        return defaultResponse;
    }

    @Override
    public DefaultResponse<String> checkCustomerExistById(long id) {
        log.info("Inside CustomerServiceImpl::checkCustomerExistById  ");
        DefaultResponse<String> defaultResponse = new DefaultResponse<>();
        try {
            boolean customerExist = customerRepository.existsById(id);
            if (customerExist) {
                defaultResponse.setStatus("00");
                defaultResponse.setMessage("Success");
                defaultResponse.setData("Customer with id " + id + " already exist");

                log.info("response...{}", defaultResponse);
                return defaultResponse;
            } else {
                defaultResponse.setStatus("01");
                defaultResponse.setMessage("Failed");
                defaultResponse.setData("Customer with id " + id + " does not exist");

                log.info("response...{}", defaultResponse);
                return defaultResponse;
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        log.info("response...{}", defaultResponse);
        return defaultResponse;
    }
}
