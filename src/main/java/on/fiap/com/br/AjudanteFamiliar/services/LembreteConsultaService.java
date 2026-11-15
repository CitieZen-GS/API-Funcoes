package on.fiap.com.br.AjudanteFamiliar.services;

import on.fiap.com.br.AjudanteFamiliar.dto.LembreteConsultaRequisicao;
import on.fiap.com.br.AjudanteFamiliar.dto.LembreteConsultaResposta;
import on.fiap.com.br.AjudanteFamiliar.models.ConsultaMedicamento;
import on.fiap.com.br.AjudanteFamiliar.models.LembreteConsulta;
import on.fiap.com.br.AjudanteFamiliar.models.Medicamento;
import on.fiap.com.br.AjudanteFamiliar.models.enums.StatusConsulta;
import on.fiap.com.br.AjudanteFamiliar.repositories.ConsultaMedicamentoRepository;
import on.fiap.com.br.AjudanteFamiliar.repositories.LembreteConsultaRepository;
import on.fiap.com.br.AjudanteFamiliar.services.excecoes.RecursoNaoEncontradoException;
import on.fiap.com.br.AjudanteFamiliar.services.excecoes.RegraNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LembreteConsultaService {

    private final LembreteConsultaRepository lembreteConsultaRepository;
    private final ConsultaMedicamentoRepository consultaMedicamentoRepository;
    private final IdosoService idosoService;
    private final MedicamentoService medicamentoService;

    @Transactional(readOnly = true)
    public List<LembreteConsultaResposta> listarPorIdoso(Long idIdoso, StatusConsulta status) {
        idosoService.buscarEntidade(idIdoso);
        List<LembreteConsulta> lembretes = status == null
                ? lembreteConsultaRepository.findByIdosoIdOrderByDataConsultaAscHoraConsultaAsc(idIdoso)
                : lembreteConsultaRepository.findByIdosoIdAndStatusOrderByDataConsultaAscHoraConsultaAsc(idIdoso, status);
        return lembretes.stream().map(LembreteConsultaResposta::de).toList();
    }

    @Transactional(readOnly = true)
    public LembreteConsultaResposta buscarPorId(Long id) {
        return LembreteConsultaResposta.de(buscarEntidade(id));
    }

    /** Lembretes novos são sempre de consultas futuras (ou de hoje) e começam como PENDENTE. */
    @Transactional
    public LembreteConsultaResposta cadastrar(Long idIdoso, LembreteConsultaRequisicao requisicao) {
        if (requisicao.dataConsulta().isBefore(LocalDate.now())) {
            throw new RegraNegocioException("A data da consulta não pode estar no passado");
        }
        LembreteConsulta lembrete = new LembreteConsulta();
        lembrete.setIdoso(idosoService.buscarEntidade(idIdoso));
        lembrete.setStatus(StatusConsulta.PENDENTE);
        aplicarDados(lembrete, requisicao);
        return LembreteConsultaResposta.de(lembreteConsultaRepository.save(lembrete));
    }

    @Transactional
    public LembreteConsultaResposta atualizar(Long id, LembreteConsultaRequisicao requisicao) {
        LembreteConsulta lembrete = buscarEntidade(id);
        aplicarDados(lembrete, requisicao);
        return LembreteConsultaResposta.de(lembrete);
    }

    @Transactional
    public LembreteConsultaResposta alterarStatus(Long id, StatusConsulta status) {
        LembreteConsulta lembrete = buscarEntidade(id);
        lembrete.setStatus(status);
        return LembreteConsultaResposta.de(lembrete);
    }

    /** Remove também os vínculos do lembrete com medicamentos. */
    @Transactional
    public void remover(Long id) {
        LembreteConsulta lembrete = buscarEntidade(id);
        consultaMedicamentoRepository.deleteByLembreteConsultaId(id);
        lembreteConsultaRepository.delete(lembrete);
    }

    /** Vincula um medicamento (do mesmo idoso) ao lembrete, ex.: remédio a levar ou revisar na consulta. */
    @Transactional
    public LembreteConsultaResposta vincularMedicamento(Long idLembrete, Long idMedicamento) {
        LembreteConsulta lembrete = buscarEntidade(idLembrete);
        Medicamento medicamento = medicamentoService.buscarEntidade(idMedicamento);

        if (!medicamento.getIdoso().getId().equals(lembrete.getIdoso().getId())) {
            throw new RegraNegocioException("O medicamento pertence a outro idoso");
        }
        if (consultaMedicamentoRepository.existsByLembreteConsultaIdAndMedicamentoId(idLembrete, idMedicamento)) {
            throw new RegraNegocioException("Medicamento já vinculado a este lembrete");
        }

        ConsultaMedicamento vinculo = new ConsultaMedicamento();
        vinculo.setLembreteConsulta(lembrete);
        vinculo.setMedicamento(medicamento);
        consultaMedicamentoRepository.save(vinculo);
        lembrete.getMedicamentos().add(vinculo);
        return LembreteConsultaResposta.de(lembrete);
    }

    @Transactional
    public LembreteConsultaResposta desvincularMedicamento(Long idLembrete, Long idMedicamento) {
        LembreteConsulta lembrete = buscarEntidade(idLembrete);
        ConsultaMedicamento vinculo = consultaMedicamentoRepository
                .findByLembreteConsultaIdAndMedicamentoId(idLembrete, idMedicamento)
                .orElseThrow(() -> new RegraNegocioException("Medicamento não está vinculado a este lembrete"));
        lembrete.getMedicamentos().remove(vinculo);
        consultaMedicamentoRepository.delete(vinculo);
        return LembreteConsultaResposta.de(lembrete);
    }

    private LembreteConsulta buscarEntidade(Long id) {
        return lembreteConsultaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Lembrete de consulta", id));
    }

    private void aplicarDados(LembreteConsulta lembrete, LembreteConsultaRequisicao requisicao) {
        lembrete.setTitulo(requisicao.titulo());
        lembrete.setNomeMedico(requisicao.nomeMedico());
        lembrete.setDataConsulta(requisicao.dataConsulta());
        lembrete.setHoraConsulta(requisicao.horaConsulta() == null ? null
                : requisicao.dataConsulta().atTime(requisicao.horaConsulta()));
        lembrete.setDescricao(requisicao.descricao());
    }
}
