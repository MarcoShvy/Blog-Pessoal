package com.AceleraMaker.Blog.controller;

import com.AceleraMaker.Blog.model.Users;
import com.AceleraMaker.Blog.repository.UserRepository;
import com.AceleraMaker.Blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("api/usuarios")
public class UserController {

    @Autowired
    private UserService usuarioService;

    // POST /api/usuarios - cadastrar novo usuário
    @PostMapping
    public ResponseEntity<Users> cadastrarUsuario(@RequestBody Users usuario) {
        Optional<Users> novoUsuario = usuarioService.cadastrarUsuario(usuario);
        return novoUsuario
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
}
