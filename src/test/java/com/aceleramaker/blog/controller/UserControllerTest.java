package com.aceleramaker.blog.controller;

import com.aceleramaker.blog.dto.UserDTO;
import com.aceleramaker.blog.model.User;
import com.aceleramaker.blog.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;
    @Mock
    private UserService userService;


    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);

        UserController userController = new UserController(userService); //NOSONAR
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void deveCadastrarUsuarioComSucesso() throws Exception {
        User user = new User(); // configure o objeto
        user.setUsuario("joao");
        user.setNome("João da Silva");

        UserDTO dto = new UserDTO();
        dto.setUsuario("joao");
        dto.setNome("João da Silva");

        when(userService.cadastrarUsuario(Mockito.any())).thenReturn(Optional.of(user));
        when(userService.toDTO(Mockito.any())).thenReturn(dto);

        mockMvc.perform(post("/api/usuarios") // <- com barra se estiver no controller
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "usuario": "joao",
                      "nome": "João da Silva",
                      "senha": "123456"
                    }
                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.usuario").value("joao"))
                .andExpect(jsonPath("$.nome").value("João da Silva"));
    }

    @Test
    void testDeletarComSucesso() {
        Long userId = 1L;

        // Nenhuma exceção é lançada
        lenient().doNothing().when(userService).deletarUsuario(userId);

        ResponseEntity<Map<String, String>> response = userController.deletarUsuario(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        lenient().doNothing().when(userService).deletarUsuario(userId);
        assertEquals("Usuário deletado com sucesso", Objects.requireNonNull(response.getBody()).get("mensagem"));
    }

}
