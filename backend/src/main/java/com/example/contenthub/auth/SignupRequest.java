package com.example.contenthub.auth;

public record SignupRequest(
        String userId,
        String password
) {}