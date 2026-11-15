package on.fiap.com.br.AjudanteFamiliar.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record IdosoRequisicao(

        @NotBlank @Size(max = 20)
        String nome,

        @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11}",
                message = "deve estar no formato 000.000.000-00 ou conter 11 dígitos")
        String cpf,

        @DecimalMin("0.30") @DecimalMax("2.50") @Digits(integer = 1, fraction = 2)
        BigDecimal altura,

        @DecimalMin("1.00") @Digits(integer = 3, fraction = 2)
        BigDecimal peso,

        @NotNull @Past
        LocalDate dataNascimento,

        @Size(max = 100)
        String comportamento,

        @NotNull
        Long idCuidador
) {
}
