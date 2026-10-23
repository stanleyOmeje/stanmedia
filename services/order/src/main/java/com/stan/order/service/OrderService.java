package com.stan.order.service;


import com.stan.order.dto.request.OrderRequest;
import com.stan.order.dto.response.CreateOrderResponse;
import com.stan.order.dto.response.DefaultResponse;
import com.stan.order.dto.response.OrderResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface OrderService {
    DefaultResponse<CreateOrderResponse> createOrder(OrderRequest request, HttpServletRequest httpServletRequest);

    DefaultResponse<List<OrderResponse>> getAllOrder();

    DefaultResponse<OrderResponse> getOrderById(Long id);

    DefaultResponse<OrderResponse> getOrderByOrderReferce(String orderReference);
}
