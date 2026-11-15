package on.fiap.com.br.AjudanteFamiliar.models.enums;

import lombok.Getter;

/**
 * Tipos de refeição aceitos pela constraint ck_alimentacao_tipo.
 */
@Getter
public enum TipoAlimentacao {

    CAFE_DA_MANHA("Cafe da manha"),
    ALMOCO("Almoco"),
    LANCHE_DE_TARDE("Lanche de tarde"),
    JANTAR("Jantar");

    /** Valor gravado na coluna tp_alimentacao. */
    private final String valorBanco;

    TipoAlimentacao(String valorBanco) {
        this.valorBanco = valorBanco;
    }

    public static TipoAlimentacao doValorBanco(String valor) {
        if (valor == null) {
            return null;
        }
        for (TipoAlimentacao tipo : values()) {
            if (tipo.valorBanco.equals(valor)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de alimentação desconhecido: " + valor);
    }
}
