package com.AceleraMaker.Blog.service;

import com.AceleraMaker.Blog.dto.PostagemDTO;
import com.AceleraMaker.Blog.dto.PostagemResponseDTO;
import com.AceleraMaker.Blog.exception.postagem.PostagemNaoEncontradaException;
import com.AceleraMaker.Blog.exception.tema.TemaNaoEncontradoException;
import com.AceleraMaker.Blog.exception.user.UsuarioNaoEncontradoException;
import com.AceleraMaker.Blog.model.Postagem;
import com.AceleraMaker.Blog.model.Tema;
import com.AceleraMaker.Blog.model.User;
import com.AceleraMaker.Blog.repository.PostagemRepository;
import com.AceleraMaker.Blog.repository.TemaRepository;
import com.AceleraMaker.Blog.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

        User usuario = userRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

        Tema tema = temaRepository.findById(dto.getTemaId())
                .orElseThrow(() -> new TemaNaoEncontradoException("Tema não encontrado"));

        postagemExistente.setTitulo(dto.getTitulo());
        postagemExistente.setTexto(dto.getTexto());
        postagemExistente.setUsuario(usuario);
        postagemExistente.setTema(tema);

        return postagemRepository.save(postagemExistente);
    }

    public boolean deletar(Long id) {
        if (!postagemRepository.existsById(id)) {
            throw new PostagemNaoEncontradaException("Postagem com ID " + id + " não encontrada");
        }

        postagemRepository.deleteById(id);
        return true;
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
                .map(this::toResponseDTO).collect(Collectors.toList());
    }

}
