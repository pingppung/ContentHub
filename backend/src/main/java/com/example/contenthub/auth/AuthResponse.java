package com.example.contenthub.auth;

public class AuthResponse {

    public record Login(
        String accessToken
    ) {}

    public record UserInfo(
        String userId,
        String role
    ) {}
}