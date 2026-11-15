package on.fiap.com.br.AjudanteFamiliar.services;

import on.fiap.com.br.AjudanteFamiliar.dto.*;
import on.fiap.com.br.AjudanteFamiliar.models.Idoso;
import on.fiap.com.br.AjudanteFamiliar.models.enums.StatusConsulta;
import on.fiap.com.br.AjudanteFamiliar.repositories.AlimentacaoRepository;
import on.fiap.com.br.AjudanteFamiliar.repositories.AtividadeRepository;
import on.fiap.com.br.AjudanteFamiliar.repositories.DoencaRepository;
import on.fiap.com.br.AjudanteFamiliar.repositories.LembreteConsultaRepository;
import on.fiap.com.br.AjudanteFamiliar.repositories.MedicamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Busca consolidada dos dados do idoso: o que o familiar acompanha e o cuidador consulta no dia a dia.
 */
@Service
@RequiredArgsConstructor
public class ResumoIdosoService {

    private final IdosoService idosoService;
    private final DoencaRepository doencaRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final AtividadeRepository atividadeRepository;
    private final AlimentacaoRepository alimentacaoRepository;
    private final LembreteConsultaRepository lembreteConsultaRepository;

    @Transactional(readOnly = true)
    public ResumoIdosoResposta gerarResumo(Long idIdoso, LocalDate data) {
        LocalDate dia = data != null ? data : LocalDate.now();
        Idoso idoso = idosoService.buscarEntidade(idIdoso);

        return new ResumoIdosoResposta(
                IdosoResposta.de(idoso),
                dia,
                doencaRepository.findByIdosoIdOrderByNomeAsc(idIdoso).stream()
                        .map(DoencaResposta::de).toList(),
                medicamentoRepository.findByIdosoIdOrderByHoraAsc(idIdoso).stream()
                        .map(MedicamentoResposta::de).toList(),
                atividadeRepository.findByIdosoIdAndDataOrderByHoraInicioAsc(idIdoso, dia).stream()
                        .map(AtividadeResposta::de).toList(),
                alimentacaoRepository.findByIdosoIdAndDataConsumoOrderByHoraConsumoAsc(idIdoso, dia).stream()
                        .map(AlimentacaoResposta::de).toList(),
                lembreteConsultaRepository
                        .findByIdosoIdAndStatusAndDataConsultaGreaterThanEqualOrderByDataConsultaAscHoraConsultaAsc(
                                idIdoso, StatusConsulta.PENDENTE, dia).stream()
                        .map(LembreteConsultaResposta::de).toList());
    }
}
