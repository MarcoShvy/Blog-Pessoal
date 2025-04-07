package com.aceleramaker.blog.dto;

import jakarta.validation.constraints.NotBlank;

public class TemaDTO {

    private Long id;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;


    public TemaDTO() {
    }

    public TemaDTO(Long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}