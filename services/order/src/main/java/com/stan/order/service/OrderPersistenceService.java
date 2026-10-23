package com.stan.order.service;

import com.stan.order.customerHelper.CustomerResponse;
import com.stan.order.dto.request.OrderRequest;
import com.stan.order.dto.request.OrderlineRequest;
import com.stan.order.dto.request.PurchaseRequest;
import com.stan.order.dto.response.DefaultResponse;
import com.stan.order.dto.response.PersistOrderResponse;
import com.stan.order.entity.Orderline;
import com.stan.order.entity.Orders;
import com.stan.order.entity.PaymentAttempt;
import com.stan.order.enums.OrderStatus;
import com.stan.order.enums.PaymentAttemptStatus;
import com.stan.order.enums.ResponseStatus;
import com.stan.order.exceptions.BadRequestException;
import com.stan.order.exceptions.DuplicateOrderException;
import com.stan.order.exceptions.PaymentMisMatchException;
import com.stan.order.mapper.OrderMapper;
import com.stan.order.paymentHelper.PaymentService;
import com.stan.order.paymentHelper.dto.CreatePaymentResponse;
import com.stan.order.paymentHelper.dto.PaymentRequest;
import com.stan.order.repository.OrderRepository;
import com.stan.order.repository.OrderlineRepository;
import com.stan.order.repository.PaymentAttemptRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.stan.order.util.OrderUtil.validateAmount;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderPersistenceService {

    private final OrderRepository orderRepository;
    private final OrderlineService orderlineService;
    private final PaymentAttemptRepository paymentAttemptRepository;
    private final OrderMapper orderMapper;
    private final PaymentService paymentService;
    private final OrderlineRepository orderlineRepository;

    @Transactional
    public Orders createPendingOrder(
        String idempotencyKey,
        OrderRequest request
    ) {
        // Final check before creating a new order.
        // The unique database constraint remains essential
        // for handling concurrent requests.
        if (orderRepository.findByIdempotencyKey(idempotencyKey)
            .isPresent()) {
            throw new DuplicateOrderException(
                "Order already exists for this idempotency key"
            );
        }

        Orders order = orderMapper.mapOrderRequestToOrder(request);

        order.setIdempotencyKey(idempotencyKey);
        order.setStatus(OrderStatus.PENDING_PAYMENT);

        Orders savedOrder = orderRepository.saveAndFlush(order);

        for (PurchaseRequest item : request.purchaseRequest()) {
            OrderlineRequest orderline = new OrderlineRequest(
                null,
                item.productCode(),
                item.amount(),
                item.quantity(),
                item.belt()
            );

            orderlineService.createOrderline(orderline, savedOrder);
        }

        PaymentAttempt attempt = new PaymentAttempt();
        attempt.setOrder(savedOrder);
        attempt.setIdempotencyKey(
            "payment:" + savedOrder.getReference()
        );
        attempt.setStatus(PaymentAttemptStatus.PENDING);

        paymentAttemptRepository.save(attempt);

        return savedOrder;
    }


    //@Transactional
    public PersistOrderResponse createOrder(OrderRequest request, CustomerResponse customer, String bearerToken) {
        // 4. Map and validate order before persisting
        PersistOrderResponse response = new PersistOrderResponse();
        Orders order = orderMapper.mapOrderRequestToOrder(request);

        if (order == null) {
            throw new IllegalStateException("Unable to create order from request");
        }

        if (!validateAmount(order.getTotalPrice(), request.amount())) {
            throw new PaymentMisMatchException(
                ResponseStatus.PAYMENT_FAILED.getCode(),
                ResponseStatus.PAYMENT_FAILED.getMessage()
            );
        }

        order = orderRepository.save(order);

        if (order.getId() == null) {
            throw new IllegalStateException("Order could not be persisted");
        }

        // 5. Create orderlines
        for (PurchaseRequest item : request.purchaseRequest()) {
            if (item == null) {
                throw new BadRequestException(
                    "Purchase items cannot contain null entries"
                );
            }

            OrderlineRequest orderlineRequest = new OrderlineRequest(
                null,
                item.productCode(),
                item.amount(),
                item.quantity(),
                item.belt()
            );

            orderlineService.createOrderline(orderlineRequest, order);
        }

        // 6. Initiate payment
        PaymentRequest paymentRequest = new PaymentRequest(
            request.amount(),
            request.paymentMethod(),
            order.getId(),
            order.getReference(),
            customer
        );

        DefaultResponse<CreatePaymentResponse> paymentResponse =
            paymentService.makePayment(paymentRequest, bearerToken);
        if (!ResponseStatus.SUCCESS.getCode().equals(paymentResponse.getStatus())) {
            log.info("Payment NOT successful");
            response.setOrder(order);
            //throw new NotFoundException("01", "Payment NOT successful");
        }else{
            //update Order
            order = updateOrder(order);

            //Update orderline
            updateOrderline(order);

            String paymentReference = paymentResponse.getData().paymentReference();
            response.setPaymentReference(paymentReference);
            response.setOrder(order);
        }

        return response;
    }

    public Orders updateOrder(Orders order) {
        log.info("Updating order");
        if (order == null) {
            throw new BadRequestException("Order could not be updated");
        }
        order.setStatus(OrderStatus.PAID);
        order = orderRepository.save(order);
        return order;
    }

    public void updateOrderline(Orders order) {
        log.info("Updating orderline");
        if (order == null) {
            throw new BadRequestException("Order could not be updated");
        }
        List<Orderline> orderlines = orderlineRepository.findByOrder(order);
        if (orderlines.isEmpty()) {
            log.info("Orderline could not be updated because there is no orderline associated with this order");
        } else {
            orderlines.forEach(orderline -> {
                orderline.setStatus(OrderStatus.PAID);
                try {
                    orderlineRepository.save(orderline);
                } catch (Exception e) {
                    log.info("Orderline could not be updated...{}", e.getMessage());
                }
            });
        }
    }


}
