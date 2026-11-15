package on.fiap.com.br.AjudanteFamiliar.models.conversores;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import on.fiap.com.br.AjudanteFamiliar.models.enums.TipoAlimentacao;

@Converter
public class ConversorTipoAlimentacao implements AttributeConverter<TipoAlimentacao, String> {

    @Override
    public String convertToDatabaseColumn(TipoAlimentacao tipo) {
        return tipo == null ? null : tipo.getValorBanco();
    }

    @Override
    public TipoAlimentacao convertToEntityAttribute(String valor) {
        return TipoAlimentacao.doValorBanco(valor);
    }
}
