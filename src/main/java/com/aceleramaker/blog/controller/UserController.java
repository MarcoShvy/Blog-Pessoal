package com.aceleramaker.blog.controller;

import com.aceleramaker.blog.dto.UserDTO;
import com.aceleramaker.blog.dto.UsuarioLogin;
import com.aceleramaker.blog.exception.user.UsuarioJaCadastradoException;
import com.aceleramaker.blog.model.User;
import com.aceleramaker.blog.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UserController {


    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<List<UserDTO>> listarTodosUsuarios() {
        List<UserDTO> usuarios = userService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping()
    public ResponseEntity<Object> cadastrarUsuarioDTO(@Valid @RequestBody UserDTO userDTO) {
        try {
            User user = userService.fromDTO(userDTO);
            User novoUsuario = userService.cadastrarUsuario(user).orElseThrow();
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

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizar(@PathVariable Long id, @RequestBody UserDTO user) {
        User usuarioAtualizado = userService.atualizarUsuario(id, user).orElseThrow();
        return ResponseEntity.status(HttpStatus.OK).body(usuarioAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletarUsuario(@PathVariable Long id) {
        userService.deletarUsuario(id);

        Map<String, String> resposta = new HashMap<>();
        resposta.put("mensagem", "Usuário deletado com sucesso");

        return ResponseEntity.ok(resposta);
    }

}
