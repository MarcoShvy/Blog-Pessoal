package com.aceleramaker.blog.service;

import com.aceleramaker.blog.dto.PostagemDTO;
import com.aceleramaker.blog.exception.postagem.PostagemNaoEncontradaException;
import com.aceleramaker.blog.exception.user.UsuarioNaoEncontradoException;
import com.aceleramaker.blog.model.Postagem;
import com.aceleramaker.blog.model.Tema;
import com.aceleramaker.blog.model.User;
import com.aceleramaker.blog.model.enums.TipoUsuario;
import com.aceleramaker.blog.repository.PostagemRepository;
import com.aceleramaker.blog.repository.TemaRepository;
import com.aceleramaker.blog.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class PostagemServiceTest {

    @Mock private PostagemRepository postagemRepository;
    @Mock private TemaRepository temaRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks private PostagemService postagemService;

    private User usuario;
    private Tema tema;
    private Postagem postagem;
    private PostagemDTO dto;

    @BeforeEach
    void setup() {
        usuario = new User();
        usuario.setId(1L);
        usuario.setUsuario("johndoe");
        usuario.setTipoUsuario(TipoUsuario.COMUM);
        usuario.setNome("John Doe");

        tema = new Tema();
        tema.setId(1L);
        tema.setDescricao("Tecnologia");

        postagem = new Postagem();
        postagem.setId(1L);
        postagem.setTitulo("Título");
        postagem.setTexto("Texto");
        postagem.setUsuario(usuario);
        postagem.setTema(tema);

        dto = new PostagemDTO();
        dto.setTitulo("Novo título");
        dto.setTexto("Novo texto");
        dto.setUsuarioId(1L);
        dto.setTemaId(1L);
    }

    @Test
    void deveCriarPostagemComSucesso() {
        when(userRepository.findById(dto.getUsuarioId())).thenReturn(Optional.of(usuario));
        when(temaRepository.findById(dto.getTemaId())).thenReturn(Optional.of(tema));
        when(postagemRepository.save(any(Postagem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Postagem criada = postagemService.criar(dto);

        assertEquals(dto.getTitulo(), criada.getTitulo());
        assertEquals(dto.getTexto(), criada.getTexto());
        assertEquals(usuario, criada.getUsuario());
        assertEquals(tema, criada.getTema());
    }

    @Test
    void deveLancarExcecaoAoCriarPostagemComUsuarioInexistente() {
        when(userRepository.findById(dto.getUsuarioId())).thenReturn(Optional.empty());
        assertThrows(UsuarioNaoEncontradoException.class, () -> postagemService.criar(dto));
    }

    @Test
    void deveAtualizarPostagemComSucesso_SeUsuarioForDono() {
        mockUsuarioAutenticado(usuario);

        when(postagemRepository.findById(1L)).thenReturn(Optional.of(postagem));
        when(userRepository.findByUsuario(anyString())).thenReturn(Optional.of(usuario));
        when(postagemRepository.save(any(Postagem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Postagem atualizada = postagemService.atualizar(1L, dto);

        assertEquals(dto.getTitulo(), atualizada.getTitulo());
        assertEquals(dto.getTexto(), atualizada.getTexto());
    }

    @Test
    void deveLancarExcecaoAoAtualizarPostagemSeNaoForDonoNemAdmin() {
        User outroUsuario = new User();
        outroUsuario.setId(2L);
        outroUsuario.setUsuario("intruso");
        outroUsuario.setTipoUsuario(TipoUsuario.COMUM);

        mockUsuarioAutenticado(outroUsuario);

        when(postagemRepository.findById(1L)).thenReturn(Optional.of(postagem));
        when(userRepository.findByUsuario(anyString())).thenReturn(Optional.of(outroUsuario));

        assertThrows(SecurityException.class, () -> postagemService.atualizar(1L, dto));
    }

    @Test
    void deveDeletarPostagem_SeForAdmin() {
        User admin = new User();
        admin.setId(3L);
        admin.setTipoUsuario(TipoUsuario.ADMIN);
        admin.setUsuario("admin");

        mockUsuarioAutenticado(admin); // simula usuário autenticado como admin

        when(postagemRepository.findById(1L)).thenReturn(Optional.of(postagem));
        when(userRepository.findByUsuario("admin")).thenReturn(Optional.of(admin));

        // A chamada real
        postagemService.deletar(1L);

        // Verifica se o método de deleção foi chamado corretamente
        verify(postagemRepository).deleteById(1L);
    }

    @Test
    void deveFiltrarPostagensPorAutorETema() {
        when(postagemRepository.findByUsuarioIdAndTemaId(1L, 1L)).thenReturn(List.of(postagem));

        List<Postagem> result = postagemService.filtrarPorUsuarioETema(1L, 1L);

        assertEquals(1, result.size());
        assertEquals(postagem.getTitulo(), result.get(0).getTitulo());
    }

    @Test
    void deveLancarExcecaoQuandoFiltroNaoRetornaPostagens() {
        when(postagemRepository.findByUsuarioIdAndTemaId(1L, 1L)).thenReturn(Collections.emptyList());
        assertThrows(PostagemNaoEncontradaException.class,
                () -> postagemService.filtrarPorUsuarioETema(1L, 1L));
    }

    private void mockUsuarioAutenticado(User user) {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(user.getUsuario());

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);

        SecurityContextHolder.setContext(context);
    }
}