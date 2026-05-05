package com.stan.order.service.impl;


import com.stan.order.dto.response.DefaultResponse;
import com.stan.order.dto.response.OrderResponse;
import com.stan.order.enums.ResponseStatus;
import com.stan.order.exceptions.NotFoundException;
import com.stan.order.mapper.OrderMapper;
import com.stan.order.repository.OrderRepository;
import com.stan.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public DefaultResponse<List<OrderResponse>> getAllOrder() {
        log.info("Inside OrderController::getAllOrder getting orders");
        DefaultResponse<List<OrderResponse>> response = new DefaultResponse<>();
        var orders = Optional.of(orderRepository.findAll())
            .orElseThrow(() -> new NotFoundException("01", "Order Not Found"))
            .stream()
            .map(OrderMapper::mapOrderToResponse)
            .toList();
        if(orders.isEmpty()){
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
        var orders =  orderRepository.findById(id)
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
