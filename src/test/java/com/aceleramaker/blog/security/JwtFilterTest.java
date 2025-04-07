package com.aceleramaker.blog.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

    @Mock private JwtService jwtService;
    @Mock private CustomUserDetails userDetailsService;
    @Mock private FilterChain filterChain;
    @Mock private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private JwtFilter jwtFilter;

    @Test
    void deveAutenticarQuandoTokenValido() throws Exception {
        String token = "tokenValido";
        String username = "usuarioTeste";

        // Cria UserDetails usando o builder do Spring Security
        UserDetails userDetails = User.withUsername(username)
                .password("senhaTeste")
                .authorities("ROLE_USER")
                .build();

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(token)).thenReturn(true);

        jwtFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(username, SecurityContextHolder.getContext().getAuthentication().getName());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void naoDeveAutenticarQuandoTokenInvalido() throws Exception {
        SecurityContextHolder.clearContext(); // garantir contexto limpo

        when(request.getHeader("Authorization")).thenReturn("Bearer tokenInvalido");
        when(jwtService.isTokenValid("tokenInvalido")).thenReturn(false);

        jwtFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication()); // agora deve passar
        verify(filterChain).doFilter(request, response);
    }
}