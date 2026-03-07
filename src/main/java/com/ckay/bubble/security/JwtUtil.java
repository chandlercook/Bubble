package com.ckay.bubble.security;

import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    //TODO After implementing WebSockets, refactor this to use Claims to parse the JWT once each request (not 3 lol)

    /*
     * --- JWT Security Notes 2/26/26 ---
     *
     * I use JWT with HS256 (HMAC-SHA256).
     *
     * The signing key is NOT a password. It is a 256-bit cryptographic key
     * generated once using a CSPRNG (Keys.secretKeyFor in JJWT).
     *
     * The key is Base64-encoded only for storage (environment variable).
     * At runtime the app Base64-decodes it and reconstructs the SecretKey.
     * Base64 does not add security — it only transports binary data safely.
     *
     * Important :
     * 1. Never derive the key from a human string (no getBytes() on a password)
     * 2. Security comes from entropy (randomness of the key), not complexity.
     *
     * If the key leaks, attackers could then forge valid tokens and impersonate users.
     */


    //Signs and verifies JWTs
    private final SecretKey secretKey;

    public JwtUtil(@Value("${JWT_SECRET}") String secret) {

        System.out.println("JWT_SECRET = " + System.getenv("JWT_SECRET"));

        if (secret == null) {
            throw new IllegalStateException("JWT_SECRET environment variable not set");
        }

        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes); // Generate

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
        return Jwts.parserBuilder()         // returns JwtParserBuilder now
                .setSigningKey(secretKey)   // configure signing key
                .build()                    // build JwtParser
                .parseClaimsJws(token)      // parse token
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
