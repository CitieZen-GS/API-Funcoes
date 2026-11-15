package on.fiap.com.br.AjudanteFamiliar.dto;

import on.fiap.com.br.AjudanteFamiliar.models.Alimentacao;
import on.fiap.com.br.AjudanteFamiliar.models.enums.TipoAlimentacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record AlimentacaoResposta(
        Long id,
        String nome,
        TipoAlimentacao tipo,
        LocalDate dataConsumo,
        LocalTime horaConsumo,
        BigDecimal calorias,
        String preparacao,
        Long idIdoso
) {

    public static AlimentacaoResposta de(Alimentacao alimentacao) {
        return new AlimentacaoResposta(
                alimentacao.getId(),
                alimentacao.getNome(),
                alimentacao.getTipo(),
                alimentacao.getDataConsumo(),
                alimentacao.getHoraConsumo() == null ? null : alimentacao.getHoraConsumo().toLocalTime(),
                alimentacao.getCalorias(),
                alimentacao.getPreparacao(),
                alimentacao.getIdoso().getId());
    }
}
