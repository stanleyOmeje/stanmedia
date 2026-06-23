package com.stan.profile.service.impl;

import com.stan.profile.entity.Users;
import com.stan.profile.service.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTServiceImpl implements JWTService {

    private static final String SECRET = "7RnmiKd1wsGA/NHiQnea8bAEYPCNmztOdSKuNOqlaw4=";

    public String generateToken(Users users) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts
            .builder()
            .claims()
            .add(claims)
            .subject(users.getEmail())
            .issuer("STAN")
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 15))
            .and()
            .signWith(generateKey())
            .compact();
    }

    public String generateRefreshToken(Users users) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts
            .builder()
            .claims()
            .add(claims)
            .subject(users.getEmail())
            .issuer("STAN")
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
            .and()
            .signWith(generateKey())
            .compact();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims getClaims(String token) {
        return Jwts
            .parser()
            .verifyWith(generateKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();

    }

    public String getUsername(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isValidateToken(String token, UserDetails users) {
        String userFromToken = getClaim(token, Claims::getSubject);
        String userFromUserDetails = users.getUsername();

        return !isTokenExpired(token) && userFromToken.equals(userFromUserDetails);
    }

    private boolean isTokenExpired(String token) {
        return getClaim(token, Claims::getExpiration).before(new Date());
    }

    private SecretKey generateKey() {
        byte[] kK = Decoders.BASE64.decode(getSecret());
        return Keys.hmacShaKeyFor(kK);
    }

    public String getSecret() {
        return SECRET;
    }
}
