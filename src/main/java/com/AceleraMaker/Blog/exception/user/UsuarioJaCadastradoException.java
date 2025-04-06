package com.AceleraMaker.Blog.exception.user;

public class UsuarioJaCadastradoException extends RuntimeException{
    public UsuarioJaCadastradoException(String mensagem) {
        super(mensagem);
    }
}
