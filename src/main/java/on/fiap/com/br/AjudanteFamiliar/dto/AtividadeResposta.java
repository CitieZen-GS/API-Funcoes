package on.fiap.com.br.AjudanteFamiliar.dto;

import on.fiap.com.br.AjudanteFamiliar.models.Atividade;

import java.time.LocalDate;
import java.time.LocalTime;

public record AtividadeResposta(
        Long id,
        String nome,
        String descricao,
        LocalDate data,
        LocalTime horaInicio,
        LocalTime horaFim,
        Long idIdoso
) {

    public static AtividadeResposta de(Atividade atividade) {
        return new AtividadeResposta(
                atividade.getId(),
                atividade.getNome(),
                atividade.getDescricao(),
                atividade.getData(),
                atividade.getHoraInicio() == null ? null : atividade.getHoraInicio().toLocalTime(),
                atividade.getHoraFim() == null ? null : atividade.getHoraFim().toLocalTime(),
                atividade.getIdoso().getId());
    }
}
