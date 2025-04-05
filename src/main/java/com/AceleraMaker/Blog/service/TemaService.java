package com.AceleraMaker.Blog.service;

import com.AceleraMaker.Blog.model.Tema;
import com.AceleraMaker.Blog.repository.TemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TemaService {

    private final TemaRepository temaRepository;

    @Autowired
    public TemaService(TemaRepository temaRepository) {
        this.temaRepository = temaRepository;
    }

    // Criar um novo tema
    public Tema criarTema(Tema tema) {
        return temaRepository.save(tema);
    }

    // Atualizar um tema existente
    public Optional<Tema> atualizarTema(Long id, Tema temaAtualizado) {
        return temaRepository.findById(id).map(tema -> {
            tema.setDescricao(temaAtualizado.getDescricao());
            return temaRepository.save(tema);
        });
    }

    // Excluir um tema
    public boolean excluirTema(Long id) {
        return temaRepository.findById(id).map(tema -> {
            temaRepository.delete(tema);
            return true;
        }).orElse(false);
    }

    // Listar todos os temas
    public List<Tema> listarTodos() {
        return temaRepository.findAll();
    }
}