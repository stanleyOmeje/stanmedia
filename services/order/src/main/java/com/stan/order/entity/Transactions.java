package com.stan.order.entity;

import com.stan.order.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionReference;
    private String paymentReference;
    private BigDecimal amount;
    private String customerEmail;

    @Enumerated(EnumType.STRING)
    private Status settlementStatus;

    @OneToOne
    private Orders order;

    private Date createdAt;
    private Date updatedAt;

}
