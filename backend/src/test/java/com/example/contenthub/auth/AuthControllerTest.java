package com.example.contenthub.auth;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Base64;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.contenthub.user.Role;
import com.example.contenthub.user.User;
import com.example.contenthub.user.UserRepository;

class AuthControllerTest {
    private final UserRepository users = mock(UserRepository.class);
    private final TokenProvider tokens = new TokenProvider(Base64.getEncoder().encodeToString(new byte[32]));
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        AuthService service = new AuthService(users, mock(PasswordEncoder.class));
        mvc = MockMvcBuilders.standaloneSetup(new AuthController(service))
                .addFilters(new JwtAuthorizationFilter(tokens)).build();
    }

    @AfterEach
    void clearAuthentication() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void meReturnsUserIdAndStoredRoleWithoutPassword() throws Exception {
        User user = User.builder().userId("test-user").password("stored-hash").role(Role.ROLE_USER).build();
        when(users.findByUserId("test-user")).thenReturn(Optional.of(user));
        String token = tokens.generateToken(new PrincipalDetails(user));

        mvc.perform(get("/auth/me").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("test-user"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void missingAndInvalidTokensReturnUnauthorized() throws Exception {
        mvc.perform(get("/auth/me")).andExpect(status().isUnauthorized());
        mvc.perform(get("/auth/me").header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deletedUserCannotGetProfileWithPreviouslyIssuedToken() throws Exception {
        User user = User.builder().userId("deleted-user").password("stored-hash").role(Role.ROLE_USER).build();
        when(users.findByUserId("deleted-user")).thenReturn(Optional.empty());
        mvc.perform(get("/auth/me").header("Authorization", "Bearer " + tokens.generateToken(new PrincipalDetails(user))))
                .andExpect(status().isUnauthorized());
    }
}
