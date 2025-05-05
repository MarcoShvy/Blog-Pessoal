package com.aceleramaker.blog.service;

import com.aceleramaker.blog.dto.PostagemDTO;
import com.aceleramaker.blog.dto.PostagemResponseDTO;
import com.aceleramaker.blog.exception.postagem.PostagemNaoEncontradaException;
import com.aceleramaker.blog.exception.tema.TemaNaoEncontradoException;
import com.aceleramaker.blog.exception.user.UsuarioNaoEncontradoException;
import com.aceleramaker.blog.model.Postagem;
import com.aceleramaker.blog.model.Tema;
import com.aceleramaker.blog.model.User;
import com.aceleramaker.blog.model.enums.TipoUsuario;
import com.aceleramaker.blog.repository.PostagemRepository;
import com.aceleramaker.blog.repository.TemaRepository;
import com.aceleramaker.blog.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostagemService {

    private final PostagemRepository postagemRepository;
    private final TemaRepository temaRepository;
    private final UserRepository userRepository;

    public PostagemService (PostagemRepository postagemRepository, TemaRepository temaRepository, UserRepository userRepository){
        this.postagemRepository = postagemRepository;
        this.temaRepository = temaRepository;
        this.userRepository = userRepository;
    }

    public List<Postagem> listarTodas() {
        return postagemRepository.findAll();
    }

    public List<Postagem> filtrarPorUsuarioETema(Long autor, Long tema) {
        List<Postagem> postagens;

        if (autor != null && tema != null) {
            postagens = postagemRepository.findByUsuarioIdAndTemaId(autor, tema);
        } else if (autor != null) {
            postagens = postagemRepository.findByUsuarioId(autor);
        } else if (tema != null) {
            postagens = postagemRepository.findByTemaId(tema);
        } else {
            postagens = listarTodas();
        }

        if (postagens.isEmpty()) {
            throw new PostagemNaoEncontradaException("Nenhuma postagem encontrada com os filtros aplicados");
        }

        return postagens;
    }


    public Postagem criar(PostagemDTO dto) {
        User usuario = userRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

        Tema tema = temaRepository.findById(dto.getTemaId())
                .orElseThrow(() -> new TemaNaoEncontradoException("Tema não encontrado"));

        Postagem novaPostagem = new Postagem();
        novaPostagem.setTitulo(dto.getTitulo());
        novaPostagem.setTexto(dto.getTexto());
        novaPostagem.setUsuario(usuario);
        novaPostagem.setTema(tema);

        return postagemRepository.save(novaPostagem);
    }

    public Postagem atualizar(Long id, PostagemDTO dto) {
        Postagem postagemExistente = postagemRepository.findById(id)
                .orElseThrow(() -> new PostagemNaoEncontradaException("Postagem não encontrada"));

        User usuarioAutenticado = getUsuarioAutenticado();

        boolean isAdmin = usuarioAutenticado.getTipoUsuario().equals(TipoUsuario.ADMIN);
        boolean isOwner = postagemExistente.getUsuario().getId().equals(usuarioAutenticado.getId());

        if (!isAdmin && !isOwner) {
            throw new SecurityException("Você não tem permissão para editar esta postagem");
        }

        postagemExistente.setTitulo(dto.getTitulo());
        postagemExistente.setTexto(dto.getTexto());

        return postagemRepository.save(postagemExistente);
    }

    public void deletar(Long id) {
        Postagem postagem = postagemRepository.findById(id)
                .orElseThrow(() -> new PostagemNaoEncontradaException("Postagem com ID " + id + " não encontrada"));

        User usuarioAutenticado = getUsuarioAutenticado();

        boolean isAdmin = usuarioAutenticado.getTipoUsuario().equals(TipoUsuario.ADMIN);
        boolean isOwner = postagem.getUsuario().getId().equals(usuarioAutenticado.getId());

        if (!isAdmin && !isOwner) {
            throw new SecurityException("Você não tem permissão para excluir esta postagem");
        }

        postagemRepository.deleteById(id);
    }

    public Postagem buscarPostagem(Long id) {
        return postagemRepository.findById(id)
                .orElseThrow(() -> new PostagemNaoEncontradaException("Postagem com ID " + id + " não encontrada."));
    }


    public PostagemResponseDTO toResponseDTO(Postagem postagem) {
        return new PostagemResponseDTO(
                postagem.getId(),
                postagem.getTitulo(),
                postagem.getTexto(),
                postagem.getData(),
                postagem.getUsuario() != null ? postagem.getUsuario().getNome() : null,
                postagem.getTema() != null ? postagem.getTema().getDescricao() : null
        );
    }

    public List<PostagemResponseDTO> toResponseDTOList(List<Postagem> postagens) {
        return postagens.stream()
                .map(this::toResponseDTO).toList();
    }

    private User getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsuario(username)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário autenticado não encontrado"));
    }

}
