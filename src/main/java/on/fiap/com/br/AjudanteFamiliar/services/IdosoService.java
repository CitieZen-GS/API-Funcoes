package on.fiap.com.br.AjudanteFamiliar.services;

import on.fiap.com.br.AjudanteFamiliar.dto.IdosoRequisicao;
import on.fiap.com.br.AjudanteFamiliar.dto.IdosoResposta;
import on.fiap.com.br.AjudanteFamiliar.models.Cuidador;
import on.fiap.com.br.AjudanteFamiliar.models.Idoso;
import on.fiap.com.br.AjudanteFamiliar.repositories.CuidadorRepository;
import on.fiap.com.br.AjudanteFamiliar.repositories.IdosoRepository;
import on.fiap.com.br.AjudanteFamiliar.services.excecoes.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IdosoService {

    private final IdosoRepository idosoRepository;
    private final CuidadorRepository cuidadorRepository;

    @Transactional(readOnly = true)
    public List<IdosoResposta> listar(Long idCuidador) {
        List<Idoso> idosos = idCuidador == null
                ? idosoRepository.findAllByOrderByNomeAsc()
                : idosoRepository.findByCuidadorIdOrderByNomeAsc(idCuidador);
        return idosos.stream().map(IdosoResposta::de).toList();
    }

    @Transactional(readOnly = true)
    public IdosoResposta buscarPorId(Long id) {
        return IdosoResposta.de(buscarEntidade(id));
    }

    @Transactional
    public IdosoResposta cadastrar(IdosoRequisicao requisicao) {
        Idoso idoso = new Idoso();
        aplicarDados(idoso, requisicao);
        return IdosoResposta.de(idosoRepository.save(idoso));
    }

    @Transactional
    public IdosoResposta atualizar(Long id, IdosoRequisicao requisicao) {
        Idoso idoso = buscarEntidade(id);
        aplicarDados(idoso, requisicao);
        return IdosoResposta.de(idoso);
    }

    @Transactional
    public void remover(Long id) {
        idosoRepository.delete(buscarEntidade(id));
        // Força o DELETE aqui para que uma violação de FK seja tratada como 409
        idosoRepository.flush();
    }

    /** Usado pelos outros services para validar e vincular o idoso. */
    @Transactional(readOnly = true)
    public Idoso buscarEntidade(Long id) {
        return idosoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Idoso", id));
    }

    private void aplicarDados(Idoso idoso, IdosoRequisicao requisicao) {
        Cuidador cuidador = cuidadorRepository.findById(requisicao.idCuidador())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cuidador", requisicao.idCuidador()));
        idoso.setNome(requisicao.nome());
        idoso.setCpf(requisicao.cpf());
        idoso.setAltura(requisicao.altura());
        idoso.setPeso(requisicao.peso());
        idoso.setDataNascimento(requisicao.dataNascimento());
        idoso.setComportamento(requisicao.comportamento());
        idoso.setCuidador(cuidador);
    }
}
