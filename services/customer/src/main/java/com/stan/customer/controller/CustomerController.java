package com.stan.customer.controller;

import com.stan.customer.dto.request.CustomerRequest;
import com.stan.customer.dto.response.DefaultResponse;
import com.stan.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {
    private final CustomerService customerService;
    @PostMapping
    public ResponseEntity<String> createCustomer(@RequestBody @Valid CustomerRequest customerRequest) {
      return customerService.createCustomer(customerRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCustomer(@PathVariable String id,
                                                 @RequestBody @Valid CustomerRequest customerRequest) {
        return customerService.updateCustomer(id,customerRequest);

    }

    @GetMapping
    public ResponseEntity<DefaultResponse<?>> getCustomer() {
       DefaultResponse defaultResponse =  customerService.getCustomer();
        return ResponseEntity.ok(defaultResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DefaultResponse<?>> getCustomerById(@PathVariable String id) {
        DefaultResponse defaultResponse =  customerService.getCustomerById(id);
        return ResponseEntity.ok(defaultResponse);
    }

    @GetMapping("/exist/{id}")
    public ResponseEntity<DefaultResponse<?>> checkCustomerExistById(@PathVariable String id) {
        DefaultResponse defaultResponse =  customerService.checkCustomerExistById(id);
        return ResponseEntity.ok(defaultResponse);
    }
}
