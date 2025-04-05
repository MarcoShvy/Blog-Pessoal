package com.AceleraMaker.Blog.repository;

import com.AceleraMaker.Blog.model.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostagemRepository extends JpaRepository<Postagem, Long> {
}

