package on.fiap.com.br.AjudanteFamiliar.dto;

import on.fiap.com.br.AjudanteFamiliar.models.enums.TipoAlimentacao;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record AlimentacaoRequisicao(

        @NotBlank @Size(max = 20)
        String nome,

        /** CAFE_DA_MANHA, ALMOCO, LANCHE_DE_TARDE ou JANTAR. */
        @NotNull
        TipoAlimentacao tipo,

        @NotNull @PastOrPresent
        LocalDate dataConsumo,

        @NotNull
        LocalTime horaConsumo,

        @DecimalMin("0.00") @Digits(integer = 4, fraction = 2)
        BigDecimal calorias,

        @Size(max = 100)
        String preparacao
) {
}
