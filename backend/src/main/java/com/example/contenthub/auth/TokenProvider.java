package com.example.contenthub.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.contenthub.common.exception.JwtException;

import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;
import java.security.Key;

@Component
public class TokenProvider {

    private static final String AUTHORIZATION_KEY = "auth";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24; // 1일
    private final Key key;

    public TokenProvider(@Value("${jwt.secret.key}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(PrincipalDetails principalDetails) {

        String authorities = principalDetails.getAuthorities().stream()
                .map(authority -> Objects.requireNonNull(authority, "authority is required").getAuthority())
                .collect(Collectors.joining(","));

        Date now = new Date();

        return Jwts.builder()
                .setSubject(principalDetails.getUsername())
                .claim("id", principalDetails.getUser().getId())
                // .claim("username", principalDetails.getUser().getUserId())
                .claim(AUTHORIZATION_KEY, authorities)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
            throw new JwtException("유효하지 않은 토큰입니다.");
        }
    }    
    
    
    // public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    //     final Claims claims = getAllClaimsFromToken(token);
    //     return claimsResolver.apply(claims);
    // }

    // private Claims getAllClaimsFromToken(String token) {
    //     return Jwts.parserBuilder()
    //             .setSigningKey(key)
    //             .build()
    //             .parseClaimsJws(token)
    //             .getBody();
    // }
    
    // public String getUserIdFromToken(String token) {
    //     return getClaimFromToken(token, Claims::getSubject);
    // }

    // public ResponseDTO<UserDetails> getUserDetailsFromToken(String token) {
    //     Claims claims = getAllClaimsFromToken(token);
    //     if (claims.get(AUTHORIZATION_KEY) == null) {
    //         throw new RuntimeException("권한 정보가 없는 토큰입니다.");
    //     }
    //     Collection<? extends GrantedAuthority> authorities = Arrays
    //             .stream(claims.get(AUTHORIZATION_KEY).toString().split(","))
    //             .map(SimpleGrantedAuthority::new)
    //             .collect(Collectors.toList());
    //     String username = getUserIdFromToken(token);
    //     UserDetails userDetails = new User(username, "", authorities);
    //     return ResponseDTO.<UserDetails>builder()
    //             .status(200)
    //             .message("User details retrieved successfully")
    //             .data(userDetails)
    //             .build();
    // }

    // private boolean isTokenExpired(String token) {
    //     return extractExpiration(token).before(new Date());
    // }

    // private Date extractExpiration(String token) {
    //     return getClaimFromToken(token, Claims::getExpiration);
    // }

}
