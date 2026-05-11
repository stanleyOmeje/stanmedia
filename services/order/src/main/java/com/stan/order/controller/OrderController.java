package com.stan.order.controller;

import com.stan.order.customerHelper.CustomerClient;
import com.stan.order.dto.request.OrderRequest;
import com.stan.order.dto.request.OrderlineRequest;
import com.stan.order.dto.request.PurchaseRequest;
import com.stan.order.dto.response.CreateOrderResponse;
import com.stan.order.dto.response.DefaultResponse;
import com.stan.order.dto.response.OrderResponse;
import com.stan.order.enums.ResponseStatus;
import com.stan.order.exceptions.NotFoundException;
import com.stan.order.exceptions.PaymentMisMatchException;
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

import static com.stan.order.util.OrderUtil.validateAmount;

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
    public ResponseEntity<DefaultResponse<CreateOrderResponse>> createOrder(@RequestBody OrderRequest request) {
        DefaultResponse<CreateOrderResponse> response = new DefaultResponse<CreateOrderResponse>();
        log.info("Inside OrderController::createOrder Creating order with request {}", request);

        //check customer
        var defaultCustomer = customerClient.getCustomerByEmail(request.email()).orElseThrow(
            () -> new NotFoundException("01", "Customer not found")
        );
        if (!"00".equals(defaultCustomer.getStatus()) || defaultCustomer.getData() == null) {
            throw new NotFoundException("01", "Customer not found");
        }
        var customer = defaultCustomer.getData();

        //purchase product
        var purchasedProduct = productHttp.purchaseProduct(request.purchaseRequest());
        //Check totalAmount against sum of items amount
        if(!validateAmount(purchasedProduct.getData().getGrandTotal(), request.amount())){
            throw new PaymentMisMatchException(ResponseStatus.PAYMENT_FAILED.getCode(), "Amount MisMatch");
        }
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
            order.getReference(),
            customer
        );
        if(!validateAmount(order.getTotalPrice(), request.amount())){
            throw new PaymentMisMatchException(ResponseStatus.PAYMENT_FAILED.getCode(),
                ResponseStatus.PAYMENT_FAILED.getMessage());
        }
        var paymentResponse = paymentClient.makePayment(paymentRequest);
        log.info("paymentResponse ...{}", paymentResponse);

        //send order confirmation notification
        orderProducer.sendOrderConfirmation(new OrderConfirmation(
            order.getReference(),
            request.amount(),
            request.paymentMethod(),
            customer,
            purchasedProduct.getData().getPurchaseResponses()
        ));
        response.setStatus(ResponseStatus.SUCCESS.getCode());
        response.setMessage("Order created successfully");
        response.setData(new CreateOrderResponse(order.getReference()));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<DefaultResponse<List<OrderResponse>>> getAllOrder() {
        log.info("Inside OrderController::getAllOrder getting orders");
        return ResponseEntity.ok(orderService.getAllOrder());
    }


    @GetMapping("/{id}")
    public ResponseEntity<DefaultResponse<OrderResponse>> getOrderById(@PathVariable("id") Long id) {
        log.info("Inside OrderController::getOrderById getting single orders");
        return ResponseEntity.ok(orderService.getOrderById(id));
    }



    @GetMapping("/order-reference/{orderReference}")
    public ResponseEntity<DefaultResponse<OrderResponse>> getOrderByOrderReferce(@PathVariable("orderReference") String orderReference) {
        log.info("Inside OrderController::getOrderByOrderReferce getting single orders");
        return ResponseEntity.ok(orderService.getOrderByOrderReferce(orderReference));
    }
}
