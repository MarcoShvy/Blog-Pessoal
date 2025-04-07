package com.AceleraMaker.Blog.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.servlet.Filter;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class SecurityConfigTest {

    private JwtFilter jwtFilter;
    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        jwtFilter = mock(JwtFilter.class);
        securityConfig = new SecurityConfig(jwtFilter);
    }

    @Test
    void testPasswordEncoder() {
        assertNotNull(securityConfig.passwordEncoder());
        assertTrue(securityConfig.passwordEncoder().matches("123456", securityConfig.passwordEncoder().encode("123456")));
    }

    @Test
    void testAuthenticationManager() throws Exception {
        AuthenticationConfiguration config = mock(AuthenticationConfiguration.class);
        AuthenticationManager manager = mock(AuthenticationManager.class);

        when(config.getAuthenticationManager()).thenReturn(manager);

        assertEquals(manager, securityConfig.authenticationManager(config));
    }
}

