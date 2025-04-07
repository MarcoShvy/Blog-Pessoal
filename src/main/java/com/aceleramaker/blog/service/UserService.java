package com.aceleramaker.blog.service;

import com.aceleramaker.blog.dto.UserDTO;
import com.aceleramaker.blog.exception.user.AutenticacaoException;
import com.aceleramaker.blog.exception.user.UsuarioJaCadastradoException;
import com.aceleramaker.blog.exception.user.UsuarioNaoEncontradoException;
import com.aceleramaker.blog.security.JwtService;
import com.aceleramaker.blog.dto.UsuarioLogin;
import com.aceleramaker.blog.model.User;
import com.aceleramaker.blog.repository.UserRepository;
import jakarta.validation.Valid;
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


@Service
public class UserService {


    private UserRepository userRepository;
    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

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

    public Optional<User> atualizarUsuario(Long id, @Valid UserDTO novoUsuario) {

        User usuarioExistente = userRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário com ID " + id + " não encontrado."));


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


    public User fromDTO(UserDTO dto) {
        return User.builder()
                .nome(dto.getNome())
                .usuario(dto.getUsuario())
                .senha(dto.getSenha())
                .foto(dto.getFoto())
                .tipoUsuario(dto.getTipoUsuario())
                .build();
    }

    public UserDTO toDTO(User user) {
        return UserDTO.builder()
                .nome(user.getNome())
                .usuario(user.getUsuario())
                .foto(user.getFoto())
                .tipoUsuario(user.getTipoUsuario())
                .build();
    }
}
