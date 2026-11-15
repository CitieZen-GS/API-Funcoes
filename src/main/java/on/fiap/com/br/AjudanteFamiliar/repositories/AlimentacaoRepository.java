package on.fiap.com.br.AjudanteFamiliar.repositories;

import on.fiap.com.br.AjudanteFamiliar.models.Alimentacao;
import on.fiap.com.br.AjudanteFamiliar.models.enums.TipoAlimentacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AlimentacaoRepository extends JpaRepository<Alimentacao, Long> {

    List<Alimentacao> findByIdosoIdOrderByDataConsumoDescHoraConsumoDesc(Long idIdoso);

    List<Alimentacao> findByIdosoIdAndDataConsumoOrderByHoraConsumoAsc(Long idIdoso, LocalDate dataConsumo);

    List<Alimentacao> findByIdosoIdAndTipoOrderByDataConsumoDescHoraConsumoDesc(Long idIdoso, TipoAlimentacao tipo);

    List<Alimentacao> findByIdosoIdAndDataConsumoAndTipoOrderByHoraConsumoAsc(Long idIdoso, LocalDate dataConsumo,
                                                                              TipoAlimentacao tipo);
}
