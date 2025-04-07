package com.aceleramaker.blog.security;

import com.aceleramaker.blog.model.User;
import com.aceleramaker.blog.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomUserDetailsTest {

    private UserRepository userRepository;
    private CustomUserDetails customUserDetails;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        customUserDetails = new CustomUserDetails(userRepository);
    }

    @Test
    void deveCarregarUsuarioPorNomeDeUsuario() {
        // Arrange
        User user = new User();
        user.setUsuario("admin");
        user.setSenha("123456");
        user.setTipoUsuario(com.aceleramaker.blog.model.enums.TipoUsuario.ADMIN);

        when(userRepository.findByUsuario("admin")).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = customUserDetails.loadUserByUsername("admin");

        // Assert
        assertEquals("admin", userDetails.getUsername());
        assertEquals("123456", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // Arrange
        when(userRepository.findByUsuario("inexistente")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetails.loadUserByUsername("inexistente");
        });
    }
}
