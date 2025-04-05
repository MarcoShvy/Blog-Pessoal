package com.AceleraMaker.Blog.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger ID;

    private String nome;

    @Column(unique = true)
    private String usuario;

    private String senha;

    @Column(length = 5000)
    private String foto;
}
