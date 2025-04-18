package com.aceleramaker.blog.service;

import com.aceleramaker.blog.dto.TemaDTO;
import com.aceleramaker.blog.exception.tema.TemaNaoEncontradoException;
import com.aceleramaker.blog.model.Tema;
import com.aceleramaker.blog.repository.TemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public TemaDTO atualizarTema(Long id, TemaDTO dto) {
        Tema tema = temaRepository.findById(id)
                .orElseThrow(() -> new TemaNaoEncontradoException("Tema com ID " + id + " não encontrado"));

        tema.setDescricao(dto.getDescricao());
        Tema atualizado = temaRepository.save(tema);
        return toDTO(atualizado);
    }

    public List<TemaDTO> listarTodos() {
        return temaRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // Excluir um tema
    public void excluirTema(Long id) {
        Tema tema = temaRepository.findById(id)
                .orElseThrow(() -> new TemaNaoEncontradoException("Tema com ID " + id + " não encontrado"));

        temaRepository.delete(tema);
    }
}