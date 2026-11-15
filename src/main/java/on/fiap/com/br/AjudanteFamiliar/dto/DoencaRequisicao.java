package on.fiap.com.br.AjudanteFamiliar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DoencaRequisicao(

        @NotBlank @Size(max = 20)
        String nome,

        @Size(max = 20)
        String descricao
) {
}
