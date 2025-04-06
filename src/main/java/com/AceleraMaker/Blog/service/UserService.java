package com.AceleraMaker.Blog.service;

import com.AceleraMaker.Blog.dto.UserDTO;
import com.AceleraMaker.Blog.exception.user.AutenticacaoException;
import com.AceleraMaker.Blog.exception.user.UsuarioJaCadastradoException;
import com.AceleraMaker.Blog.exception.user.UsuarioNaoEncontradoException;
import com.AceleraMaker.Blog.security.JwtService;
import com.AceleraMaker.Blog.dto.UsuarioLogin;
import com.AceleraMaker.Blog.model.User;
import com.AceleraMaker.Blog.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.websocket.AuthenticationException;
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
        if (userRepository.existsByUsuario(usuario.getUsuario())) {
            throw new UsuarioJaCadastradoException("Usuário já existe.");
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return Optional.of(userRepository.save(usuario));
    }

    // Autenticar usuário
    public Optional<UsuarioLogin> autenticarUsuario(UsuarioLogin userLogin) {
        Optional<User> usuario = userRepository.findByUsuario(userLogin.usuario());

        if (usuario.isEmpty() || !passwordEncoder.matches(userLogin.senha(), usuario.get().getSenha())) {
            throw new AutenticacaoException("Usuário ou senha inválidos");
        }

        String token = jwtService.generateToken(usuario.get());
        UsuarioLogin autenticado = new UsuarioLogin(userLogin.usuario(), null, token);
        return Optional.of(autenticado);
    }


    // Atualizar usuário

    public Optional<User> atualizarUsuario(Long id, @Valid User novoUsuario) {

        User usuarioExistente = userRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário com ID " + id + " não encontrado."));

        // Verifica se já existe outro usuário com o mesmo nome de usuário
        Optional<User> usuarioComMesmoNome = userRepository.findByUsuario(novoUsuario.getUsuario());

        if (usuarioComMesmoNome.isPresent() && !usuarioComMesmoNome.get().getId().equals(id)) {
            throw new UsuarioJaCadastradoException("Nome de usuário já está em uso.");
        }

        usuarioExistente.setNome(novoUsuario.getNome());
        usuarioExistente.setUsuario(novoUsuario.getUsuario());

        if (novoUsuario.getSenha() != null && !novoUsuario.getSenha().isBlank()) {
            usuarioExistente.setSenha(passwordEncoder.encode(novoUsuario.getSenha()));
        }

        usuarioExistente.setFoto(novoUsuario.getFoto());

        return Optional.of(userRepository.save(usuarioExistente));
    }


    // Deletar usuário

    public void deletarUsuario(Long id) {
        Optional<User> usuario = userRepository.findById(id);

        if (usuario.isEmpty()) {
            throw new UsuarioNaoEncontradoException("Usuário com ID " + id + " não encontrado.");
        }

        userRepository.deleteById(id);
    }

    public UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getNome(), user.getUsuario(), user.getFoto());
    }
}
