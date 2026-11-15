package on.fiap.com.br.AjudanteFamiliar.dto;

import on.fiap.com.br.AjudanteFamiliar.models.Medicamento;

import java.time.LocalDate;
import java.time.LocalTime;

public record MedicamentoResposta(
        Long id,
        String nome,
        String viaAdministracao,
        String frequencia,
        String instrucaoUso,
        LocalDate data,
        LocalTime hora,
        Long idIdoso
) {

    public static MedicamentoResposta de(Medicamento medicamento) {
        return new MedicamentoResposta(
                medicamento.getId(),
                medicamento.getNome(),
                medicamento.getViaAdministracao(),
                medicamento.getFrequencia(),
                medicamento.getInstrucaoUso(),
                medicamento.getData(),
                medicamento.getHora() == null ? null : medicamento.getHora().toLocalTime(),
                medicamento.getIdoso().getId());
    }
}
