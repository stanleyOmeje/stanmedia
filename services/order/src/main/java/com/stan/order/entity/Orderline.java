package com.stan.order.entity;

import com.stan.order.enums.Belt;
import com.stan.order.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Orderline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    private double quantity;
    String productCode;
    BigDecimal amount;
    Belt belt;

    @ManyToOne
    @JoinColumn
    private Orders order;
}
