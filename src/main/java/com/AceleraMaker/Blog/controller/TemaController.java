package com.AceleraMaker.Blog.controller;

import com.AceleraMaker.Blog.dto.TemaDTO;
import com.AceleraMaker.Blog.service.TemaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/temas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TemaController {

    private final TemaService temaService;

    public TemaController(TemaService temaService) {
        this.temaService = temaService;
    }

    @PostMapping
    public ResponseEntity<TemaDTO> criarTema(@Valid @RequestBody TemaDTO temaDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(temaService.criarTema(temaDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TemaDTO> atualizarTema(@Valid @PathVariable Long id, @RequestBody TemaDTO temaAtualizado) {
        TemaDTO tema = temaService.atualizarTema(id, temaAtualizado);
        return ResponseEntity.ok(tema);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTema(@PathVariable Long id) {
        temaService.excluirTema(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TemaDTO>> listarTodos() {
        return ResponseEntity.ok(temaService.listarTodos());
    }
}
