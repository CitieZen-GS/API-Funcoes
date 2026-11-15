package on.fiap.com.br.AjudanteFamiliar.services;

import on.fiap.com.br.AjudanteFamiliar.dto.AtividadeRequisicao;
import on.fiap.com.br.AjudanteFamiliar.dto.AtividadeResposta;
import on.fiap.com.br.AjudanteFamiliar.models.Atividade;
import on.fiap.com.br.AjudanteFamiliar.repositories.AtividadeRepository;
import on.fiap.com.br.AjudanteFamiliar.services.excecoes.RecursoNaoEncontradoException;
import on.fiap.com.br.AjudanteFamiliar.services.excecoes.RegraNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AtividadeService {

    private final AtividadeRepository atividadeRepository;
    private final IdosoService idosoService;

    /** Lista todas as atividades do idoso ou só as de uma data. */
    @Transactional(readOnly = true)
    public List<AtividadeResposta> listarPorIdoso(Long idIdoso, LocalDate data) {
        idosoService.buscarEntidade(idIdoso);
        List<Atividade> atividades = data == null
                ? atividadeRepository.findByIdosoIdOrderByDataAscHoraInicioAsc(idIdoso)
                : atividadeRepository.findByIdosoIdAndDataOrderByHoraInicioAsc(idIdoso, data);
        return atividades.stream().map(AtividadeResposta::de).toList();
    }

    @Transactional(readOnly = true)
    public AtividadeResposta buscarPorId(Long id) {
        return AtividadeResposta.de(buscarEntidade(id));
    }

    @Transactional
    public AtividadeResposta cadastrar(Long idIdoso, AtividadeRequisicao requisicao) {
        validarHorarios(requisicao);
        Atividade atividade = new Atividade();
        atividade.setIdoso(idosoService.buscarEntidade(idIdoso));
        aplicarDados(atividade, requisicao);
        return AtividadeResposta.de(atividadeRepository.save(atividade));
    }

    @Transactional
    public AtividadeResposta atualizar(Long id, AtividadeRequisicao requisicao) {
        validarHorarios(requisicao);
        Atividade atividade = buscarEntidade(id);
        aplicarDados(atividade, requisicao);
        return AtividadeResposta.de(atividade);
    }

    @Transactional
    public void remover(Long id) {
        atividadeRepository.delete(buscarEntidade(id));
    }

    private Atividade buscarEntidade(Long id) {
        return atividadeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Atividade", id));
    }

    private void validarHorarios(AtividadeRequisicao requisicao) {
        if (requisicao.horaFim() == null) {
            return;
        }
        if (requisicao.horaInicio() == null) {
            throw new RegraNegocioException("Informe a hora de início quando houver hora de fim");
        }
        if (!requisicao.horaFim().isAfter(requisicao.horaInicio())) {
            throw new RegraNegocioException("A hora de fim deve ser depois da hora de início");
        }
    }

    private void aplicarDados(Atividade atividade, AtividadeRequisicao requisicao) {
        atividade.setNome(requisicao.nome());
        atividade.setDescricao(requisicao.descricao());
        atividade.setData(requisicao.data());
        atividade.setHoraInicio(requisicao.horaInicio() == null ? null
                : requisicao.data().atTime(requisicao.horaInicio()));
        atividade.setHoraFim(requisicao.horaFim() == null ? null
                : requisicao.data().atTime(requisicao.horaFim()));
    }
}
