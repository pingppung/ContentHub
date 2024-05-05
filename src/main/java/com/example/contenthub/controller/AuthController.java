package com.example.contenthub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.contenthub.dto.JwtDto;
import com.example.contenthub.entity.User;
import com.example.contenthub.exception.JwtException;
import com.example.contenthub.exception.UserException;
import com.example.contenthub.service.AuthService;
import com.example.contenthub.service.JwtService;

import jakarta.servlet.http.HttpServletRequest;



@RestController
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    @Autowired
    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }


    @PostMapping("/auth/login")
    public ResponseEntity<?> LoginIn(@RequestBody User user) throws UserException {
        try{
            JwtDto jwtDto = authService.loginUser(user);
            System.out.println(jwtDto);
            return ResponseEntity.ok(jwtDto);
        } catch(UserException e){
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
        String jwtToken = request.getHeader("Authorization").substring(7);
        String username = jwtService.isValidToken(jwtToken);
        return ResponseEntity.ok(username);

    }

    // 예외 처리 핸들러
    @ExceptionHandler(UserException.class)
    public ResponseEntity<String> handleUserException(UserException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
    
}
