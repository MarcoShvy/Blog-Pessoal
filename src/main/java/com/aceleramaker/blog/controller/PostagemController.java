package com.aceleramaker.blog.controller;

import com.aceleramaker.blog.dto.PostagemDTO;
import com.aceleramaker.blog.dto.PostagemResponseDTO;
import com.aceleramaker.blog.model.Postagem;
import com.aceleramaker.blog.service.PostagemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/postagens")
public class PostagemController {

    private PostagemService postagemService;

    @Autowired
    public PostagemController(PostagemService postagemService) {
        this.postagemService = postagemService;
    }

    public PostagemController() {}

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

    @PutMapping("{id}")
    public ResponseEntity<PostagemResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody PostagemDTO dto) {
        Postagem postagemAtualizada = postagemService.atualizar(id, dto);
        return ResponseEntity.ok(postagemService.toResponseDTO(postagemAtualizada));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, String>> deletar(@PathVariable Long id) {
        postagemService.deletar(id);
        Map<String, String> resposta = new HashMap<>();
        resposta.put("mensagem", "Postagem deletada com sucesso");
        return ResponseEntity.ok(resposta);
    }
}
