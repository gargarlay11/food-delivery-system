package com.fooddelivery.backend.security;

import java.util.Date;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey signingKey;

    private final long expirationMs;

    public JwtService(
            @Value("${app.jwt.secret}")
            String jwtSecret,

            @Value("${app.jwt.expiration-ms}")
            long expirationMs
    ) {

        this.signingKey =
                Keys.hmacShaKeyFor(
                    Decoders.BASE64.decode(
                        jwtSecret
                    )
                );

        this.expirationMs =
                expirationMs;
    }

    public String generateToken(
            UserDetails userDetails
    ) {

        Date now =
                new Date();

        Date expiration =
                new Date(
                    now.getTime()
                    + expirationMs
                );

        return Jwts.builder()
                .subject(
                    userDetails.getUsername()
                )
                .issuedAt(now)
                .expiration(expiration)
                .signWith(signingKey)
                .compact();
    }

    public String extractEmail(
            String token
    ) {

        return extractClaims(token)
                .getSubject();
    }

    public boolean isTokenValid(
            String token,
            UserDetails userDetails
    ) {

        String email =
                extractEmail(token);

        return email.equalsIgnoreCase(
                    userDetails.getUsername()
                )
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(
            String token
    ) {

        return extractClaims(token)
                .getExpiration()
                .before(
                    new Date()
                );
    }

    private Claims extractClaims(
            String token
    ) {

        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}