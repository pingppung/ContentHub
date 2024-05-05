package com.example.contenthub.service;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
//JwtProvider를 통해 JWT를 생성하고 검증
@Service
public class JwtService {

    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    // 토큰 유효시간 30분
    private long EXPIRATION_TIME = 30 * 60 * 1000L;
      
    public String generateToken(Long userId, String username) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("username", username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
   public String isValidToken(String jwtToken) {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();

            System.out.println("Username: " + claims.get("username"));
            return (String) claims.get("username");
        
    }
}