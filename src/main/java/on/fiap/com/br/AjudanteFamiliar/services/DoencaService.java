package on.fiap.com.br.AjudanteFamiliar.services;

import on.fiap.com.br.AjudanteFamiliar.dto.DoencaRequisicao;
import on.fiap.com.br.AjudanteFamiliar.dto.DoencaResposta;
import on.fiap.com.br.AjudanteFamiliar.models.Doenca;
import on.fiap.com.br.AjudanteFamiliar.repositories.DoencaRepository;
import on.fiap.com.br.AjudanteFamiliar.services.excecoes.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoencaService {

    private final DoencaRepository doencaRepository;
    private final IdosoService idosoService;

    @Transactional(readOnly = true)
    public List<DoencaResposta> listarPorIdoso(Long idIdoso) {
        idosoService.buscarEntidade(idIdoso);
        return doencaRepository.findByIdosoIdOrderByNomeAsc(idIdoso).stream()
                .map(DoencaResposta::de).toList();
    }

    @Transactional(readOnly = true)
    public DoencaResposta buscarPorId(Long id) {
        return DoencaResposta.de(buscarEntidade(id));
    }

    @Transactional
    public DoencaResposta cadastrar(Long idIdoso, DoencaRequisicao requisicao) {
        Doenca doenca = new Doenca();
        doenca.setIdoso(idosoService.buscarEntidade(idIdoso));
        aplicarDados(doenca, requisicao);
        return DoencaResposta.de(doencaRepository.save(doenca));
    }

    @Transactional
    public DoencaResposta atualizar(Long id, DoencaRequisicao requisicao) {
        Doenca doenca = buscarEntidade(id);
        aplicarDados(doenca, requisicao);
        return DoencaResposta.de(doenca);
    }

    @Transactional
    public void remover(Long id) {
        doencaRepository.delete(buscarEntidade(id));
    }

    private Doenca buscarEntidade(Long id) {
        return doencaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Doença", id));
    }

    private void aplicarDados(Doenca doenca, DoencaRequisicao requisicao) {
        doenca.setNome(requisicao.nome());
        doenca.setDescricao(requisicao.descricao());
    }
}
