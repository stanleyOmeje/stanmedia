package com.stan.payment.controller;

import com.stan.payment.dto.request.PaymentRequest;
import com.stan.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("api/v1/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Long> makePayment(@RequestBody PaymentRequest paymentRequest) {
        log.info("Inside PaymentController::makePayment with request: {}", paymentRequest);
        return ResponseEntity.ok(paymentService.makePayment(paymentRequest));
    }
}
