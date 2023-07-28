package com.example.noteapp.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

import static java.lang.Long.parseLong;

@Component
public class JwtService {
    private final String SECRET_KEY = "JujutsuKaisen";
    public static final String AUTH_TOKEN = "Token";
    public static final String RESET_TOKEN = "ResetToken";
    public String generateToken(Map<String, Object> extraClaim, UserDetails userDetails) {
        long expirationTime = 1000 * 60 * 60;
        return Jwts.builder()
                .setClaims(extraClaim)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }
    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }
    public long extractId(String token) {
        return parseLong(extractAllClaims(token).get("id").toString());
    }
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date(System.currentTimeMillis()));
    }
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}