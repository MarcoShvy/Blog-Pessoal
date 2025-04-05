package com.AceleraMaker.Blog.controller;

import com.AceleraMaker.Blog.dto.UsuarioLogin;
import com.AceleraMaker.Blog.model.Users;
import com.AceleraMaker.Blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @Autowired
    private UserService userService;

    // POST /usuarios/cadastrar
    @PostMapping("/cadastrar")
    public ResponseEntity<Object> cadastrarUsuario(@RequestBody Users usuario) {
        Optional<Users> novoUsuario = userService.cadastrarUsuario(usuario);

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
}
