package com.example.contenthub.service.auth;

import com.example.contenthub.dto.ResponseDTO;
import com.example.contenthub.entity.User;
import com.example.contenthub.exception.UserException;
import com.example.contenthub.repository.UserRepository;
import com.example.contenthub.utils.TokenProvider;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


//자격 증명 확인 후 유효성 검증, 로그인 / 회원가입 처리
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public void saveUser(User request) throws UserException {
        User existingUser = userRepository.findByUserId(request.getUserId());
        if (existingUser != null) {
            // 이미 존재하는 사용자인 경우 예외 발생
            throw UserException.duplicateUserException();
        }
        //Set<RoleType> roles = new HashSet<>();
        //roles.add(RoleType.ROLE_USER); // enum 타입으로 ROLE_USER 추가

        User user = User.builder().userId(request.getUserId())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("ROLE_USER")
                .build();
        userRepository.save(user);
    }

    public void validateAuthorizationHeader(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new IllegalArgumentException();
        }
    }

    public String extractToken(String authorizationHeader) {
        return authorizationHeader.substring("Bearer ".length());
    }

    public ResponseDTO<UserDetails> getUserInfo(String token) {
        return tokenProvider.getUserDetailsFromToken(token);
    }
}