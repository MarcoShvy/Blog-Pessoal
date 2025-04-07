package com.aceleramaker.blog.controller;

import com.aceleramaker.blog.dto.TemaDTO;
import com.aceleramaker.blog.service.TemaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/temas/")
public class TemaController {

    private final TemaService temaService;

    public TemaController(TemaService temaService) {
        this.temaService = temaService;
    }

    @PostMapping
    public ResponseEntity<TemaDTO> criarTema(@Valid @RequestBody TemaDTO temaDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(temaService.criarTema(temaDTO));
    }

    @PutMapping("{id}")
    public ResponseEntity<TemaDTO> atualizarTema(@Valid @PathVariable Long id, @RequestBody TemaDTO temaAtualizado) {
        TemaDTO tema = temaService.atualizarTema(id, temaAtualizado);
        return ResponseEntity.ok(tema);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, String>> deletarTema(@PathVariable Long id) {
        temaService.excluirTema(id);

        Map<String, String> resposta = new HashMap<>();
        resposta.put("mensagem", "Tema deletado com sucesso");

        return ResponseEntity.ok(resposta);
    }

    @GetMapping
    public ResponseEntity<List<TemaDTO>> listarTodos() {
        return ResponseEntity.ok(temaService.listarTodos());
    }
}
