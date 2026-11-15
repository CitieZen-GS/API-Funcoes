package on.fiap.com.br.AjudanteFamiliar.models.enums;

import lombok.Getter;

/**
 * Status aceitos pela constraint ck_consulta_status.
 */
@Getter
public enum StatusConsulta {

    PENDENTE("Pendente"),
    REALIZADO("Realizado");

    /** Valor gravado na coluna st_consulta. */
    private final String valorBanco;

    StatusConsulta(String valorBanco) {
        this.valorBanco = valorBanco;
    }

    public static StatusConsulta doValorBanco(String valor) {
        if (valor == null) {
            return null;
        }
        for (StatusConsulta status : values()) {
            if (status.valorBanco.equals(valor)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Status de consulta desconhecido: " + valor);
    }
}
