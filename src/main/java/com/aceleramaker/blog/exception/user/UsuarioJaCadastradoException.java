package com.aceleramaker.blog.exception.user;

public class UsuarioJaCadastradoException extends RuntimeException{
    public UsuarioJaCadastradoException(String mensagem) {
        super(mensagem);
    }
}
