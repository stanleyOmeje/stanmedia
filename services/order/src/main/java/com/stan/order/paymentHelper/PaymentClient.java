package com.stan.order.paymentHelper;


import com.stan.order.paymentHelper.dto.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service",
url = "${app.config.payment-url}")
public interface PaymentClient {

    @PostMapping
    ResponseEntity<Long> makePayment(@RequestBody PaymentRequest request);
}
