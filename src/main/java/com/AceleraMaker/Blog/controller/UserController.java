package com.AceleraMaker.Blog.controller;

import com.AceleraMaker.Blog.dto.UserDTO;
import com.AceleraMaker.Blog.dto.UsuarioLogin;
import com.AceleraMaker.Blog.exception.user.UsuarioJaCadastradoException;
import com.AceleraMaker.Blog.model.User;
import com.AceleraMaker.Blog.service.UserService;
import org.apache.tomcat.websocket.AuthenticationException;
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

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UsuarioLogin userLogin) {
        UsuarioLogin usuarioAutenticado = userService.autenticarUsuario(userLogin).orElseThrow();
        return ResponseEntity.ok(usuarioAutenticado);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<Object> atualizar(@RequestBody User user) {
        User usuarioAtualizado = userService.atualizarUsuario(user.getId(), user).orElseThrow();
        return ResponseEntity.status(HttpStatus.OK).body(usuarioAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        userService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

}
