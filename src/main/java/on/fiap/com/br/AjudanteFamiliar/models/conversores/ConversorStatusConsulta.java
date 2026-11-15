package on.fiap.com.br.AjudanteFamiliar.models.conversores;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import on.fiap.com.br.AjudanteFamiliar.models.enums.StatusConsulta;

@Converter
public class ConversorStatusConsulta implements AttributeConverter<StatusConsulta, String> {

    @Override
    public String convertToDatabaseColumn(StatusConsulta status) {
        return status == null ? null : status.getValorBanco();
    }

    @Override
    public StatusConsulta convertToEntityAttribute(String valor) {
        return StatusConsulta.doValorBanco(valor);
    }
}
