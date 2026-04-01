package com.finflow.authservice.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import com.finflow.authservice.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private final String secret;
    private final long expiration;

    public JwtUtil(
            @Value("${finflow.jwt.secret}") String secret,
            @Value("${finflow.jwt.expiration-ms}") long expiration
    ) {
        this.secret = secret;
        this.expiration = expiration;
    }

    private Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        claims.put("userId", user.getId());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractRole(String token) {
        Object role = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
        return role == null ? "" : role.toString();
    }

    public Long extractUserId(String token) {
        Object userId = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId");
        if (userId instanceof Integer integerValue) {
            return integerValue.longValue();
        }
        if (userId instanceof Long longValue) {
            return longValue;
        }
        return userId == null ? null : Long.valueOf(userId.toString());
    }
}
