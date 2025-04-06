package com.AceleraMaker.Blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PostagemDTO {

    @NotBlank(message = "O título é obrigatório")
    @Size(min = 5, max = 100, message = "O título deve ter entre 5 e 100 caracteres")
    private String titulo;

    @NotBlank(message = "O conteúdo é obrigatório")
    @Size(min = 10, message = "O texto deve ter pelo menos 10 caracteres")
    private String texto;

    @NotNull(message = "O ID do usuário é obrigatório")
    private Long usuarioId;

    @NotNull(message = "O ID do tema é obrigatório")
    private Long temaId;

    // Getters e setters
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

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getTemaId() {
        return temaId;
    }

    public void setTemaId(Long temaId) {
        this.temaId = temaId;
    }
}
