package com.shopsphere.auth_service.service;

import com.shopsphere.auth_service.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.micrometer.core.instrument.config.validate.Validated;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private Long expiration;
    public SecretKey secretKey(){
        return Keys.hmacShaKeyFor(
                secretKey.getBytes(StandardCharsets.UTF_8)
        );
    }
    public String generateToken(String email, Role role){
        return Jwts.builder()
                .subject(email)
                .claim("role",role.name())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(secretKey())
                .compact();
    }
    public Claims extractClaims(String token){
        return Jwts.parser()
                .verifyWith(secretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public String extractUsername(String token){
        return extractClaims(token).getSubject();
    }
    public boolean isTokenExpired(String token){
        return extractClaims(token).getExpiration().before(new Date());
    }
    public boolean isTokenValid(String token,String email){
        return extractUsername(token).equalsIgnoreCase(email) && !isTokenExpired(token);
    }
}