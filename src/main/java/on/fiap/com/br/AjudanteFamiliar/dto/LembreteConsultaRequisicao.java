package on.fiap.com.br.AjudanteFamiliar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public record LembreteConsultaRequisicao(

        @NotBlank @Size(max = 20)
        String titulo,

        @Size(max = 20)
        String nomeMedico,

        @NotNull
        LocalDate dataConsulta,

        LocalTime horaConsulta,

        @Size(max = 100)
        String descricao
) {
}
