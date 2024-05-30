package com.example.contenthub.controller;

import com.example.contenthub.dto.JwtDto;
import com.example.contenthub.entity.User;
import com.example.contenthub.exception.UserException;
import com.example.contenthub.service.AuthService;
import com.example.contenthub.service.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    private final AuthService authService;
    private final JwtProvider jwtService;

    @Autowired
    public AuthController(AuthService authService, JwtProvider jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> authenticate(@RequestBody User user) throws UserException {
        try {
            JwtDto jwtDto = authService.authenticate(user);
            return ResponseEntity.ok(jwtDto);
        } catch (UserException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<String> SignUp(@RequestBody User user) throws UserException {
        try {
            authService.saveUser(user);
            return ResponseEntity.ok("User saved successfully");
        } catch (UserException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @GetMapping("/auth/verifyToken")
    public ResponseEntity<String> verifyToken(HttpServletRequest request) throws UserException {
        String token = request.getHeader("Authorization").substring(7);
        if (jwtService.isValidToken(token)) {
            return ResponseEntity.ok(jwtService.extractUsername(token));
        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 유효하지 않습니다");
    }

    public String getMethodName(@RequestParam String param) {
        return new String();
    }

    // 예외 처리 핸들러
    @ExceptionHandler(UserException.class)
    public ResponseEntity<String> handleUserException(UserException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

}
