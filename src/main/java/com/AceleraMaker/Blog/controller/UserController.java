package com.AceleraMaker.Blog.controller;

import com.AceleraMaker.Blog.dto.UsuarioLogin;
import com.AceleraMaker.Blog.model.User;
import com.AceleraMaker.Blog.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {


    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> cadastrarUsuario(@RequestBody User usuario) {
        Optional<User> novoUsuario = userService.cadastrarUsuario(usuario);

        if (novoUsuario.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existente");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UsuarioLogin userLogin) {
        Optional<UsuarioLogin> userAuth = userService.autenticarUsuario(userLogin);

        if (userAuth.isPresent()) {
            return ResponseEntity.ok(userAuth.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha inválidos");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarUsuario(@PathVariable Long id, @RequestBody User usuarioAtualizado) {
        Optional<User> usuario = userService.atualizarUsuario(id, usuarioAtualizado);

        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarUsuario(@PathVariable Long id) {
        boolean deletado = userService.deletarUsuario(id);

        if (deletado) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }
    }

}
