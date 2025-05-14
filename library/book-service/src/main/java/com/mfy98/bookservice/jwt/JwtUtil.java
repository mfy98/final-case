/*
package com.mfy98.bookservice.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private final String secret = "B7sT5lsNO-YktTYcqUQn1NnkNerKFdlzT6qWwAcsHb0=";

    public Claims validate(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}*/
