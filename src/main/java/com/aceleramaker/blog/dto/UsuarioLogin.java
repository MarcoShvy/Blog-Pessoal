package com.aceleramaker.blog.dto;

public record UsuarioLogin(
        String usuario,
        String senha,
        String token,
        Long id
) {}
