package com.yt.jpa.hospitalManagement.security;

import com.yt.jpa.hospitalManagement.entity.User;
import com.yt.jpa.hospitalManagement.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Component
/* Create and verify token */
public class AuthUtil {

    @Value("${jwt.secretKey}")
    private String SECRET;

    @Value("${security.jwt.expiration-time}")
    private Long EXPIRATION;


    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user){
        // Implementation of generating JWT token
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("roles", user.getRoles())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSecretKey() )
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims =  Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();

    }

    public Set<Role> getRolesFromToken(String token) {
        Claims claims =  Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        List<String> roles = claims.get("roles" , List.class);

        return roles.stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }
}
