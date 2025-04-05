package com.AceleraMaker.Blog.service;

import com.AceleraMaker.Blog.config.JwtService;
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
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    // Cadastrar usuário
    public Optional<Users> cadastrarUsuario(Users usuario) {
        if (userRepository.findByUsuario(usuario.getUsuario()).isPresent()) {
            return Optional.empty();
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return Optional.of(userRepository.save(usuario));
    }

    // Atualizar usuário
    public Optional<Users> atualizarUsuario(Users usuario) {
        if (userRepository.findById(usuario.getID()).isEmpty()) {
            return Optional.empty(); // não existe
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return Optional.of(userRepository.save(usuario));
    }

    public Optional<UsuarioLogin> autenticarUsuario(UsuarioLogin usuarioLogin) {
        Optional<Users> usuario = userRepository.findByUsuario(usuarioLogin.usuario());

        if (usuario.isPresent()) {
            Users user = usuario.get();

            if (passwordEncoder.matches(usuarioLogin.senha(), user.getSenha())) {
                String token = jwtService.generateToken(user.getUsuario());


                UsuarioLogin loginResposta = new UsuarioLogin(
                        user.getID(),
                        user.getNome(),
                        user.getUsuario(),
                        null,
                        user.getFoto(),
                        token
                );

                return Optional.of(loginResposta);
            }
        }

        return Optional.empty();
    }

    // Excluir usuário
    public void deletarUsuario(Long id) {
        userRepository.deleteById(id);
    }
}
