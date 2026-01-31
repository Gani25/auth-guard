package com.sprk.security_demo_project.service;

import com.sprk.security_demo_project.dto.AuthRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecretJwk;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${my.secret}")
    private String secretKey;


    public String getToken(Authentication authentication) {


        Map<String,Object> claims = new HashMap<>();

        // Username
        String username = authentication.getName();

        // Roles
        String roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        claims.put("roles", roles);

        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts
                .builder()
                .header()
                .add(Map.of("alg","HS256","type","jwt"))
                .and()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+1000*60*60)) // 1hour
                .signWith(getSignInKey())
                .compact();
    }



    public String extractUsername(String token) {

        return extractClaims(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    private <T> T extractClaims(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails){

        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));

    }

    private SecretKey getSignInKey() {

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);

    }
}
