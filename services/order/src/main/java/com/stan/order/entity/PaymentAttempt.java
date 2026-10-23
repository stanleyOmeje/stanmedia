package com.stan.order.entity;

import com.stan.order.enums.OrderStatus;
import com.stan.order.enums.PaymentAttemptStatus;
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
public class PaymentAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Orders order;

    private String idempotencyKey;
    private PaymentAttemptStatus status;

}
