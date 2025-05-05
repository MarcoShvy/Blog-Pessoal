/*package com.aceleramaker.blog.model;

import com.aceleramaker.blog.model.enums.TipoUsuario;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    @DisplayName("Deve persistir e recuperar um usuário com sucesso")
    void testPersistUser() {
        User user = new User();
        user.setNome("Marco");
        user.setUsuario("marco123");
        user.setSenha("123456");
        user.setFoto("https://imagem.com/foto.jpg");
        user.setTipoUsuario(TipoUsuario.ADMIN);

        entityManager.persist(user);
        entityManager.flush();
        entityManager.clear();

        User encontrado = entityManager.find(User.class, user.getId());
        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getUsuario()).isEqualTo("marco123");
        assertThat(encontrado.getTipoUsuario()).isEqualTo(TipoUsuario.ADMIN);
    }
}*/