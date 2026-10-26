package com.example.contenthub.auth;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse.UserInfo> getCurrentUser() {
        return ResponseEntity.ok(authService.getCurrentUser());
    }

}

// 아이디 중복
// → 400 또는 409

// 로그인 실패
// → 401

// 권한 부족
// → 403

// 서버 자체 오류
// → 500
