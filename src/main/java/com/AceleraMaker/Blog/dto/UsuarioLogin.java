package com.AceleraMaker.Blog.dto;

public record UsuarioLogin(
        Long id,
        String nome,
        String usuario,
        String senha,
        String foto,
        String token
) {}
