package on.fiap.com.br.AjudanteFamiliar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public record MedicamentoRequisicao(

        @NotBlank @Size(max = 20)
        String nome,

        @Size(max = 20)
        String viaAdministracao,

        @Size(max = 50)
        String frequencia,

        @Size(max = 100)
        String instrucaoUso,

        /** Data de início / da dose. Opcional. */
        LocalDate data,

        /** Horário da dose, ex.: "08:00". */
        @NotNull
        LocalTime hora
) {
}
