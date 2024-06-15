package com.example.contenthub.service;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.example.contenthub.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

        public Map<String, String> extractUserInfo(String token) {
                String username = JWT.decode(extractToken(token)).getClaim("username").asString();
                String role = JWT.decode(extractToken(token)).getClaim("role").asString();

                Map<String, String> userInfo = new HashMap<>();
                userInfo.put("username", username);
                userInfo.put("role", role);

                return userInfo;
        }
}