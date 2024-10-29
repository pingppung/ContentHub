package com.example.contenthub.config;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.example.contenthub.service.auth.principal.PrincipalDetailsService;
import com.example.contenthub.service.auth.jwt.JwtAuthenticationFilter;
import com.example.contenthub.service.auth.jwt.JwtAuthorizationFilter;
import com.example.contenthub.repository.UserRepository;
import com.example.contenthub.utils.TokenProvider;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@Slf4j
@EnableWebSecurity // Spring Security filter가 spring filterchain에 등록
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final CorsFilter corsFilter;
    private final PrincipalDetailsService principalDetailsService;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    @Value("${jwt.secret.key}")
    private String SECRET_KEY;
    // 사용자가 제공한 비밀번호를 암호화하여 저장하고, 인증 시 저장된 비밀번호와 사용자가 제공한 비밀번호를 비교하여 일치 여부를 확인
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder sharedObject = http.getSharedObject(AuthenticationManagerBuilder.class);

        sharedObject.userDetailsService(this.principalDetailsService);
        AuthenticationManager authenticationManager = sharedObject.build();

        http.authenticationManager(authenticationManager);
        http.csrf(CsrfConfigurer::disable)
                .httpBasic(httpBasic -> httpBasic.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .addFilter(corsFilter) // 1. 컨트롤러에 @CrossOrigin 하는 방법 - 인증 X, 2. 시큐리티 필터에 등록 - 인증O
                .addFilter(new JwtAuthenticationFilter(authenticationManager, tokenProvider))
                .addFilter(
                        new JwtAuthorizationFilter(authenticationManager, userRepository, tokenProvider, SECRET_KEY));
        http.sessionManagement( // JWT 방식은 세션저장을 사용하지 않기 때문에 꺼주기.
                sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER") // 인증뿐만 아니라 권한이 있는 사람만 들어올 수 있다.
                .requestMatchers("/user/**").authenticated() // 해당 주소로 들어오면 인증이 필요하다.
                .anyRequest().permitAll());
        http.formLogin(formLogin -> formLogin
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .loginProcessingUrl("/auth/login") // 주소가 호출되면 시큐리티가 낚아채서 대신 로그인 진행
                .successHandler(successHandler())
                .failureHandler(customFailureHandler())
                // .defaultSuccessUrl("/auth/login")
                .permitAll());
        http.logout(logout -> logout
                .permitAll());
        //
        return http.build();
    }

    @Bean
    protected AuthenticationSuccessHandler successHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                    Authentication authentication) throws IOException {
                log.info("로그인 성공");
                log.info("requset", request);

                try {
                    response.sendRedirect("http://localhost:3000/");
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }

        };
    }


    @Bean
    public CustomAuthenticationFailureHandler customFailureHandler() {
        return new CustomAuthenticationFailureHandler(); // Custom 핸들러를 빈으로 등록
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);
        // exposed-headers 설정
        config.setExposedHeaders(Arrays.asList("Access-Control-Allow-Headers",
                "Authorization, x-xsrf-token, Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, " +
                        "Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}