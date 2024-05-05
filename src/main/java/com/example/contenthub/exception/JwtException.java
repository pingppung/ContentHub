package com.example.contenthub.exception;

public class JwtException extends RuntimeException{
    public JwtException(String message) {
        super(message);
    }

    public static JwtException InvalidJwtException() {
         return new JwtException("유효하지 않는 JWT 입니다.");
    }

    public static JwtException ExpiredJwtTokenException() {
        return new JwtException("만료된 JWT token 입니다.");
    }

}
