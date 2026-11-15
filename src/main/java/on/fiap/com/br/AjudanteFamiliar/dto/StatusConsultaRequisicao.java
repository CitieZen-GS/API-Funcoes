package on.fiap.com.br.AjudanteFamiliar.dto;

import on.fiap.com.br.AjudanteFamiliar.models.enums.StatusConsulta;
import jakarta.validation.constraints.NotNull;

/** Corpo do PATCH de status: PENDENTE ou REALIZADO. */
public record StatusConsultaRequisicao(@NotNull StatusConsulta status) {
}
