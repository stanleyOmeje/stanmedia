package com.stan.order.mapper;


import com.stan.order.dto.request.OrderlineRequest;
import com.stan.order.dto.response.OrderlineResponse;
import com.stan.order.entity.Orderline;
import com.stan.order.entity.Orders;
import com.stan.order.enums.OrderStatus;
import org.springframework.stereotype.Component;

@Component
public class OrderlineMapper {
    public Orderline mapOrderlineRequestToOrderline(
        OrderlineRequest request,
        Orders order) {
        return Orderline.builder()
            .productCode(request.productCode())
            .status(OrderStatus.PENDING_PAYMENT)
            .amount(request.amount())
            .quantity(request.quantity())
            .belt(request.belt())
            .order(order)
            .build();
    }

    public static OrderlineResponse mapOrderlineToOrderlineResponse(Orderline orderline) {
        return new OrderlineResponse(
            orderline.getId(),
            orderline.getProductCode(),
            orderline.getAmount(),
            orderline.getQuantity(),
            orderline.getBelt()
        );
    }
}
