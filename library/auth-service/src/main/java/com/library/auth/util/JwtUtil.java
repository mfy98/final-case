package com.library.auth.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    // 256‑bit Base64 (URL‑safe) secret; test/prod profillerinde override edilebilir
    @Value("${jwt.secret:B7sT5lsNO-YktTYcqUQn1NnkNerKFdlzT6qWwAcsHb0=}")
    private String secretBase64;

    private Key key;
    private final long expiry = 1000 * 60 * 60; // 1h

    // Decode secret & build Key once
    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getUrlDecoder().decode(secretBase64);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username, String role) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiry))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims validate(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
