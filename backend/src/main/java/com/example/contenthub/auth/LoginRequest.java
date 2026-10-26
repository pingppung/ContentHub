package com.example.contenthub.auth;

public record LoginRequest(
        String userId,
        String password
) {}