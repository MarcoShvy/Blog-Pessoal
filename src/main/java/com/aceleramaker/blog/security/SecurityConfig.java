package com.aceleramaker.blog.security;


import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private JwtFilter jwtFilter;
    private static final String COMUM = "COMUM";
    private static final String ADMIN = "ADMIN";
    private static final String LINK_MAP_POSTAGENS = "/api/postagens/**";
    private static final String LINK_MAP_TEMAS = "/api/temas";

    public SecurityConfig (JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http

                // SonarQube: CSRF desabilitado pois usamos JWT e a API é stateless
                .csrf(csrf -> csrf.disable()) //NOSONAR
                .headers(headers -> headers
                        .defaultsDisabled()
                        .frameOptions(frame -> frame.sameOrigin())
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos
                        .requestMatchers(
                                "/api/postagens/**",
                                "/api/temas",
                                "/api/usuarios",
                                "/api/usuarios/login",
                                "/swagger-ui/**",
                                "/v3/**",
                                "/swagger-ui.html",
                                "/webjars/**"
                        ).permitAll()

                        .requestMatchers(HttpMethod.DELETE, "/api/postagens/**").authenticated()
                        // Outros endpoints exigem autenticação
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}