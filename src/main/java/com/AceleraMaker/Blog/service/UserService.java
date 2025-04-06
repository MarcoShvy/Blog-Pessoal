package com.AceleraMaker.Blog.service;

import com.AceleraMaker.Blog.dto.UserDTO;
import com.AceleraMaker.Blog.security.JwtService;
import com.AceleraMaker.Blog.dto.UsuarioLogin;
import com.AceleraMaker.Blog.model.User;
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
    public Optional<User> cadastrarUsuario(User usuario) {
        if (userRepository.findByUsuario(usuario.getUsuario()).isPresent()) {
            return Optional.empty();
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return Optional.of(userRepository.save(usuario));
    }

    // Autenticar usuário
    public Optional<UsuarioLogin> autenticarUsuario(UsuarioLogin userLogin) {
        Optional<User> usuario = userRepository.findByUsuario(userLogin.usuario());

        if (usuario.isPresent()) {
            boolean senhaCorreta = passwordEncoder.matches(userLogin.senha(), usuario.get().getSenha());

            if (senhaCorreta) {
                String token = jwtService.generateToken(usuario.get());

                UsuarioLogin autenticado = new UsuarioLogin(userLogin.usuario(), null, token);
                return Optional.of(autenticado);
            }
        }

        return Optional.empty();
    }

    // Atualizar usuário

    public Optional<User> atualizarUsuario(Long id, User usuarioAtualizado) {
        Optional<User> usuarioExistente = userRepository.findById(id);

        if (usuarioExistente.isPresent()) {
            User usuario = usuarioExistente.get();

            usuario.setNome(usuarioAtualizado.getNome());
            usuario.setUsuario(usuarioAtualizado.getUsuario());

            if (usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isEmpty()) {
                usuario.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
            }
            usuario.setFoto(usuarioAtualizado.getFoto());

            return Optional.of(userRepository.save(usuario));
        }

        return Optional.empty();
    }


    // Deletar usuário

    public boolean deletarUsuario(Long id) {
        Optional<User> usuarioExistente = userRepository.findById(id);

        if (usuarioExistente.isPresent()) {
            userRepository.deleteById(id);
            return true;
        }

        return false;
    }

    public UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getNome(), user.getUsuario(), user.getFoto());
    }
}
