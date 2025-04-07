package com.aceleramaker.blog.model;

import com.aceleramaker.blog.model.enums.TipoUsuario;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PostagemTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    @DisplayName("Deve persistir uma postagem com relacionamentos")
    void testPersistPostagem() {
        // Criação do usuário
        User user = new User();
        user.setNome("Marco");
        user.setUsuario("marco123");
        user.setSenha("123456");
        user.setFoto("https://imagem.com/foto.jpg");
        user.setTipoUsuario(TipoUsuario.COMUM);
        entityManager.persist(user);

        // Criação do tema
        Tema tema = new Tema();
        tema.setDescricao("Saúde");
        entityManager.persist(tema);

        // Criação da postagem
        Postagem postagem = new Postagem();
        postagem.setTitulo("Importância da vacinação");
        postagem.setTexto("Vacinar é proteger vidas.");
        postagem.setUsuario(user);
        postagem.setTema(tema);
        entityManager.persist(postagem);

        entityManager.flush();
        entityManager.clear();

        Postagem encontrada = entityManager.find(Postagem.class, postagem.getId());

        assertThat(encontrada).isNotNull();
        assertThat(encontrada.getTitulo()).isEqualTo("Importância da vacinação");
        assertThat(encontrada.getUsuario().getUsuario()).isEqualTo("marco123");
        assertThat(encontrada.getTema().getDescricao()).isEqualTo("Saúde");
    }
}
