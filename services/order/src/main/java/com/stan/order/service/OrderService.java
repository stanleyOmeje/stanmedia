package com.stan.order.service;


import com.stan.order.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    List<OrderResponse> getAllOrder();

    OrderResponse getOrderById(Long id);
}
