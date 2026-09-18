package com.campus.campus_event_management.service;

import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final String SECRET_KEY =
            "mySecretKeyForCampusEventManagementSystem123456789";

    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    // Create encryption key
    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
        );
    }

    // Generate JWT token
    public String generateToken(String email, String role) {

    return Jwts.builder()
            .subject(email)
            .claim("role", role)
            .issuedAt(new Date())
            .expiration(
                new Date(System.currentTimeMillis() + EXPIRATION_TIME)
            )
            .signWith(getKey())
            .compact();
}

    // Extract email from JWT token
    public String extractEmail(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }
    public Claims extractClaims(String token) {

    return Jwts.parser()
            .verifyWith(getKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
}
}