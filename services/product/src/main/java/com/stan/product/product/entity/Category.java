package com.stan.product.product.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @NotBlank(message = "category code cannot be blank")
    private String code;
    private String description;
    private Date createdAt;
    private Date updatedAt;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private List<Product> product = new ArrayList<>();
}
