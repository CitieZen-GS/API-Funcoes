package on.fiap.com.br.AjudanteFamiliar.services;

import on.fiap.com.br.AjudanteFamiliar.dto.AlimentacaoRequisicao;
import on.fiap.com.br.AjudanteFamiliar.dto.AlimentacaoResposta;
import on.fiap.com.br.AjudanteFamiliar.models.Alimentacao;
import on.fiap.com.br.AjudanteFamiliar.models.enums.TipoAlimentacao;
import on.fiap.com.br.AjudanteFamiliar.repositories.AlimentacaoRepository;
import on.fiap.com.br.AjudanteFamiliar.services.excecoes.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlimentacaoService {

    private final AlimentacaoRepository alimentacaoRepository;
    private final IdosoService idosoService;

    /** Filtros opcionais por data e por tipo de refeição. */
    @Transactional(readOnly = true)
    public List<AlimentacaoResposta> listarPorIdoso(Long idIdoso, LocalDate data, TipoAlimentacao tipo) {
        idosoService.buscarEntidade(idIdoso);
        List<Alimentacao> alimentacoes;
        if (data != null && tipo != null) {
            alimentacoes = alimentacaoRepository
                    .findByIdosoIdAndDataConsumoAndTipoOrderByHoraConsumoAsc(idIdoso, data, tipo);
        } else if (data != null) {
            alimentacoes = alimentacaoRepository.findByIdosoIdAndDataConsumoOrderByHoraConsumoAsc(idIdoso, data);
        } else if (tipo != null) {
            alimentacoes = alimentacaoRepository
                    .findByIdosoIdAndTipoOrderByDataConsumoDescHoraConsumoDesc(idIdoso, tipo);
        } else {
            alimentacoes = alimentacaoRepository.findByIdosoIdOrderByDataConsumoDescHoraConsumoDesc(idIdoso);
        }
        return alimentacoes.stream().map(AlimentacaoResposta::de).toList();
    }

    @Transactional(readOnly = true)
    public AlimentacaoResposta buscarPorId(Long id) {
        return AlimentacaoResposta.de(buscarEntidade(id));
    }

    @Transactional
    public AlimentacaoResposta cadastrar(Long idIdoso, AlimentacaoRequisicao requisicao) {
        Alimentacao alimentacao = new Alimentacao();
        alimentacao.setIdoso(idosoService.buscarEntidade(idIdoso));
        aplicarDados(alimentacao, requisicao);
        return AlimentacaoResposta.de(alimentacaoRepository.save(alimentacao));
    }

    @Transactional
    public AlimentacaoResposta atualizar(Long id, AlimentacaoRequisicao requisicao) {
        Alimentacao alimentacao = buscarEntidade(id);
        aplicarDados(alimentacao, requisicao);
        return AlimentacaoResposta.de(alimentacao);
    }

    @Transactional
    public void remover(Long id) {
        alimentacaoRepository.delete(buscarEntidade(id));
    }

    private Alimentacao buscarEntidade(Long id) {
        return alimentacaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Alimentação", id));
    }

    private void aplicarDados(Alimentacao alimentacao, AlimentacaoRequisicao requisicao) {
        alimentacao.setNome(requisicao.nome());
        alimentacao.setTipo(requisicao.tipo());
        alimentacao.setDataConsumo(requisicao.dataConsumo());
        alimentacao.setHoraConsumo(requisicao.dataConsumo().atTime(requisicao.horaConsumo()));
        alimentacao.setCalorias(requisicao.calorias());
        alimentacao.setPreparacao(requisicao.preparacao());
    }
}
