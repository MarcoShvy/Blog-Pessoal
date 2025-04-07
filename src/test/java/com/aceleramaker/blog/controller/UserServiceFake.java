package com.aceleramaker.blog.controller;

import com.aceleramaker.blog.dto.UserDTO;
import com.aceleramaker.blog.service.UserService;

public class UserServiceFake extends UserService {
    
    public UserDTO criar(UserDTO dto) {
        // Retorna um usuário simulado com ID fictício
        return new UserDTO(1L, dto.getNome(), dto.getUsuario(), dto.getFoto(), dto.getSenha(), dto.getTipoUsuario());
    }
}
