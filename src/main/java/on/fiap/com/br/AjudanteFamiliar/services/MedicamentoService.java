package on.fiap.com.br.AjudanteFamiliar.services;

import on.fiap.com.br.AjudanteFamiliar.dto.MedicamentoRequisicao;
import on.fiap.com.br.AjudanteFamiliar.dto.MedicamentoResposta;
import on.fiap.com.br.AjudanteFamiliar.models.Medicamento;
import on.fiap.com.br.AjudanteFamiliar.repositories.ConsultaMedicamentoRepository;
import on.fiap.com.br.AjudanteFamiliar.repositories.MedicamentoRepository;
import on.fiap.com.br.AjudanteFamiliar.services.excecoes.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicamentoService {

    private final MedicamentoRepository medicamentoRepository;
    private final ConsultaMedicamentoRepository consultaMedicamentoRepository;
    private final IdosoService idosoService;

    @Transactional(readOnly = true)
    public List<MedicamentoResposta> listarPorIdoso(Long idIdoso) {
        idosoService.buscarEntidade(idIdoso);
        return medicamentoRepository.findByIdosoIdOrderByHoraAsc(idIdoso).stream()
                .map(MedicamentoResposta::de).toList();
    }

    @Transactional(readOnly = true)
    public MedicamentoResposta buscarPorId(Long id) {
        return MedicamentoResposta.de(buscarEntidade(id));
    }

    @Transactional
    public MedicamentoResposta cadastrar(Long idIdoso, MedicamentoRequisicao requisicao) {
        Medicamento medicamento = new Medicamento();
        medicamento.setIdoso(idosoService.buscarEntidade(idIdoso));
        aplicarDados(medicamento, requisicao);
        return MedicamentoResposta.de(medicamentoRepository.save(medicamento));
    }

    @Transactional
    public MedicamentoResposta atualizar(Long id, MedicamentoRequisicao requisicao) {
        Medicamento medicamento = buscarEntidade(id);
        aplicarDados(medicamento, requisicao);
        return MedicamentoResposta.de(medicamento);
    }

    /** Remove também os vínculos do medicamento com lembretes de consulta. */
    @Transactional
    public void remover(Long id) {
        Medicamento medicamento = buscarEntidade(id);
        consultaMedicamentoRepository.deleteByMedicamentoId(id);
        medicamentoRepository.delete(medicamento);
    }

    @Transactional(readOnly = true)
    public Medicamento buscarEntidade(Long id) {
        return medicamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Medicamento", id));
    }

    private void aplicarDados(Medicamento medicamento, MedicamentoRequisicao requisicao) {
        medicamento.setNome(requisicao.nome());
        medicamento.setViaAdministracao(requisicao.viaAdministracao());
        medicamento.setFrequencia(requisicao.frequencia());
        medicamento.setInstrucaoUso(requisicao.instrucaoUso());
        medicamento.setData(requisicao.data());
        // A coluna hr_medicamento é DATE (data + hora): sem data informada, usa o dia do cadastro
        LocalDate dataBase = requisicao.data() != null ? requisicao.data() : LocalDate.now();
        medicamento.setHora(dataBase.atTime(requisicao.hora()));
    }
}
