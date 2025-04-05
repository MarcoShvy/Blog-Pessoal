package com.AceleraMaker.Blog.service;

import com.AceleraMaker.Blog.security.JwtService;
import com.AceleraMaker.Blog.dto.UsuarioLogin;
import com.AceleraMaker.Blog.model.Users;
import com.AceleraMaker.Blog.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Cadastrar usuário
    public Optional<Users> cadastrarUsuario(Users usuario) {
        if (userRepository.findByUsuario(usuario.getUsuario()).isPresent()) {
            return Optional.empty();
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return Optional.of(userRepository.save(usuario));
    }

    // Autenticar usuário
    public Optional<UsuarioLogin> autenticarUsuario(UsuarioLogin userLogin) {
        Optional<Users> usuario = userRepository.findByUsuario(userLogin.usuario());

        if (usuario.isPresent()) {
            boolean senhaCorreta = passwordEncoder.matches(userLogin.senha(), usuario.get().getSenha());

            if (senhaCorreta) {
                String token = jwtService.generateToken(usuario.get());

                // Cria um novo UserLogin com o token e senha oculta
                UsuarioLogin autenticado = new UsuarioLogin(userLogin.usuario(), null, token);
                return Optional.of(autenticado);
            }
        }

        return Optional.empty();
    }
}
