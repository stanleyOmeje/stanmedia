package com.stan.data_storage.controller;

import com.stan.data_storage.dto.request.CustomerRequest;
import com.stan.data_storage.dto.response.CustomerResponse;
import com.stan.data_storage.dto.response.DefaultResponse;
import com.stan.data_storage.mapper.CustomerMapper;
import com.stan.data_storage.service.RedisUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/storage")
public class CustomerController {

    private final RedisUtility redisUtility;
    private final CustomerMapper customerMapper;
    private static final String CUSTOMER_KEY = "customer";

    @PostMapping("/customers")
    public ResponseEntity<DefaultResponse<CustomerRequest>> saveCustomer(@RequestBody CustomerRequest customerRequest) {
        log.info("Inside CustomerController::SaveCustomer with request: {}", customerRequest);
        DefaultResponse<CustomerRequest> defaultResponse = new DefaultResponse<>();
        try {
            Duration duration = Duration.ofMillis(20000);
            redisUtility.save(CUSTOMER_KEY, customerRequest, duration);
            defaultResponse.setStatus("00");
            defaultResponse.setMessage("Customer saved successfully");
            defaultResponse.setData(customerRequest);
            return ResponseEntity.ok(defaultResponse);
        } catch (Exception e) {
            defaultResponse.setStatus("01");
            defaultResponse.setMessage(e.getMessage());
            defaultResponse.setData(null);
            return ResponseEntity.ok(defaultResponse);
        }
    }


    @GetMapping("/customers")
    public ResponseEntity<DefaultResponse<CustomerResponse>> getCustomer() {
        log.info("Inside CustomerController::getCustomer ");
        DefaultResponse<CustomerResponse> defaultResponse = new DefaultResponse<>();
        try {
            CustomerRequest customer = redisUtility.getWithObjectMapper(CUSTOMER_KEY, CustomerRequest.class);
            defaultResponse.setStatus("00");
            if (customer == null) {
                defaultResponse.setMessage("No Customer found");
            } else {
                defaultResponse.setMessage("Customer retrieved successfully");
            }
            defaultResponse.setData(customerMapper.mapCustomerRequestToCustomerResponse(customer));
            return ResponseEntity.ok(defaultResponse);
        } catch (Exception e) {
            log.info(e.getMessage());
            defaultResponse.setStatus("99");
            defaultResponse.setMessage(e.getMessage());
            defaultResponse.setData(null);
            return ResponseEntity.ok(defaultResponse);
        }
    }

    @DeleteMapping("/customers/delete")
    public ResponseEntity<DefaultResponse<String>> deleteCustomer() {
        log.info("Inside CustomerController::deleteCustomer ");
        DefaultResponse<String> defaultResponse = new DefaultResponse<>();
        try {
            redisUtility.delete(CUSTOMER_KEY);
            defaultResponse.setStatus("00");
            defaultResponse.setMessage("Customer deleted successfully");
            defaultResponse.setData("Customer has been deleted successfully");
            return ResponseEntity.ok(defaultResponse);
        } catch (Exception e) {
            log.info(e.getMessage());
            defaultResponse.setStatus("01");
            defaultResponse.setMessage(e.getMessage());
            defaultResponse.setData(null);
            return ResponseEntity.ok(defaultResponse);
        }
    }
}
