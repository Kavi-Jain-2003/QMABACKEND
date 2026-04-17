package com.app.quantitymeasurement.security;

import java.util.Date;


import io.jsonwebtoken.security.Keys;
import java.security.Key;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

	private final String SECRET = "mysecretkeymysecretkeymysecretkey123";

	private Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    public boolean validateToken(String token, String username) {
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return expiration.before(new Date());
    }

}
