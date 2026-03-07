package com.stan.product.product.entity;


import com.stan.product.product.enums.FeeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
public class FeeMapping {

    @Id
    private Long id;
    private FeeType feeType;
    private BigDecimal price;

    @OneToOne
    private Product product;

}
