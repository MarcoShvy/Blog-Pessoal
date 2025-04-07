package com.aceleramaker.blog.exception.tema;

public class TemaNaoEncontradoException extends RuntimeException{
    public TemaNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
