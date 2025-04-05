package com.AceleraMaker.Blog.controller;

import com.AceleraMaker.Blog.model.Tema;
import com.AceleraMaker.Blog.service.TemaService;
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

    // Criar um novo tema
    @PostMapping
    public ResponseEntity<Tema> criarTema(@RequestBody Tema tema) {
        Tema novoTema = temaService.criarTema(tema);
        return ResponseEntity.status(201).body(novoTema);
    }

    // Atualizar um tema existente
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarTema(@PathVariable Long id, @RequestBody Tema temaAtualizado) {
        Optional<Tema> tema = temaService.atualizarTema(id, temaAtualizado);
        if (tema.isPresent()) {
            return ResponseEntity.ok(tema.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tema não encontrado");
        }
    }

    //  Excluir um tema
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarTema(@PathVariable Long id) {
        boolean deletado = temaService.excluirTema(id);
        if (deletado) {
            return ResponseEntity.ok("Tema excluído com sucesso");
        } else {
            return ResponseEntity.status(404).body("Tema não encontrado");
        }
    }

    // Listar todos os temas
    @GetMapping
    public ResponseEntity<List<Tema>> listarTodos() {
        return ResponseEntity.ok(temaService.listarTodos());
    }
}