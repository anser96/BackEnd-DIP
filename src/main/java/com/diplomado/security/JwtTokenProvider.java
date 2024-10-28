package com.diplomado.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // Asegúrate de que la clave tenga al menos 32 caracteres
    private final String jwtSecret = "thisIsASecretKeyThatHasAtLeast32Chars!";
    private final long jwtExpiration = 86400000; // 24 horas

    // Crear una clave segura a partir de jwtSecret
    private final SecretKey secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());

    public String generateToken(String key) {
        return Jwts.builder()
                .setSubject(key)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpiration))
                .signWith(secretKey) // Firma con la clave secreta
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey) // Usar el secretKey
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey) // Usar el secretKey
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
