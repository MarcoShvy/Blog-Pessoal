/* com.aceleramaker.blog.repository;

import com.aceleramaker.blog.model.Postagem;
import com.aceleramaker.blog.model.Tema;
import com.aceleramaker.blog.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PostagemRepositoryTest {

    @Autowired
    private PostagemRepository postagemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TemaRepository temaRepository;

    @Test
    @DisplayName("Deve retornar postagens por usuario e tema")
    void testFindByUsuarioIdAndTemaId() {
        // Criar usuário
        User user = new User();
        user.setUsuario("usuario1");
        user.setSenha("senha");
        user.setNome("Usuário Teste");
        user = userRepository.save(user);

        // Criar tema
        Tema tema = new Tema();
        tema.setDescricao("Saúde");
        tema = temaRepository.save(tema);

        // Criar postagem
        Postagem postagem = new Postagem();
        postagem.setTitulo("Post Teste");
        postagem.setTexto("Conteúdo");
        postagem.setUsuario(user);
        postagem.setTema(tema);
        postagemRepository.save(postagem);

        // Buscar postagens
        List<Postagem> resultados = postagemRepository.findByUsuarioIdAndTemaId(user.getId(), tema.getId());

        assertThat(resultados).hasSize(1);
        assertThat(resultados.get(0).getTitulo()).isEqualTo("Post Teste");
    }
}
*/