package com.AceleraMaker.Blog.security;

import com.AceleraMaker.Blog.model.User;
import com.AceleraMaker.Blog.model.enums.TipoUsuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    private final User user = new User(1L, "nomeTest", "usuarioTeste", "senha123", "Foto.png", TipoUsuario.COMUM, null);

    @Test
    void deveGerarTokenValido() {
        String token = jwtService.generateToken(user);
        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    void deveExtrairUsernameDoToken() {
        String token = jwtService.generateToken(user);
        String usernameExtraido = jwtService.extractUsername(token);
        assertEquals(user.getUsuario(), usernameExtraido);
    }

    @Test
    void deveDetectarTokenInvalido() {
        String tokenInvalido = "token.invalido.aqui";
        assertFalse(jwtService.isTokenValid(tokenInvalido));
    }
}
