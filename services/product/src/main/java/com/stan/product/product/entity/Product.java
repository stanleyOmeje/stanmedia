package com.stan.product.product.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stan.product.product.enums.Belt;
import com.stan.product.product.enums.ProductType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

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
    private Date createdAt;
    private Date updatedAt;

    @Enumerated(EnumType.STRING)
    private Belt belt;


    @OneToOne(mappedBy = "product")
    private FeeMapping fee;

    @JsonIgnore
    @JoinColumn(name = "category_id")
    @ManyToOne
    private Category category;

}
