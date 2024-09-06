package com.example.demo.security;

import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class generateJWT {
    public static final long JWT_EXPIRATION = 700000000;
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String token(Authentication authentication) {
        String userName = authentication.getName();
        Date currentDate = new Date();
        Date expiryDate = new Date(currentDate.getTime() + JWT_EXPIRATION);

        String token = Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        System.out.println(token);
        return token;

    }

    public String getUserName(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            throw new AuthenticationCredentialsNotFoundException("Incorrect JWT Token",
                    ex.fillInStackTrace());
        }
    }

}
