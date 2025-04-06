package com.AceleraMaker.Blog.controller;

import com.AceleraMaker.Blog.dto.PostagemDTO;
import com.AceleraMaker.Blog.dto.PostagemResponseDTO;
import com.AceleraMaker.Blog.model.Postagem;
import com.AceleraMaker.Blog.service.PostagemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/postagens")
public class PostagemController {

    @Autowired
    private PostagemService postagemService;

    @GetMapping
    public ResponseEntity<List<Postagem>> listarTodas() {
        return ResponseEntity.ok(postagemService.listarTodas());
    }

    @GetMapping("/filtro")
    public ResponseEntity<List<PostagemResponseDTO>> filtrar(
            @RequestParam(required = false) Long autor,
            @RequestParam(required = false) Long tema) {

        List<Postagem> resultado = postagemService.filtrarPorUsuarioETema(autor, tema);
        List<PostagemResponseDTO> resposta = postagemService.toResponseDTOList(resultado);

        return ResponseEntity.ok(resposta);
    }

    @PostMapping
    public ResponseEntity<PostagemResponseDTO> criar(@Valid @RequestBody PostagemDTO dto) {
        Postagem postagem = postagemService.criar(dto);
        return ResponseEntity.ok(postagemService.toResponseDTO(postagem));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostagemResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody PostagemDTO dto) {
        Postagem postagemAtualizada = postagemService.atualizar(id, dto);
        return ResponseEntity.ok(postagemService.toResponseDTO(postagemAtualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        postagemService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
