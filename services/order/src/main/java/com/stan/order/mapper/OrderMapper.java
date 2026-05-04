package com.stan.order.mapper;


import com.stan.order.dto.request.OrderRequest;
import com.stan.order.dto.response.OrderResponse;
import com.stan.order.entity.Orders;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OrderMapper {
    public Orders mapOrderRequestToOrder(OrderRequest request){
       // var order = new Order();
        return Orders.builder()
            .reference(request.reference())
            .totalPrice(request.amount())
            .customerId(request.customerId())
            .paymentMethod(request.paymentMethod())
            .createdAt(LocalDateTime.now())
            .build();
    }

    public static OrderResponse mapOrderToResponse(Orders order){
        return new OrderResponse(
           order.getId(),
           order.getReference(),
           order.getTotalPrice(),
           order.getPaymentMethod(),
           order.getCustomerId()
        );
    }
}
