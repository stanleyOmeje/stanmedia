package com.stan.order.entity;

import com.stan.order.enums.Belt;
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

    private double quantity;
    String productCode;
    BigDecimal amount;
    Belt belt;

    @ManyToOne
    @JoinColumn
    private Orders order;
}
