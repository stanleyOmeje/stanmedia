package com.stan.order.entity;

import com.stan.order.enums.OrderStatus;
import com.stan.order.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "idempotency_key", nullable = false, unique = true)
    private String idempotencyKey;

    private String reference;
    private BigDecimal totalPrice;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    private String customerEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @OneToMany
    private List<Orderline> orderline;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedDate;
}
