package com.aceleramaker.blog.security;

import com.aceleramaker.blog.model.User;
import com.aceleramaker.blog.model.enums.TipoUsuario;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    private final User user = new User(1L, "nomeTest", "usuarioTeste", "senha123", "Foto.png", TipoUsuario.COMUM, null);

    // Defina a variável de ambiente SECRET_KEY antes dos testes
    @BeforeAll
    static void setup() {
        System.setProperty("SECRET_KEY", "acelera-maker-key-blog-key");

    }

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
