package com.example.contenthub.service;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;

//JwtProvider를 통해 JWT를 생성하고 검증
@Service
@RequiredArgsConstructor
public class JwtService {
        private final JwtProvider jwtProvider;

        public Claims parseJwtToken(String authorizationHeader) {
                validationAuthorizationHeader(authorizationHeader); // (1)
                String token = extractToken(authorizationHeader); // (2)

                return Jwts.parser()
                                .setSigningKey("wpqkf") // (3)
                                .parseClaimsJws(token) // (4)
                                .getBody();
        }

        private void validationAuthorizationHeader(String header) {
                if (header == null || !header.startsWith("Bearer ")) {
                        throw new IllegalArgumentException();
                }
        }

        private String extractToken(String authorizationHeader) {
                return authorizationHeader.substring("Bearer ".length());
        }

        public String extractUsername(String token) {
                return JWT.decode(extractToken(token)).getClaim("username").asString();
                // return jwtProvider.extractUsername(extractToken(token));
                // return (String) extractClaim(token, claims -> claims.get("username"));
                // Claims claims = extractAllClaims(token); // 토큰에서 모든 클레임 추출
                // return (String) claims.get("username");
        }
}