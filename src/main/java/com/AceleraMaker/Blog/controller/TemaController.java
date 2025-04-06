package com.AceleraMaker.Blog.controller;

import com.AceleraMaker.Blog.dto.TemaDTO;
import com.AceleraMaker.Blog.model.Tema;
import com.AceleraMaker.Blog.service.TemaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/temas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TemaController {

    private final TemaService temaService;

    @Autowired
    public TemaController(TemaService temaService) {
        this.temaService = temaService;
    }

    @PostMapping
    public ResponseEntity<TemaDTO> criarTema(@Valid @RequestBody TemaDTO temaDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(temaService.criarTema(temaDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarTema(@PathVariable Long id, @RequestBody TemaDTO temaAtualizado) {
        Optional<TemaDTO> tema = temaService.atualizarTema(id, temaAtualizado);
        return tema.<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tema não encontrado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarTema(@PathVariable Long id) {
        boolean deletado = temaService.excluirTema(id);
        if (deletado) {
            return ResponseEntity.ok("Tema excluído com sucesso");
        } else {
            return ResponseEntity.status(404).body("Tema não encontrado");
        }
    }

    @GetMapping
    public ResponseEntity<List<TemaDTO>> listarTodos() {
        return ResponseEntity.ok(temaService.listarTodos());
    }
}
