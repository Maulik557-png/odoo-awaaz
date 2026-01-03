package com.odoo.hackathon.hrms.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtTokenProvider {

    // Provide safe defaults to avoid placeholder-resolution failures at runtime
    @Value("${jwt.secret:TestJwtSecretForLocalDevelopment_ChangeThisToARealSecret_0123456789}")
    private String secret;

    @Value("${jwt.expiration:3600000}")
    private long expiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // Generate JWT token
    public String generateToken(String loginId, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        String token = Jwts.builder()
                .setSubject(loginId)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();

        log.debug("Generated token prefix: {}..., signingKeyLen={}", token.length()>20?token.substring(0,20):token, getSigningKey().getEncoded().length);
        return token;
    }

    // Get loginId from token
    public String getLoginIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    // Get role from token
    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("role", String.class);
    }

    // Validate JWT token
    public boolean validateToken(String token) {
        try {
            log.debug("Validating token prefix: {}..., signingKeyLen={}", token.length()>20?token.substring(0,20):token, getSigningKey().getEncoded().length);
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (JwtException ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty");
        }
        return false;
    }
}