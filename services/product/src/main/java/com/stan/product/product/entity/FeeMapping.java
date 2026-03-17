package com.stan.product.product.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stan.product.product.enums.FeeType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
public class FeeMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private FeeType feeType;
    private BigDecimal price;
    private Date createdAt;
    private Date updatedAt;

    @JsonIgnore
    @OneToOne
    private Product product;

}
