package com.mfy98.apigateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.security.Key;
import java.util.Base64;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretBase64;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getUrlDecoder().decode(secretBase64);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
