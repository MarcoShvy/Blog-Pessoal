package com.AceleraMaker.Blog.service;

import com.AceleraMaker.Blog.dto.PostagemDTO;
import com.AceleraMaker.Blog.dto.PostagemResponseDTO;
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
        if (autor != null && tema != null) {
            return postagemRepository.findByUsuarioIdAndTemaId(autor, tema);
        } else if (autor != null) {
            return postagemRepository.findByUsuarioId(autor);
        } else if (tema != null) {
            return postagemRepository.findByTemaId(tema);
        } else {
            return listarTodas();
        }
    }


    public Optional<Postagem> criar(PostagemDTO dto) {
        Postagem postagem = new Postagem();
        postagem.setTitulo(dto.getTitulo());
        postagem.setTexto(dto.getTexto());

        // Busca o usuário no banco
        User usuario = userRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        postagem.setUsuario(usuario);

        // Busca o tema no banco
        Tema tema = temaRepository.findById(dto.getTemaId())
                .orElseThrow(() -> new RuntimeException("Tema não encontrado"));
        postagem.setTema(tema);

        return Optional.of(postagemRepository.save(postagem));
    }

    public Optional<Postagem> atualizar(Long id, PostagemDTO dto) {
        Optional<Postagem> existente = postagemRepository.findById(id);
        if (existente.isPresent()) {
            Postagem postagem = existente.get();
            postagem.setTitulo(dto.getTitulo());
            postagem.setTexto(dto.getTexto());
            return Optional.of(postagemRepository.save(postagem));
        } else {
            return Optional.empty();
        }
    }

    public boolean deletar(Long id) {
        if (postagemRepository.existsById(id)) {
            postagemRepository.deleteById(id);
            return true;
        }
        return false;
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
