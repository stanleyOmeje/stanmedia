package com.stan.profile.service;

import com.stan.profile.entity.Users;
import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {
    String getUsername(String token);

    boolean isValidateToken(String token, UserDetails users);

    String generateToken(Users users);

    String generateRefreshToken(Users users);
}
