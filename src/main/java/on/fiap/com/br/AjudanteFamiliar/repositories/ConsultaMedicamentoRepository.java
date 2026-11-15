package on.fiap.com.br.AjudanteFamiliar.repositories;

import on.fiap.com.br.AjudanteFamiliar.models.ConsultaMedicamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConsultaMedicamentoRepository extends JpaRepository<ConsultaMedicamento, Long> {

    List<ConsultaMedicamento> findByLembreteConsultaId(Long idLembreteConsulta);

    boolean existsByLembreteConsultaIdAndMedicamentoId(Long idLembreteConsulta, Long idMedicamento);

    Optional<ConsultaMedicamento> findByLembreteConsultaIdAndMedicamentoId(Long idLembreteConsulta,
                                                                           Long idMedicamento);

    void deleteByLembreteConsultaId(Long idLembreteConsulta);

    void deleteByMedicamentoId(Long idMedicamento);
}
