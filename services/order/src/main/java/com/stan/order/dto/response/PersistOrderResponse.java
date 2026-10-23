package com.stan.order.dto.response;

import com.stan.order.entity.Orders;
import com.stan.order.entity.Transactions;
import lombok.Data;

@Data
public class PersistOrderResponse {
    private Orders order;
    private String paymentReference;
}
