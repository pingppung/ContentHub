package com.example.contenthub.auth;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.example.contenthub.common.exception.JwtException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null
                || !authorizationHeader.startsWith("Bearer ")) {

            chain.doFilter(request, response);
            return;
        }

        try {

            String token = authorizationHeader.substring(7);

            Claims claims = tokenProvider.parseToken(token);

            String userId = claims.getSubject();
            String authority = claims.get("auth", String.class);
            if (userId == null || userId.isBlank() || authority == null || authority.isBlank()) {
                throw new JwtException("토큰에 사용자 또는 권한 정보가 없습니다.");
            }

            Collection<? extends GrantedAuthority> authorities = Arrays.stream(authority.split(","))
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.debug("JWT 인증 성공 userId={}", userId);

        } catch (AuthenticationException | JwtException e) {
            SecurityContextHolder.clearContext();
            log.debug("JWT 인증 실패: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 인증 토큰입니다.");
            return;
        }

        chain.doFilter(request, response);
    }

}
