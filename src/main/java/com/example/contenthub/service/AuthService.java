package com.example.contenthub.service;

import com.example.contenthub.constants.Role;
import com.example.contenthub.dto.JwtDto;
import com.example.contenthub.entity.User;
import com.example.contenthub.exception.UserException;
import com.example.contenthub.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//인증 정보를 처리하고, 인증이 성공하면 JwtService를 통해 JWT를 생성
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void saveUser(User request) throws UserException {
        User existingUser = userRepository.findByUsername(request.getUsername());
        if (existingUser != null) {
            // 이미 존재하는 사용자인 경우 예외 발생
            throw UserException.duplicateUserException();
        }
        User user = User.builder().username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("ROLE_USER")
                .build();
        userRepository.save(user);
        // System.out.println(user.getId());
    }

    // public JwtDto authenticate(User request) throws UserException {
    // User user = userRepository.findByUsername(request.getUsername());
    // if (user == null || !passwordEncoder.matches(request.getPassword(),
    // user.getPassword())) {
    // throw UserException.invalidUserException();
    // }
    // // String token = jwtService.generateToken(user.getId(), user.getUsername());
    // return new JwtDto(token);
    // }

}