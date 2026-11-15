package on.fiap.com.br.AjudanteFamiliar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public record AtividadeRequisicao(

        @NotBlank @Size(max = 20)
        String nome,

        @Size(max = 80)
        String descricao,

        @NotNull
        LocalDate data,

        LocalTime horaInicio,

        LocalTime horaFim
) {
}
