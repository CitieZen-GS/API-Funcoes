package on.fiap.com.br.AjudanteFamiliar.repositories;

import on.fiap.com.br.AjudanteFamiliar.models.LembreteConsulta;
import on.fiap.com.br.AjudanteFamiliar.models.enums.StatusConsulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LembreteConsultaRepository extends JpaRepository<LembreteConsulta, Long> {

    List<LembreteConsulta> findByIdosoIdOrderByDataConsultaAscHoraConsultaAsc(Long idIdoso);

    List<LembreteConsulta> findByIdosoIdAndStatusOrderByDataConsultaAscHoraConsultaAsc(Long idIdoso,
                                                                                       StatusConsulta status);

    List<LembreteConsulta> findByIdosoIdAndStatusAndDataConsultaGreaterThanEqualOrderByDataConsultaAscHoraConsultaAsc(
            Long idIdoso, StatusConsulta status, LocalDate apartirDe);
}
