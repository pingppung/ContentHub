package com.example.contenthub.common.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.example.contenthub.auth.PrincipalDetailsService;
import com.example.contenthub.auth.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

class SecurityConfigTest {

    @Test
    void securityFilterChainStartsWithoutBuildingSharedAuthenticationBuilderTwice() {
        new WebApplicationContextRunner()
                .withUserConfiguration(SecurityConfig.class, TestDependencies.class)
                .run(context -> {
                    assertThat(context).hasNotFailed();
                    assertThat(context).hasSingleBean(AuthenticationManager.class);
                    assertThat(context).hasSingleBean(SecurityFilterChain.class);
                });
    }

    @TestConfiguration(proxyBeanMethods = false)
    @EnableWebMvc
    static class TestDependencies {
        @Bean
        PrincipalDetailsService principalDetailsService() {
            return mock(PrincipalDetailsService.class);
        }

        @Bean
        TokenProvider tokenProvider() {
            return mock(TokenProvider.class);
        }

        @Bean
        ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }
}
