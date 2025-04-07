package com.aceleramaker.blog.exception.postagem;

public class PostagemNaoEncontradaException extends RuntimeException{
    public PostagemNaoEncontradaException(String message) {
        super(message);
    }
}
