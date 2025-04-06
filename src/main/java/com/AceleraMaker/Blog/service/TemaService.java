package com.AceleraMaker.Blog.service;

import com.AceleraMaker.Blog.dto.TemaDTO;
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

    public TemaDTO toDTO(Tema tema) {
        TemaDTO dto = new TemaDTO();
        dto.setId(tema.getId());
        dto.setDescricao(tema.getDescricao());
        return dto;
    }

    public Tema toEntity(TemaDTO dto) {
        Tema tema = new Tema();
        tema.setId(dto.getId());
        tema.setDescricao(dto.getDescricao());
        return tema;
    }


    public TemaDTO criarTema(TemaDTO dto) {
        Tema tema = toEntity(dto);
        Tema novo = temaRepository.save(tema);
        return toDTO(novo);
    }

    public Optional<TemaDTO> atualizarTema(Long id, TemaDTO dto) {
        return temaRepository.findById(id).map(tema -> {
            tema.setDescricao(dto.getDescricao());
            Tema atualizado = temaRepository.save(tema);
            return toDTO(atualizado);
        });
    }

    public List<TemaDTO> listarTodos() {
        return temaRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // Excluir um tema
    public boolean excluirTema(Long id) {
        return temaRepository.findById(id).map(tema -> {
            temaRepository.delete(tema);
            return true;
        }).orElse(false);
    }
}