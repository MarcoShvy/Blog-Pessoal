package com.aceleramaker.blog.controller;

import com.aceleramaker.blog.dto.TemaDTO;
import com.aceleramaker.blog.service.TemaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TemaControllerTest {

    private MockMvc mockMvc;
    private TemaService temaService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        temaService = mock(TemaService.class);
        TemaController temaController = new TemaController(temaService);
        mockMvc = MockMvcBuilders.standaloneSetup(temaController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void deveCriarTemaERetornarStatus201() throws Exception {
        TemaDTO tema = new TemaDTO(null, "Tecnologia");
        TemaDTO temaCriado = new TemaDTO(1L, "Tecnologia");

        when(temaService.criarTema(any(TemaDTO.class))).thenReturn(temaCriado);

        mockMvc.perform(post("/api/temas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tema)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.descricao").value("Tecnologia"));
    }

    @Test
    void deveAtualizarTemaERetornarStatus200() throws Exception {
        TemaDTO temaAtualizado = new TemaDTO(1L, "Atualizado");

        when(temaService.atualizarTema(eq(1L), any(TemaDTO.class))).thenReturn(temaAtualizado);

        mockMvc.perform(put("/api/temas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(temaAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Atualizado"));
    }

    @Test
    void deveExcluirTemaERetornarMensagem() throws Exception {
        doNothing().when(temaService).excluirTema(1L);

        mockMvc.perform(delete("/api/temas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("Tema deletado com sucesso"));
    }

    @Test
    void deveListarTodosOsTemas() throws Exception {
        List<TemaDTO> temas = List.of(
                new TemaDTO(1L, "Tema 1"),
                new TemaDTO(2L, "Tema 2")
        );

        when(temaService.listarTodos()).thenReturn(temas);

        mockMvc.perform(get("/api/temas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }
}
