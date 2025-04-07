package com.aceleramaker.blog.model;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TemaTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    @DisplayName("Deve persistir e recuperar um tema com sucesso")
    void testPersistTema() {
        Tema tema = new Tema();
        tema.setDescricao("Tecnologia");

        entityManager.persist(tema);
        entityManager.flush();
        entityManager.clear();

        Tema encontrado = entityManager.find(Tema.class, tema.getId());
        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getDescricao()).isEqualTo("Tecnologia");
    }
}
