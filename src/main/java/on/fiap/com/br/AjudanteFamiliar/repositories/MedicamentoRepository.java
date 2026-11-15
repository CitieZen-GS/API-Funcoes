package on.fiap.com.br.AjudanteFamiliar.repositories;

import on.fiap.com.br.AjudanteFamiliar.models.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {

    List<Medicamento> findByIdosoIdOrderByHoraAsc(Long idIdoso);
}
