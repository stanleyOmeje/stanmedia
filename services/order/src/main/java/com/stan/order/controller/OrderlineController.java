package com.stan.order.controller;


import com.stan.order.dto.response.OrderlineResponse;
import com.stan.order.service.OrderlineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/order-line")
public class OrderlineController {
    private final OrderlineService orderlineService;

    @GetMapping("/order/{order-id}")
    public ResponseEntity<List<OrderlineResponse>> getOrderlineByOrderId(@PathVariable("order-id") long orderId) {
        log.info("Inside OrderlineController::getOrderlineByOrderId ");
        return ResponseEntity.ok(orderlineService.getOrderlineByOrderId(orderId));
    }
}
