package com.stan.customer.controller;

import com.stan.customer.dto.request.CustomerRequest;
import com.stan.customer.dto.response.DefaultResponse;
import com.stan.customer.entity.Customer;
import com.stan.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {
    private final CustomerService customerService;
    @PostMapping
    public ResponseEntity<DefaultResponse<?>> createCustomer(@RequestBody @Valid CustomerRequest customerRequest) {
        log.info("Inside Customer Controller :: createCustomer with request ...{}", customerRequest);
        DefaultResponse response = customerService.createCustomer(customerRequest);
      return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DefaultResponse<?>> updateCustomer(@PathVariable String id,
                                                 @RequestBody @Valid CustomerRequest customerRequest) {
        log.info("Inside Customer Controller :: updateCustomer with request ...{}", customerRequest);
        DefaultResponse response = customerService.updateCustomer(id,customerRequest);
        return ResponseEntity.ok(response);

    }

    @GetMapping
    public ResponseEntity<DefaultResponse<?>> getCustomer() {
        log.info("Inside Customer Controller :: getCustomer ");
       DefaultResponse defaultResponse =  customerService.getCustomer();
        return ResponseEntity.ok(defaultResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DefaultResponse<?>> getCustomerById(@PathVariable long id) {
        log.info("Inside Customer Controller :: getCustomerById with request ...{}", id);
        DefaultResponse defaultResponse =  customerService.getCustomerById(id);
        return ResponseEntity.ok(defaultResponse);
    }

    @GetMapping("/exist/{id}")
    public ResponseEntity<DefaultResponse<?>> checkCustomerExistById(@PathVariable long id) {
        log.info("Inside Customer Controller :: checkCustomerExistById with request ...{}", id);
        DefaultResponse defaultResponse =  customerService.checkCustomerExistById(id);
        return ResponseEntity.ok(defaultResponse);
    }
}
