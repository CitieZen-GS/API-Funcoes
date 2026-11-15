package on.fiap.com.br.AjudanteFamiliar.dto;

import on.fiap.com.br.AjudanteFamiliar.models.Doenca;

public record DoencaResposta(Long id, String nome, String descricao, Long idIdoso) {

    public static DoencaResposta de(Doenca doenca) {
        return new DoencaResposta(doenca.getId(), doenca.getNome(), doenca.getDescricao(),
                doenca.getIdoso().getId());
    }
}
