package com.AceleraMaker.Blog.service;

import com.AceleraMaker.Blog.dto.TemaDTO;
import com.AceleraMaker.Blog.exception.tema.TemaNaoEncontradoException;
import com.AceleraMaker.Blog.model.Tema;
import com.AceleraMaker.Blog.repository.TemaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TemaServiceTest {

    private TemaRepository temaRepository;
    private TemaService temaService;

    @BeforeEach
    void setUp() {
        temaRepository = mock(TemaRepository.class);
        temaService = new TemaService(temaRepository);
    }

    @Test
    void deveCriarTema() {
        TemaDTO dto = new TemaDTO();
        dto.setDescricao("Tecnologia");

        Tema temaSalvo = new Tema();
        temaSalvo.setId(1L);
        temaSalvo.setDescricao("Tecnologia");

        when(temaRepository.save(any(Tema.class))).thenReturn(temaSalvo);

        TemaDTO resultado = temaService.criarTema(dto);

        assertEquals(1L, resultado.getId());
        assertEquals("Tecnologia", resultado.getDescricao());
    }

    @Test
    void deveAtualizarTema() {
        Long id = 1L;
        Tema temaExistente = new Tema();
        temaExistente.setId(id);
        temaExistente.setDescricao("Antiga");

        TemaDTO dtoAtualizado = new TemaDTO();
        dtoAtualizado.setDescricao("Nova descrição");

        when(temaRepository.findById(id)).thenReturn(Optional.of(temaExistente));
        when(temaRepository.save(any(Tema.class))).thenAnswer(i -> i.getArgument(0));

        TemaDTO resultado = temaService.atualizarTema(id, dtoAtualizado);

        assertEquals("Nova descrição", resultado.getDescricao());
    }

    @Test
    void deveLancarExcecao_aoAtualizarTemaInexistente() {
        Long id = 99L;
        TemaDTO dto = new TemaDTO();
        dto.setDescricao("Novo");

        when(temaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(TemaNaoEncontradoException.class, () -> temaService.atualizarTema(id, dto));
    }

    @Test
    void deveListarTodosTemas() {
        Tema tema1 = new Tema();
        tema1.setId(1L);
        tema1.setDescricao("Tecnologia");

        Tema tema2 = new Tema();
        tema2.setId(2L);
        tema2.setDescricao("Educação");

        when(temaRepository.findAll()).thenReturn(Arrays.asList(tema1, tema2));

        List<TemaDTO> resultado = temaService.listarTodos();

        assertEquals(2, resultado.size());
        assertEquals("Tecnologia", resultado.get(0).getDescricao());
        assertEquals("Educação", resultado.get(1).getDescricao());
    }

    @Test
    void deveExcluirTema() {
        Long id = 1L;
        Tema tema = new Tema();
        tema.setId(id);

        when(temaRepository.findById(id)).thenReturn(Optional.of(tema));

        temaService.excluirTema(id);

        verify(temaRepository, times(1)).delete(tema);
    }

    @Test
    void deveLancarExcecao_aoExcluirTemaInexistente() {
        Long id = 42L;
        when(temaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(TemaNaoEncontradoException.class, () -> temaService.excluirTema(id));
    }
}
