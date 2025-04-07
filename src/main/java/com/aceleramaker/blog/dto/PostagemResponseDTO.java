package com.aceleramaker.blog.dto;

import java.time.LocalDateTime;

public class PostagemResponseDTO {
    private Long id;
    private String titulo;
    private String texto;
    private LocalDateTime dataCriacao;
    private String nomeAutor;
    private String nomeTema;

    // Construtores
    public PostagemResponseDTO() {}

    public PostagemResponseDTO(Long id, String titulo, String texto, LocalDateTime dataCriacao, String nomeAutor, String nomeTema) {
        this.id = id;
        this.titulo = titulo;
        this.texto = texto;
        this.dataCriacao = dataCriacao;
        this.nomeAutor = nomeAutor;
        this.nomeTema = nomeTema;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getNomeAutor() {
        return nomeAutor;
    }

    public void setNomeAutor(String nomeAutor) {
        this.nomeAutor = nomeAutor;
    }

    public String getNomeTema() {
        return nomeTema;
    }

    public void setNomeTema(String nomeTema) {
        this.nomeTema = nomeTema;
    }
}
