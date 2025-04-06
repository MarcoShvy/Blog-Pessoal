package com.AceleraMaker.Blog.controller;

import com.AceleraMaker.Blog.dto.PostagemDTO;
import com.AceleraMaker.Blog.model.Postagem;
import com.AceleraMaker.Blog.service.PostagemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/postagens")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostagemController {

    @Autowired
    private PostagemService postagemService;

    @GetMapping
    public ResponseEntity<List<Postagem>> listarTodas() {
        return ResponseEntity.ok(postagemService.listarTodas());
    }

    @GetMapping("/filtro")
    public ResponseEntity<List<Postagem>> filtrar(
            @RequestParam(required = false) Long autor,
            @RequestParam(required = false) Long tema) {

        List<Postagem> resultado = postagemService.filtrarPorUsuarioETema(autor, tema);
        return ResponseEntity.ok(resultado);
    }

    @PostMapping
    public ResponseEntity<Postagem> criar(@Valid @RequestBody PostagemDTO dto) {
        return ResponseEntity.ok(postagemService.criar(dto).get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody PostagemDTO dto) {
        return postagemService.atualizar(id, dto)
                .map(postagem -> ResponseEntity.ok(postagem))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (postagemService.deletar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
