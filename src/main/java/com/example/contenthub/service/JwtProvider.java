package com.example.contenthub.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.auth0.jwt.algorithms.Algorithm;
import com.example.contenthub.entity.User;

import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Collection;

//JwtProvider를 통해 JWT를 생성하고 검증
@Service
public class JwtProvider {

    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    // 토큰 유효시간 30분
    private long EXPIRATION_TIME = 30 * 60 * 1000L;

    /**
     * @param token          jwt 토큰
     * @param claimsResolver jwt 토큰으로 부터 어떤 정보를 추출할 지 지정하는 함수
     * @param <T>            토큰으로 부터 추출한 정보의 타입
     * @return 토큰으로 부터 추출한 정보
     */

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 토큰으로부터 모든 정보를 가져온다
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(SECRET_KEY) /* jwt가 중간에 변경되지 않았는 지 확인하기 위한 서명키 */
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // public String generateToken(Long userId, String username) {
    // return Jwts.builder()
    // .setSubject(String.valueOf(userId))
    // .claim("username", username)
    // .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
    // .signWith(Algorithm.HMAC512(SECRET_KEY))
    // .compact();
    // }

    public String extractUsername(String token) {
        return (String) extractClaim(token, claims -> claims.get("username"));
        // Claims claims = extractAllClaims(token); // 토큰에서 모든 클레임 추출
        // return (String) claims.get("username");
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(
                        "wpqkfwpqkfwpqkfwpqkfwpqkfwpqkfwpqkfwpqkfwpqkfwpqkfwpqkfwpqkfwpqkfwpqkfwpqkfwpqkfwpqkfwpqkf"
                                .getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(SECRET_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        String username = extractUsername(token);

        User principal = new User();

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);

    }

    // 토큰이 유효한지 확인
    public boolean isValidToken(String token) {
        try {
            // 서명이 올바르지 않으면 SignatureException 발생
            extractAllClaims(token); // parseClaimsJws에서 토큰의 서명을 검증
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰이 만료되었는 지 확인
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 토큰으로부터 토큰 만료일을 가져온다.
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}