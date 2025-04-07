package com.aceleramaker.blog.service;

import com.aceleramaker.blog.dto.UserDTO;
import com.aceleramaker.blog.dto.UsuarioLogin;
import com.aceleramaker.blog.exception.user.AutenticacaoException;
import com.aceleramaker.blog.exception.user.UsuarioJaCadastradoException;
import com.aceleramaker.blog.exception.user.UsuarioNaoEncontradoException;
import com.aceleramaker.blog.model.User;
import com.aceleramaker.blog.model.enums.TipoUsuario;
import com.aceleramaker.blog.repository.UserRepository;
import com.aceleramaker.blog.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    private UserRepository userRepository;
    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        jwtService = mock(JwtService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserService(userRepository, jwtService, passwordEncoder);
    }

    @Test
    void deveCadastrarUsuarioComSucesso() {
        User user = new User();
        user.setUsuario("marco");
        user.setSenha("123");

        when(userRepository.existsByUsuario("marco")).thenReturn(false);
        when(passwordEncoder.encode("123")).thenReturn("senhaCodificada");
        when(userRepository.save(user)).thenReturn(user);

        Optional<User> resultado = userService.cadastrarUsuario(user);

        assertTrue(resultado.isPresent());
        verify(userRepository).save(user);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioJaCadastrado() {
        User user = new User();
        user.setUsuario("marco");

        when(userRepository.existsByUsuario("marco")).thenReturn(true);

        assertThrows(UsuarioJaCadastradoException.class, () -> userService.cadastrarUsuario(user));
    }

    @Test
    void deveAutenticarUsuarioComSucesso() {
        User user = new User();
        user.setUsuario("marco");
        user.setSenha("senhaCodificada");

        UsuarioLogin login = new UsuarioLogin("marco", "123", null);

        when(userRepository.findByUsuario("marco")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123", "senhaCodificada")).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("token123");

        Optional<UsuarioLogin> resultado = userService.autenticarUsuario(login);

        assertTrue(resultado.isPresent());
        assertEquals("token123", resultado.get().token());
    }

    @Test
    void deveLancarExcecaoAoAutenticarUsuarioInvalido() {
        UsuarioLogin login = new UsuarioLogin("marco", "123", null);

        when(userRepository.findByUsuario("marco")).thenReturn(Optional.empty());

        assertThrows(AutenticacaoException.class, () -> userService.autenticarUsuario(login));
    }

    @Test
    void deveAtualizarUsuarioComSucesso() {
        Long userId = 1L;

        User userExistente = new User();
        userExistente.setId(userId);
        userExistente.setUsuario("marco");

        UserDTO novoUsuario = new UserDTO(userId, "Marco Atualizado", "marco", "foto.jpg", "novaSenha", TipoUsuario.COMUM);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userExistente));
        when(userRepository.findByUsuario("marco")).thenReturn(Optional.of(userExistente));
        when(passwordEncoder.encode("novaSenha")).thenReturn("senhaNovaCodificada");
        when(userRepository.save(any(User.class))).thenReturn(userExistente);

        Optional<User> resultado = userService.atualizarUsuario(userId, novoUsuario);

        assertTrue(resultado.isPresent());
        assertEquals("Marco Atualizado", resultado.get().getNome());
        assertEquals("senhaNovaCodificada", resultado.get().getSenha());
    }

    @Test
    void deveLancarExcecaoAoAtualizarUsuarioInexistente() {
        Long userId = 1L;
        UserDTO novoUsuario = new UserDTO(userId, "Novo Nome", "novo", "foto.jpg", "senha", TipoUsuario.COMUM);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class, () -> userService.atualizarUsuario(userId, novoUsuario));
    }

    @Test
    void deveDeletarUsuarioComSucesso() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.deletarUsuario(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    void deveLancarExcecaoAoDeletarUsuarioInexistente() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class, () -> userService.deletarUsuario(userId));
    }
}
