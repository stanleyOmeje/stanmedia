package com.stan.order.service.impl;


import com.stan.order.customerHelper.CustomerClient;
import com.stan.order.dto.request.OrderRequest;
import com.stan.order.dto.response.CreateOrderResponse;
import com.stan.order.dto.response.DefaultResponse;
import com.stan.order.dto.response.OrderResponse;
import com.stan.order.dto.response.PersistOrderResponse;
import com.stan.order.entity.Orders;
import com.stan.order.entity.Transactions;
import com.stan.order.enums.OrderStatus;
import com.stan.order.enums.ResponseStatus;
import com.stan.order.enums.Status;
import com.stan.order.exceptions.BadRequestException;
import com.stan.order.exceptions.NotFoundException;
import com.stan.order.exceptions.PaymentMisMatchException;
import com.stan.order.kafka.OrderConfirmation;
import com.stan.order.kafka.OrderProducer;
import com.stan.order.mapper.OrderMapper;
import com.stan.order.paymentHelper.PaymentService;
import com.stan.order.productHelper.ProductHttp;
import com.stan.order.repository.OrderRepository;
import com.stan.order.repository.TransactionRepository;
import com.stan.order.service.OrderPersistenceService;
import com.stan.order.service.OrderService;
import com.stan.order.service.OrderlineService;
import com.stan.order.service.TransactionService;
import com.stan.order.util.ReferenceUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.stan.order.util.OrderUtil.validateAmount;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    private final CustomerClient customerClient;
    private final ProductHttp productHttp;
    private final OrderlineService orderlineService;
    private final OrderProducer orderProducer;
    private final TransactionService transactionService;
    private final PaymentService paymentService;
    private final OrderPersistenceService orderPersistenceService;
    private final TransactionRepository transactionRepository;

    @Override
    public DefaultResponse<CreateOrderResponse> createOrder(
        OrderRequest request,
        HttpServletRequest httpServletRequest
    ) {
        log.info("Creating order");

        // 1. Validate request and extract the authorization header
        if (request == null) {
            throw new BadRequestException("Order request cannot be null");
        }

        if (httpServletRequest == null) {
            throw new BadRequestException("HTTP request cannot be null");
        }

        String bearerToken = httpServletRequest.getHeader("Authorization");

        if (StringUtils.isBlank(bearerToken)
            || !StringUtils.startsWithIgnoreCase(bearerToken, "Bearer ")
            || StringUtils.isBlank(bearerToken.substring(7))) {
            throw new BadRequestException("A valid bearer token is required");
        }

        // Validate required order fields before calling external services
        if (StringUtils.isBlank(request.email())) {
            throw new BadRequestException("Customer email is required");
        }

        if (StringUtils.isBlank(request.idempotencyKey())) {
            throw new BadRequestException("Customer idempotencyKey is required");
        }

        if (request.amount() == null
            || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Order amount must be greater than zero");
        }

        if (request.purchaseRequest() == null
            || request.purchaseRequest().isEmpty()) {
            throw new BadRequestException("Purchase items cannot be empty");
        }

        var existingOrder = orderRepository
            .findByIdempotencyKey(request.idempotencyKey());

        if (existingOrder.isPresent()) {
            Orders order = existingOrder.get();

            return switch (order.getStatus()) {
                case PAID -> successResponse(order);
                case PENDING_PAYMENT, PAYMENT_PROCESSING -> pendingResponse(order);
                case PAYMENT_FAILED -> handleExistingFailedOrder(order);
            };
        }


        // 2. Retrieve and validate customer
        var customerResponse =
            customerClient.getCustomerByEmail(request.email(), bearerToken);

        if (customerResponse == null
            || customerResponse.isEmpty()
            || !"00".equals(customerResponse.get().getStatus())
            || customerResponse.get().getData() == null) {
            throw new NotFoundException("01", "Customer not found");
        }

        var customer = customerResponse.get().getData();

        // 3. Purchase product and validate the response
        var purchasedProduct =
            productHttp.purchaseProduct(request.purchaseRequest(), bearerToken);

        if (purchasedProduct == null
            || purchasedProduct.getData() == null
            || purchasedProduct.getData().getGrandTotal() == null) {
            throw new IllegalStateException(
                "Product service returned an invalid purchase response"
            );
        }

        var productData = purchasedProduct.getData();

        if (!validateAmount(productData.getGrandTotal(), request.amount())) {
            throw new PaymentMisMatchException(
                ResponseStatus.PAYMENT_FAILED.getCode(),
                "Amount mismatch"
            );
        }

        //create initial transaction
        Transactions transaction = buildInitialTransactions(request);
        transactionService.createTransaction(transaction);


//        // 4. Map and validate order before persisting
//        Orders order = orderMapper.mapOrderRequestToOrder(request);
//
//        if (order == null) {
//            throw new IllegalStateException("Unable to create order from request");
//        }
//
//        if (!validateAmount(order.getTotalPrice(), request.amount())) {
//            throw new PaymentMisMatchException(
//                ResponseStatus.PAYMENT_FAILED.getCode(),
//                ResponseStatus.PAYMENT_FAILED.getMessage()
//            );
//        }
//
//        order = orderRepository.save(order);
//
//        if (order.getId() == null) {
//            throw new IllegalStateException("Order could not be persisted");
//        }
//
//        // 5. Create orderlines
//        for (PurchaseRequest item : request.purchaseRequest()) {
//            if (item == null) {
//                throw new BadRequestException(
//                    "Purchase items cannot contain null entries"
//                );
//            }
//
//            OrderlineRequest orderlineRequest = new OrderlineRequest(
//                null,
//                item.productCode(),
//                item.amount(),
//                item.quantity(),
//                item.belt()
//            );
//
//            orderlineService.createOrderline(orderlineRequest, order);
//        }
//
//        // 6. Initiate payment
//         PaymentRequest paymentRequest = new PaymentRequest(
//            request.amount(),
//            request.paymentMethod(),
//            order.getId(),
//            order.getReference(),
//            customer
//        );
//
//        DefaultResponse<CreatePaymentResponse> paymentResponse =
//            paymentService.makePayment(paymentRequest, bearerToken);
//        if (!ResponseStatus.SUCCESS.getCode().equals(paymentResponse.getStatus())) {
//            throw new NotFoundException("01", "Payment NOT successful");
//        }
//
//


        PersistOrderResponse persistOrderResponse = orderPersistenceService.createOrder(request, customer, bearerToken);
        Orders order = persistOrderResponse.getOrder();
        String paymentReference = persistOrderResponse.getPaymentReference();

        // IMPORTANT:
        // Validate the actual payment success status here using your
        // PaymentResponse fields and business status codes.
        // A non-null response alone does not mean payment succeeded.

        // 7. update order
        //order = updateOrder(order, orderRepository);

        // 7. update transaction
        transaction = updateTransactions(transaction,
            order,
            paymentReference,
            transactionRepository);

        // 8. Send order confirmation
        log.info("Sending Order Confirmation");
        orderProducer.sendOrderConfirmation(
            new OrderConfirmation(
                order.getReference(),
                request.amount(),
                request.paymentMethod(),
                customer,
                productData.getPurchaseResponses()
            )
        );
        log.info("Order Confirmation sent successfully");

        // 9. Return success
        DefaultResponse<CreateOrderResponse> response =
            new DefaultResponse<>();

        response.setStatus(ResponseStatus.SUCCESS.getCode());
        response.setMessage("Order created successfully");
        response.setData(new CreateOrderResponse(order.getReference()));

        log.info("Order createOrder r successfully");
        return response;
    }

    private DefaultResponse<CreateOrderResponse> successResponse(Orders order) {
        DefaultResponse<CreateOrderResponse> response =
            new DefaultResponse<>();

        response.setStatus(ResponseStatus.SUCCESS.getCode());
        response.setMessage("Order already created successfully");
        response.setData(new CreateOrderResponse(order.getReference()));

        return response;
    }

    private DefaultResponse<CreateOrderResponse> pendingResponse(Orders order) {
        DefaultResponse<CreateOrderResponse> response =
            new DefaultResponse<>();

        response.setStatus(ResponseStatus.PENDING.getCode());
        response.setMessage(ResponseStatus.PENDING.getMessage() +" PAYMENT");
        response.setData(new CreateOrderResponse(order.getReference()));

        return response;
    }

    private DefaultResponse<CreateOrderResponse> handleExistingFailedOrder(Orders order) {
        DefaultResponse<CreateOrderResponse> response =
            new DefaultResponse<>();

        response.setStatus(ResponseStatus.FAILED.getCode());
        response.setMessage(ResponseStatus.FAILED.getMessage());
        response.setData(new CreateOrderResponse(order.getReference()));

        return response;
    }

    private static Transactions buildInitialTransactions(
        OrderRequest request
    ) {
        Objects.requireNonNull(request, "Order request cannot be null");

        Transactions transaction = new Transactions();
        transaction.setTransactionReference(
            ReferenceUtil.generateInternalReference()
        );
        transaction.setAmount(request.amount());
        transaction.setCustomerEmail(request.email());
        transaction.setSettlementStatus(Status.PENDING);

        transaction.setCreatedAt(new Date());

        return transaction;
    }

    private static Transactions getTransactions(
        OrderRequest request,
        Orders order
    ) {
        Objects.requireNonNull(request, "Order request cannot be null");
        Objects.requireNonNull(order, "Order cannot be null");

        Transactions transaction = new Transactions();
        transaction.setTransactionReference(
            ReferenceUtil.generateInternalReference()
        );
        transaction.setAmount(request.amount());
        transaction.setCustomerEmail(request.email());
        transaction.setSettlementStatus(Status.PENDING);
        transaction.setOrder(order);
        transaction.setCreatedAt(new Date());

        return transaction;
    }

    private static Transactions updateTransactions(
        Transactions transaction,
        Orders order, String paymentReference,
        TransactionRepository transactionRepository
    ) {
        log.info("Inside updateTransactions with transaction ...{} and order {}", transaction, order);
        Objects.requireNonNull(order, "Order cannot be null");
        transaction.setOrder(order);
        transaction.setPaymentReference(paymentReference);
        transaction.setSettlementStatus(Status.COMPLETED);
        transaction.setUpdatedAt(new Date());

        transaction = transactionRepository.save(transaction);

        log.info("updated transaction is {}", transaction);
        return transaction;
    }

    private static Orders updateOrder(
        Orders order, OrderRepository orderRepository
    ) {
        log.info("Inside updateOrder with order {}",  order);
        Objects.requireNonNull(order, "Order cannot be null");
        order.setStatus(OrderStatus.PAID);

        order = orderRepository.save(order);

        log.info("updated order is {}", order);
        return order;
    }


    @Override
    public DefaultResponse<List<OrderResponse>> getAllOrder() {
        log.info("Inside OrderController::getAllOrder getting orders");
        DefaultResponse<List<OrderResponse>> response = new DefaultResponse<>();
        var orders = Optional.of(orderRepository.findAll())
            .orElseThrow(() -> new NotFoundException("01", "Order Not Found"))
            .stream()
            .map(OrderMapper::mapOrderToResponse)
            .toList();
        if (orders.isEmpty()) {
            throw new NotFoundException("01", "Order Not Found");
        }
        response.setStatus(ResponseStatus.SUCCESS.getCode());
        response.setMessage(ResponseStatus.SUCCESS.getMessage());
        response.setData(orders);
        log.info("Returning getAllOrder response...{}", response);
        return response;
    }

    @Override
    public DefaultResponse<OrderResponse> getOrderById(Long id) {
        log.info("Inside OrderController::getOrderById getting order by id");
        DefaultResponse<OrderResponse> response = new DefaultResponse<>();
        var orders = orderRepository.findById(id)
            .map(OrderMapper::mapOrderToResponse)
            .orElseThrow(() -> new NotFoundException("01", "Order Not Found"));
        response.setStatus(ResponseStatus.SUCCESS.getCode());
        response.setMessage(ResponseStatus.SUCCESS.getMessage());
        response.setData(orders);
        log.info("Returning getOrderById response...{}", response);
        return response;
    }

    @Override
    public DefaultResponse<OrderResponse> getOrderByOrderReferce(String orderReference) {
        log.info("Inside OrderController::getOrderByOrderReferce getting order by order reference");
        DefaultResponse<OrderResponse> response = new DefaultResponse<>();
        var orders = orderRepository.findByReference(orderReference)
            .map(OrderMapper::mapOrderToResponse)
            .orElseThrow(() -> new NotFoundException("01", "Order Not Found"));
        response.setStatus(ResponseStatus.SUCCESS.getCode());
        response.setMessage(ResponseStatus.SUCCESS.getMessage());
        response.setData(orders);
        log.info("Returning getOrderByOrderReferce response...{}", response);
        return response;
    }
}
