package com.example.contenthub.controller;

import com.example.contenthub.entity.User;
import com.example.contenthub.exception.UserException;
import com.example.contenthub.service.AuthService;
import com.example.contenthub.service.JwtService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    @Autowired
    private final AuthService authService;
    @Autowired
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam User user) throws UserException {
        System.out.println("user " + user.getUsername() + " " + user.getUsername());
        try {
            // JwtDto jwtDto = authService.authenticate(user);
            return ResponseEntity.ok(user);
        } catch (UserException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<String> SignUp(@RequestBody User user) throws UserException {
        try {
            System.out.println("user sign " + user.getUsername() + " " + user.getUsername());
            authService.saveUser(user);
            return ResponseEntity.ok("User saved successfully");
        } catch (UserException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    // 인증이나 권한이 필요한 부분에서만 mapping한거라서 그냥 보내줘도 되지 않을까
    @GetMapping("/auth/verifyToken")
    public ResponseEntity<Object> verifyToken(HttpServletRequest request) throws UserException {
        String jwtToken = request.getHeader("Authorization");
        System.out.println(jwtToken);
        Map<String, String> userInfo = jwtService.extractUserInfo(jwtToken);
        System.out.println(userInfo);
        return ResponseEntity.ok(userInfo);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/auth/verifyAuth")
    public ResponseEntity<String> verifyADMIN(HttpServletRequest request) throws UserException {
        String jwtToken = request.getHeader("Authorization");
        System.out.println("누구?");
        return ResponseEntity.ok("허락한다");
    }

    // 예외 처리 핸들러
    @ExceptionHandler(UserException.class)
    public ResponseEntity<String> handleUserException(UserException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

}
