package com.stan.order.service;


import com.stan.order.dto.response.DefaultResponse;
import com.stan.order.dto.response.OrderResponse;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface OrderService {
    DefaultResponse<List<OrderResponse>> getAllOrder();

    DefaultResponse<OrderResponse> getOrderById(Long id);

    DefaultResponse<OrderResponse> getOrderByOrderReferce(String orderReference);
}
