package com.stan.order.repository;


import com.stan.order.entity.Orderline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderlineRepository extends JpaRepository<Orderline, Long> {

    List<Orderline> findByOrderId(long orderId);
}
