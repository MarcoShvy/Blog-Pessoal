package com.aceleramaker.blog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Tema {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @OneToMany(mappedBy = "tema", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("tema")
    @JsonIgnore
    private List<Postagem> postagems;

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

    public List<Postagem> getPostagems() {
        return postagems;
    }

    public void setPostagems(List<Postagem> postagems) {
        this.postagems = postagems;
    }
}
