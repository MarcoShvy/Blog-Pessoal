package com.AceleraMaker.Blog.controller;

import com.AceleraMaker.Blog.dto.UserDTO;
import com.AceleraMaker.Blog.dto.UsuarioLogin;
import com.AceleraMaker.Blog.exception.user.UsuarioJaCadastradoException;
import com.AceleraMaker.Blog.model.User;
import com.AceleraMaker.Blog.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/usuarios/")
public class UserController {


    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<Object> cadastrarUsuario(@RequestBody User usuario) {
        try {
            User novoUsuario = userService.cadastrarUsuario(usuario).orElseThrow();
            UserDTO dto = userService.toDTO(novoUsuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (UsuarioJaCadastradoException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping("login")
    public ResponseEntity<Object> login(@RequestBody UsuarioLogin userLogin) {
        UsuarioLogin usuarioAutenticado = userService.autenticarUsuario(userLogin).orElseThrow();
        return ResponseEntity.ok(usuarioAutenticado);
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> atualizar(@PathVariable Long id, @RequestBody UserDTO user) {
        User usuarioAtualizado = userService.atualizarUsuario(id, user).orElseThrow();
        return ResponseEntity.status(HttpStatus.OK).body(usuarioAtualizado);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, String>> deletarUsuario(@PathVariable Long id) {
        userService.deletarUsuario(id);

        Map<String, String> resposta = new HashMap<>();
        resposta.put("mensagem", "Usuário deletado com sucesso");

        return ResponseEntity.ok(resposta);
    }

}
