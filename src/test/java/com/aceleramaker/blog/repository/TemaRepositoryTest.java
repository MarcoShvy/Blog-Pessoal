/*package com.aceleramaker.blog.repository;

import com.aceleramaker.blog.model.Tema;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TemaRepositoryTest {

    @Autowired
    private TemaRepository temaRepository;

    @Test
    @DisplayName("Deve salvar e recuperar tema")
    void testSaveAndFind() {
        Tema tema = new Tema();
        tema.setDescricao("Tecnologia");

        Tema salvo = temaRepository.save(tema);

        assertThat(salvo.getId()).isNotNull();
        assertThat(salvo.getDescricao()).isEqualTo("Tecnologia");
    }
}
*/