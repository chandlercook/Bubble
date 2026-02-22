package com.ckay.bubble.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    //TODO After implementing WebSockets, refactor this to use Claims to parse the JWT once each request (not 3 lol)


    //Signs and verifies JWTs
    private final Key secretKey;

    public JwtUtil() {

        String secret = System.getenv("JWT_SECRET");
        if (secret == null) {
            throw new IllegalStateException("JWT_SECRET environment variable not set");
        }
        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Authentication authentication) {
        return Jwts.builder()
                .setSubject(authentication.getName()) // Issue a token to the user that Spring Security just verified
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour
                .signWith(secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()                  // returns JwtParserBuilder now
                .setSigningKey(secretKey)   // configure signing key
                .build()                     // build JwtParser
                .parseClaimsJws(token)       // parse token
                .getBody()
                .getSubject();
    }

    private boolean isTokenExpired(String token) {
        Date expirationDate = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expirationDate.before(new Date()); // Test the token expiration date with the current date
    }

    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }


}
