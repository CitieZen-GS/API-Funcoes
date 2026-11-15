package on.fiap.com.br.AjudanteFamiliar.dto;

import on.fiap.com.br.AjudanteFamiliar.models.Idoso;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

public record IdosoResposta(
        Long id,
        String nome,
        String cpf,
        BigDecimal altura,
        BigDecimal peso,
        LocalDate dataNascimento,
        Integer idade,
        String comportamento,
        Long idCuidador
) {

    public static IdosoResposta de(Idoso idoso) {
        Integer idade = idoso.getDataNascimento() == null ? null
                : Period.between(idoso.getDataNascimento(), LocalDate.now()).getYears();
        return new IdosoResposta(
                idoso.getId(),
                idoso.getNome(),
                idoso.getCpf(),
                idoso.getAltura(),
                idoso.getPeso(),
                idoso.getDataNascimento(),
                idade,
                idoso.getComportamento(),
                idoso.getCuidador().getId());
    }
}
