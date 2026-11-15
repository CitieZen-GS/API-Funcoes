package on.fiap.com.br.AjudanteFamiliar.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * Visão consolidada do idoso para a tela inicial (cuidador e familiar).
 */
public record ResumoIdosoResposta(
        IdosoResposta idoso,
        LocalDate data,
        List<DoencaResposta> doencas,
        List<MedicamentoResposta> medicamentos,
        List<AtividadeResposta> atividadesDoDia,
        List<AlimentacaoResposta> alimentacaoDoDia,
        List<LembreteConsultaResposta> proximasConsultas
) {
}
