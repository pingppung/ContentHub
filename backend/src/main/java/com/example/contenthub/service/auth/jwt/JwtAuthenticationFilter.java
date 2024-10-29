package com.example.contenthub.service.auth.jwt;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.contenthub.service.auth.principal.PrincipalDetails;
import com.example.contenthub.entity.User;
import com.example.contenthub.utils.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//로그인 요청해서 username, password post 전송하면
//usernamepasswordauthenticationfilter가 작동
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    /// auth/login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        log.info("JwtAuthenticationFilter : 로그인 시도 중");
        // 1. username과 password를 받아서
        try {
            // request.getInputStream() => username과 password가 담겨져있음
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);

            // 예시
            // 토큰 생성 - 원래는 로그인 할 때 자동으로 만들어지게 할거임
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    user.getUsername(), user.getPassword());
            // PrincipalDetailsService의 loadUserByUserName()함수가 실행된 후 정상이면 authentication이
            // 리턴
            // authentication => 로그인한 정보가 담김
            // 토큰을 통해 로그인 시도를 해보고 정상적으로 되면 authentication이 생성
            // DB에 있는 username과 password가 일치한다
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            log.info("토큰 생성 완료!");

            // 로그인 정상적으로 되었다는 뜻
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            log.info("로그인 완료됨 : " + principalDetails.getUser().getUsername());
            // authentication 객체가 session영역에 저장해야하고 그 방법은 return 해주면 됨
            // 리턴의 이유는 권한 관리를 security가 대신 해주기 때문에 편하려고 하는거임
            // 굳이 JWT 토큰을 사용하면서 세션을 만들 이유가 없음. 근데 단지 권한 처리 때문에 session넣어줌
            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // attemptAuthentication 실행 후 인증이 정상적으로 되면 successfulAuthentication 함수가 실행됨
    // JWT 토큰을 만들어서 request요청한 사용자에게 JWT 토큰을 response해주면 됨.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        log.info("JWT 토큰 생성");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        String jwtToken = tokenProvider.generateToken(principalDetails);

        response.addHeader("Authorization", "Bearer " + jwtToken);
    }
}
