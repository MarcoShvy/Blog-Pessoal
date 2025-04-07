package com.aceleramaker.blog.controller;

import com.aceleramaker.blog.dto.UserDTO;
import com.aceleramaker.blog.model.User;
import com.aceleramaker.blog.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);

        UserController userController = new UserController(userService);
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

        Mockito.when(userService.cadastrarUsuario(Mockito.any())).thenReturn(Optional.of(user));
        Mockito.when(userService.toDTO(Mockito.any())).thenReturn(dto);

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
}
