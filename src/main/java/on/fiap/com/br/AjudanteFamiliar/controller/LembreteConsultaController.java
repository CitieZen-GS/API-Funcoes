package on.fiap.com.br.AjudanteFamiliar.controller;

import on.fiap.com.br.AjudanteFamiliar.dto.LembreteConsultaRequisicao;
import on.fiap.com.br.AjudanteFamiliar.dto.LembreteConsultaResposta;
import on.fiap.com.br.AjudanteFamiliar.dto.StatusConsultaRequisicao;
import on.fiap.com.br.AjudanteFamiliar.models.enums.StatusConsulta;
import on.fiap.com.br.AjudanteFamiliar.services.LembreteConsultaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LembreteConsultaController {

    private final LembreteConsultaService lembreteConsultaService;

    /** Filtro opcional: ?status=PENDENTE ou ?status=REALIZADO */
    @GetMapping("/idosos/{idIdoso}/lembretes-consulta")
    public List<LembreteConsultaResposta> listar(@PathVariable Long idIdoso,
                                                 @RequestParam(required = false) StatusConsulta status) {
        return lembreteConsultaService.listarPorIdoso(idIdoso, status);
    }

    @PostMapping("/idosos/{idIdoso}/lembretes-consulta")
    public ResponseEntity<LembreteConsultaResposta> cadastrar(@PathVariable Long idIdoso,
                                                              @RequestBody @Valid LembreteConsultaRequisicao requisicao,
                                                              UriComponentsBuilder uriBuilder) {
        LembreteConsultaResposta resposta = lembreteConsultaService.cadastrar(idIdoso, requisicao);
        var uri = uriBuilder.path("/lembretes-consulta/{id}").buildAndExpand(resposta.id()).toUri();
        return ResponseEntity.created(uri).body(resposta);
    }

    @GetMapping("/lembretes-consulta/{id}")
    public LembreteConsultaResposta buscarPorId(@PathVariable Long id) {
        return lembreteConsultaService.buscarPorId(id);
    }

    @PutMapping("/lembretes-consulta/{id}")
    public LembreteConsultaResposta atualizar(@PathVariable Long id,
                                              @RequestBody @Valid LembreteConsultaRequisicao requisicao) {
        return lembreteConsultaService.atualizar(id, requisicao);
    }

    /** Marca a consulta como REALIZADO (ou volta para PENDENTE). */
    @PatchMapping("/lembretes-consulta/{id}/status")
    public LembreteConsultaResposta alterarStatus(@PathVariable Long id,
                                                  @RequestBody @Valid StatusConsultaRequisicao requisicao) {
        return lembreteConsultaService.alterarStatus(id, requisicao.status());
    }

    @DeleteMapping("/lembretes-consulta/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        lembreteConsultaService.remover(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/lembretes-consulta/{id}/medicamentos/{idMedicamento}")
    public LembreteConsultaResposta vincularMedicamento(@PathVariable Long id, @PathVariable Long idMedicamento) {
        return lembreteConsultaService.vincularMedicamento(id, idMedicamento);
    }

    @DeleteMapping("/lembretes-consulta/{id}/medicamentos/{idMedicamento}")
    public LembreteConsultaResposta desvincularMedicamento(@PathVariable Long id,
                                                           @PathVariable Long idMedicamento) {
        return lembreteConsultaService.desvincularMedicamento(id, idMedicamento);
    }
}
