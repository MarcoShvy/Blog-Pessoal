package com.aceleramaker.blog.exception.user;

public class UsuarioNaoEncontradoException extends RuntimeException{
    public UsuarioNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
