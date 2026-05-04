package com.stan.order.service.impl;


import com.stan.order.dto.response.OrderResponse;
import com.stan.order.exceptions.NotFoundException;
import com.stan.order.mapper.OrderMapper;
import com.stan.order.repository.OrderRepository;
import com.stan.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public List<OrderResponse> getAllOrder() {
        return Optional.of(orderRepository.findAll())
            .orElseThrow(() -> new NotFoundException("01", "Order Not Found"))
            .stream()
            .map(OrderMapper::mapOrderToResponse)
            .collect(Collectors.toList());
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        return orderRepository.findById(id)
            .map(OrderMapper::mapOrderToResponse)
            .orElseThrow(() -> new NotFoundException("01", "Order Not Found"));
    }
}
