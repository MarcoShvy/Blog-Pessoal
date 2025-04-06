package com.AceleraMaker.Blog.service;

import com.AceleraMaker.Blog.dto.PostagemDTO;
import com.AceleraMaker.Blog.model.Postagem;
import com.AceleraMaker.Blog.repository.PostagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostagemService {

    private PostagemRepository postagemRepository;
    public PostagemService (PostagemRepository postagemRepository){
        this.postagemRepository = postagemRepository;
    }

    public List<Postagem> listarTodas() {
        return postagemRepository.findAll();
    }

    public List<Postagem> filtrarPorAutorETema(Long autorId, Long temaId) {
        if (autorId != null && temaId != null) {
            return postagemRepository.findByAutorIdAndTemaId(autorId, temaId);
        } else if (autorId != null) {
            return postagemRepository.findByAutorId(autorId);
        } else if (temaId != null) {
            return postagemRepository.findByTemaId(temaId);
        } else {
            return postagemRepository.findAll(); // ou retornar uma lista vazia, dependendo da regra
        }
    }

    public Optional<Postagem> criar(PostagemDTO dto) {
        Postagem postagem = new Postagem();
        postagem.setTitulo(dto.getTitulo());
        postagem.setTexto(dto.getTexto());
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
}
