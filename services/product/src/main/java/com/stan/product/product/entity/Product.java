package com.stan.product.product.entity;


import com.stan.product.product.enums.Belt;
import com.stan.product.product.enums.ProductType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @NotBlank(message = "product code cannot be blank")
    private String code;
    private String description;
    private double availableQuantity;

    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @Enumerated(EnumType.STRING)
    private Belt belt;

    @OneToOne(mappedBy = "product")
    private FeeMapping fee;

    @JoinColumn(name = "category_id")
    @ManyToOne
    private Category category;

}
