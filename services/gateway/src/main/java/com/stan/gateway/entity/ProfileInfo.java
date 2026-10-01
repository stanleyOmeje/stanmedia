package com.stan.gateway.entity;

import com.stan.gateway.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ProfileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
}
