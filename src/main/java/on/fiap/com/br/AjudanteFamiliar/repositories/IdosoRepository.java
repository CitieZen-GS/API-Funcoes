package on.fiap.com.br.AjudanteFamiliar.repositories;

import on.fiap.com.br.AjudanteFamiliar.models.Idoso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IdosoRepository extends JpaRepository<Idoso, Long> {

    List<Idoso> findAllByOrderByNomeAsc();

    List<Idoso> findByCuidadorIdOrderByNomeAsc(Long idCuidador);
}
