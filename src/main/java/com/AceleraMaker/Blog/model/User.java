package com.AceleraMaker.Blog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String usuario;

    private String senha;

    @Column(length = 5000)
    private String foto;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("usuario")
    @JsonIgnore
    private List<Postagem> postagems;
}
