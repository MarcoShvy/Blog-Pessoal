package com.aceleramaker.blog.repository;

import com.aceleramaker.blog.model.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostagemRepository extends JpaRepository<Postagem, Long> {

    List<Postagem> findByUsuarioId(Long usuarioId);

    List<Postagem> findByTemaId(Long temaId);

    List<Postagem> findByUsuarioIdAndTemaId(Long usuarioId, Long temaId);
}
