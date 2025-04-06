package com.AceleraMaker.Blog.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Postagem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 100)
    private String titulo;

    @Column(length = 500)
    private String texto;

    @CreationTimestamp
    private LocalDateTime data;

    @ManyToOne
    @JoinColumn(name = "tema_id")
    @JsonIgnoreProperties("postagem")
    private Tema tema;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnoreProperties("postagem")
    private User usuario;
}
