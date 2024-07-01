package com.example.contenthub.controller;

import com.example.contenthub.dto.ResponseDTO;
import com.example.contenthub.entity.User;
import com.example.contenthub.exception.UserException;
import com.example.contenthub.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam User user) throws UserException {
        try {
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

    @GetMapping("/auth/verifyToken")
    public ResponseEntity<Object> verifyToken(@RequestHeader("Authorization") String tokenHeader) throws UserException {
        authService.validateAuthorizationHeader(tokenHeader);
        String jwt = authService.extractToken(tokenHeader);
        ResponseDTO<UserDetails> res = authService.getUserInfo(jwt);
        return ResponseEntity.ok(res);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/auth/verifyAuth")
    public ResponseEntity<String> verifyADMIN(HttpServletRequest request) throws UserException {
        // String jwtToken = request.getHeader("Authorization");
        return ResponseEntity.ok("권한 test 허락한다");
    }

    // 예외 처리 핸들러
    @ExceptionHandler(UserException.class)
    public ResponseEntity<String> handleUserException(UserException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

}
