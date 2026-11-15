package on.fiap.com.br.AjudanteFamiliar.repositories;

import on.fiap.com.br.AjudanteFamiliar.models.Atividade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AtividadeRepository extends JpaRepository<Atividade, Long> {

    List<Atividade> findByIdosoIdOrderByDataAscHoraInicioAsc(Long idIdoso);

    List<Atividade> findByIdosoIdAndDataOrderByHoraInicioAsc(Long idIdoso, LocalDate data);
}
