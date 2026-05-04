package com.stan.order.entity;

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

    private String reference;
    private BigDecimal totalPrice;
    private PaymentMethod paymentMethod;
    private Long customerId;
    @OneToMany
    private List<Orderline> orderline;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedDate;
}
