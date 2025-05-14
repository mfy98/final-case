/*
package com.mfy98.borrowservice.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtil {
    private final String secret = "B7sT5lsNO-YktTYcqUQn1NnkNerKFdlzT6qWwAcsHb0=";
    private final long expiry = 1000 * 60 * 60;


    public Claims validate(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}*/
