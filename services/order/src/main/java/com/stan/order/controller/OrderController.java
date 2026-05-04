package com.stan.order.controller;

import com.stan.order.customerHelper.CustomerClient;
import com.stan.order.dto.request.OrderRequest;
import com.stan.order.dto.request.OrderlineRequest;
import com.stan.order.dto.request.PurchaseRequest;
import com.stan.order.dto.response.OrderResponse;
import com.stan.order.exceptions.NotFoundException;
import com.stan.order.kafka.OrderConfirmation;
import com.stan.order.kafka.OrderProducer;
import com.stan.order.mapper.OrderMapper;
import com.stan.order.paymentHelper.PaymentClient;
import com.stan.order.paymentHelper.dto.PaymentRequest;
import com.stan.order.productHelper.ProductHttp;
import com.stan.order.repository.OrderRepository;
import com.stan.order.service.OrderService;
import com.stan.order.service.OrderlineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/orders")
public class OrderController {

    private final CustomerClient customerClient;
    private final ProductHttp productHttp;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderlineService orderlineService;
    private final OrderProducer orderProducer;
    private final OrderService orderService;
    private final PaymentClient paymentClient;


    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest request) {
        log.info("Inside OrderController::createOrder Creating order with request {}", request);

        //check customer
        var defaultCustomer = customerClient.getCustomerById(request.customerId()).orElseThrow(
            () -> new NotFoundException("01", "Customer not found")
        );
        if (!"00".equals(defaultCustomer.getStatus()) || defaultCustomer.getData() == null) {
            throw new NotFoundException("01", "Customer not found");
        }
        var customer = defaultCustomer.getData();

        //purchase product
        var purchasedProduct = productHttp.purchaseProduct(request.purchaseRequest());
        //Write a method to calculate the total amount of items
        //persist order
        var order = orderRepository.save(orderMapper.mapOrderRequestToOrder(request));

        //persist orderline
        for (PurchaseRequest purchaseRequest : request.purchaseRequest()) {
            OrderlineRequest orderlineRequest = new OrderlineRequest(null,
                purchaseRequest.productCode(),
                purchaseRequest.amount(),
                purchaseRequest.quantity(),
                purchaseRequest.belt()
            );
            orderlineService.createOrderline(orderlineRequest, order);
        }
        //start payment
        PaymentRequest paymentRequest = new PaymentRequest(
            request.amount(),
            request.paymentMethod(),
            order.getId(),
            request.reference(),
            customer
        );
        paymentClient.makePayment(paymentRequest);

        //send order confirmation notification
        orderProducer.sendOrderConfirmation(new OrderConfirmation(
            request.reference(),
            request.amount(),
            request.paymentMethod(),
            customer,
            purchasedProduct.getData()
        ));
        return ResponseEntity.ok(String.valueOf(order.getId()));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrder() {
        log.info("Inside OrderController::getAllOrder getting orders");
        return ResponseEntity.ok(orderService.getAllOrder());
    }


    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable("id") Long id) {
        log.info("Inside OrderController::getOrderById getting single orders");
        return ResponseEntity.ok(orderService.getOrderById(id));
    }
}
