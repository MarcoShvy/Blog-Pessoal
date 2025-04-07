package com.aceleramaker.blog.repository;

import com.aceleramaker.blog.model.User;
import com.aceleramaker.blog.model.enums.TipoUsuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Deve salvar e buscar usuário pelo campo 'usuario'")
    void testFindByUsuario() {
        User user = new User();
        user.setUsuario("marco");
        user.setSenha("123456");
        user.setNome("Marco");
        user.setFoto("foto.png");
        user.setTipoUsuario(TipoUsuario.ADMIN);

        userRepository.save(user);

        Optional<User> resultado = userRepository.findByUsuario("marco");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("Marco");
    }

    @Test
    @DisplayName("Deve retornar true se o usuario existir")
    void testExistsByUsuario() {
        User user = new User();
        user.setUsuario("teste");
        user.setSenha("123");
        user.setNome("Teste");
        user.setTipoUsuario(TipoUsuario.ADMIN);
        user.setPostagems(null);

        userRepository.save(user);

        boolean existe = userRepository.existsByUsuario("teste");

        assertThat(existe).isTrue();
    }
}