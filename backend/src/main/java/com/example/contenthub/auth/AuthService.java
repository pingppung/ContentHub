package com.example.contenthub.auth;

import com.example.contenthub.common.exception.UserException;
import com.example.contenthub.user.Role;
import com.example.contenthub.user.User;
import com.example.contenthub.user.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    // private final TokenProvider tokenProvider;

    @Transactional
    public void signup(SignupRequest request) {

        // 이미 존재하는 사용자인 경우 예외 발생
        if (userRepository.existsByUserId(request.userId())) {
            throw UserException.duplicateUserException();
        }

        // Set<RoleType> roles = new HashSet<>();
        // roles.add(RoleType.ROLE_USER); // enum 타입으로 ROLE_USER 추가

        User user = User.builder().userId(request.userId())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);
    }

    public String getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || authentication instanceof AnonymousAuthenticationToken
                || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof String userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        return userId;
    }

    @Transactional
    public AuthResponse.UserInfo getCurrentUser() {
        User user = userRepository.findByUserId(getAuthenticatedUser())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "사용자를 찾을 수 없습니다."));
        return new AuthResponse.UserInfo(user.getUserId(), user.getRole().name());
    }
}
