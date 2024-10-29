package com.example.contenthub.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.example.contenthub.service.auth.principal.PrincipalDetails;
import com.example.contenthub.dto.ResponseDTO;
import com.example.contenthub.exception.JwtException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.security.Key;

@Component
public class TokenProvider {

    private static final String AUTHORIZATION_KEY = "auth";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24; // 1일
    // private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 *
    // 7; // 7일
    private Key key;

    public TokenProvider(@Value("${jwt.secret.key}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(PrincipalDetails principalDetails) {
        String authorities = principalDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();

        return Jwts.builder()
                .setSubject(principalDetails.getUsername())
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
                .claim(AUTHORIZATION_KEY, authorities)
                .claim("id", principalDetails.getUser().getId())
                .claim("username", principalDetails.getUser().getUsername())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // username값
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public ResponseDTO<UserDetails> getUserDetailsFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        if (claims.get(AUTHORIZATION_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }
        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(claims.get(AUTHORIZATION_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        String username = getUsernameFromToken(token);
        UserDetails userDetails = new User(username, "", authorities);

        return ResponseDTO.<UserDetails>builder()
                .status(200)
                .message("User details retrieved successfully")
                .data(userDetails)
                .build();
    }

    // claim에서 특정값 가져오기
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // 토큰 복호화
    private Claims getAllClaimsFromToken(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) throws JwtException {
        try {
            Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException("유효하지 않은 토큰입니다");
        }
    }

    // 토큰이 만료되었는 지 확인
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 토큰으로부터 토큰 만료일을 가져온다.
    private Date extractExpiration(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

}