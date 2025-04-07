package com.aceleramaker.blog.controller;

import com.aceleramaker.blog.dto.PostagemDTO;
import com.aceleramaker.blog.dto.PostagemResponseDTO;
import com.aceleramaker.blog.model.Postagem;
import com.aceleramaker.blog.service.PostagemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class PostagemControllerTest {

    private MockMvc mockMvc;
    private PostagemService postagemService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        postagemService = mock(PostagemService.class);
        PostagemController postagemController = new PostagemController();

        ReflectionTestUtils.setField(postagemController, "postagemService", postagemService);

        mockMvc = MockMvcBuilders.standaloneSetup(postagemController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void deveListarTodasAsPostagens() throws Exception {
        List<Postagem> postagens = List.of(new Postagem(), new Postagem());
        when(postagemService.listarTodas()).thenReturn(postagens);

        mockMvc.perform(get("/api/postagens/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void deveFiltrarPostagensPorAutorETema() throws Exception {
        List<Postagem> filtradas = List.of(new Postagem());
        List<PostagemResponseDTO> resposta = List.of(new PostagemResponseDTO());

        when(postagemService.filtrarPorUsuarioETema(1L, 2L)).thenReturn(filtradas);
        when(postagemService.toResponseDTOList(filtradas)).thenReturn(resposta);

        mockMvc.perform(get("/api/postagens/filtro")
                        .param("autor", "1")
                        .param("tema", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void deveCriarPostagem() throws Exception {
        PostagemDTO dto = new PostagemDTO();
        Postagem postagemCriada = new Postagem();
        PostagemResponseDTO responseDTO = new PostagemResponseDTO();
        responseDTO.setTitulo("Nova postagem");

        when(postagemService.criar(any(PostagemDTO.class))).thenReturn(postagemCriada);
        when(postagemService.toResponseDTO(postagemCriada)).thenReturn(responseDTO);

        mockMvc.perform(post("/api/postagens/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Nova postagem"));
    }

    @Test
    void deveAtualizarPostagem() throws Exception {
        PostagemDTO dto = new PostagemDTO();
        Postagem postagemAtualizada = new Postagem();
        PostagemResponseDTO responseDTO = new PostagemResponseDTO();
        responseDTO.setTitulo("Atualizada");

        when(postagemService.atualizar(eq(1L), any(PostagemDTO.class))).thenReturn(postagemAtualizada);
        when(postagemService.toResponseDTO(postagemAtualizada)).thenReturn(responseDTO);

        mockMvc.perform(put("/api/postagens/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Atualizada"));
    }

    @Test
    void deveDeletarPostagem() throws Exception {
        doNothing().when(postagemService).deletar(1L);

        mockMvc.perform(delete("/api/postagens/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("Postagem deletada com sucesso"));
    }

}
