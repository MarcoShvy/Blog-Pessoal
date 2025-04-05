package com.AceleraMaker.Blog.dto;

public record UsuarioLogin(
        String usuario,
        String senha,
        String token
) {}
