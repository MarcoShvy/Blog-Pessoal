package com.AceleraMaker.Blog.security;


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

    public SecurityConfig (JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
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
                                "/api/usuarios/login",
                                "/api/usuarios"
                        ).permitAll()

                        // Leitura pública
                        .requestMatchers(HttpMethod.GET, "/api/postagens/**", "/api/temas/**").permitAll()

                        // Criação, edição e deleção: permitido para USER e ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/postagens/**", "/api/temas/**").hasAnyRole("COMUM", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/postagens/**", "/api/temas/**").hasAnyRole("COMUM", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/postagens/**", "/api/temas/**").hasAnyRole("COMUM", "ADMIN")

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