package on.fiap.com.br.AjudanteFamiliar.repositories;

import on.fiap.com.br.AjudanteFamiliar.models.Doenca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoencaRepository extends JpaRepository<Doenca, Long> {

    List<Doenca> findByIdosoIdOrderByNomeAsc(Long idIdoso);
}
