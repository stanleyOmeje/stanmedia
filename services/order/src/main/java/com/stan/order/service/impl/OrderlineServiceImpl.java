package com.stan.order.service.impl;


import com.stan.order.dto.request.OrderlineRequest;
import com.stan.order.dto.response.OrderlineResponse;
import com.stan.order.entity.Orderline;
import com.stan.order.entity.Orders;
import com.stan.order.exceptions.NotFoundException;
import com.stan.order.mapper.OrderlineMapper;
import com.stan.order.repository.OrderlineRepository;
import com.stan.order.service.OrderlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderlineServiceImpl implements OrderlineService {

    private final OrderlineRepository orderlineRepository;
    private final OrderlineMapper orderlineMapper;

    public Orderline createOrderline(OrderlineRequest request, Orders order) {

        var orderline = orderlineRepository.save(orderlineMapper.mapOrderlineRequestToOrderline(
            request, order
        ));
        return orderline;
    }

    @Override
    public List<OrderlineResponse> getOrderlineByOrderId(long orderId) {
        return Optional.of(orderlineRepository.findByOrderId(orderId))
            .orElseThrow(() -> new NotFoundException("01", "Order Not Found"))
            .stream()
            .map(OrderlineMapper::mapOrderlineToOrderlineResponse)
            .collect(Collectors.toList());
    }
}
