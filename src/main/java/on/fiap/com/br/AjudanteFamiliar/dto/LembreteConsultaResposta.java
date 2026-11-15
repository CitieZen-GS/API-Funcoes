package on.fiap.com.br.AjudanteFamiliar.dto;

import on.fiap.com.br.AjudanteFamiliar.models.LembreteConsulta;
import on.fiap.com.br.AjudanteFamiliar.models.enums.StatusConsulta;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record LembreteConsultaResposta(
        Long id,
        String titulo,
        String nomeMedico,
        LocalDate dataConsulta,
        LocalTime horaConsulta,
        String descricao,
        StatusConsulta status,
        Long idIdoso,
        List<MedicamentoResposta> medicamentos
) {

    public static LembreteConsultaResposta de(LembreteConsulta lembrete) {
        List<MedicamentoResposta> medicamentos = lembrete.getMedicamentos().stream()
                .map(vinculo -> MedicamentoResposta.de(vinculo.getMedicamento()))
                .toList();
        return new LembreteConsultaResposta(
                lembrete.getId(),
                lembrete.getTitulo(),
                lembrete.getNomeMedico(),
                lembrete.getDataConsulta(),
                lembrete.getHoraConsulta() == null ? null : lembrete.getHoraConsulta().toLocalTime(),
                lembrete.getDescricao(),
                lembrete.getStatus(),
                lembrete.getIdoso().getId(),
                medicamentos);
    }
}
