package com.stan.order.service;


import com.stan.order.dto.request.OrderlineRequest;
import com.stan.order.dto.response.OrderlineResponse;
import com.stan.order.entity.Orderline;
import com.stan.order.entity.Orders;

import java.util.List;

public interface OrderlineService {
    Orderline createOrderline(OrderlineRequest request, Orders order);

    List<OrderlineResponse> getOrderlineByOrderId(long orderId);
}
