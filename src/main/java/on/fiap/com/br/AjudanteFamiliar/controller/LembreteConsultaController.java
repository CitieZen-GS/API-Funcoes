package on.fiap.com.br.AjudanteFamiliar.controller;

import on.fiap.com.br.AjudanteFamiliar.dto.LembreteConsultaRequisicao;
import on.fiap.com.br.AjudanteFamiliar.dto.LembreteConsultaResposta;
import on.fiap.com.br.AjudanteFamiliar.dto.StatusConsultaRequisicao;
import on.fiap.com.br.AjudanteFamiliar.models.enums.StatusConsulta;
import on.fiap.com.br.AjudanteFamiliar.services.LembreteConsultaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@Tag(name = "Lembretes de consulta", description = "Calendário de consultas futuras do idoso")
@RequiredArgsConstructor
public class LembreteConsultaController {

    private final LembreteConsultaService lembreteConsultaService;

    /** Filtro opcional: ?status=PENDENTE ou ?status=REALIZADO */
    @Operation(summary = "Lista os lembretes do idoso (filtro opcional por status)")
    @GetMapping("/idosos/{idIdoso}/lembretes-consulta")
    public List<LembreteConsultaResposta> listar(@PathVariable Long idIdoso,
                                                 @RequestParam(required = false) StatusConsulta status) {
        return lembreteConsultaService.listarPorIdoso(idIdoso, status);
    }

    @Operation(summary = "Cria um lembrete de consulta (status PENDENTE)")
    @PostMapping("/idosos/{idIdoso}/lembretes-consulta")
    public ResponseEntity<LembreteConsultaResposta> cadastrar(@PathVariable Long idIdoso,
                                                              @RequestBody @Valid LembreteConsultaRequisicao requisicao,
                                                              UriComponentsBuilder uriBuilder) {
        LembreteConsultaResposta resposta = lembreteConsultaService.cadastrar(idIdoso, requisicao);
        var uri = uriBuilder.path("/lembretes-consulta/{id}").buildAndExpand(resposta.id()).toUri();
        return ResponseEntity.created(uri).body(resposta);
    }

    @Operation(summary = "Busca um lembrete")
    @GetMapping("/lembretes-consulta/{id}")
    public LembreteConsultaResposta buscarPorId(@PathVariable Long id) {
        return lembreteConsultaService.buscarPorId(id);
    }

    @Operation(summary = "Atualiza um lembrete")
    @PutMapping("/lembretes-consulta/{id}")
    public LembreteConsultaResposta atualizar(@PathVariable Long id,
                                              @RequestBody @Valid LembreteConsultaRequisicao requisicao) {
        return lembreteConsultaService.atualizar(id, requisicao);
    }

    /** Marca a consulta como REALIZADO (ou volta para PENDENTE). */
    @Operation(summary = "Marca a consulta como REALIZADO ou PENDENTE")
    @PatchMapping("/lembretes-consulta/{id}/status")
    public LembreteConsultaResposta alterarStatus(@PathVariable Long id,
                                                  @RequestBody @Valid StatusConsultaRequisicao requisicao) {
        return lembreteConsultaService.alterarStatus(id, requisicao.status());
    }

    @Operation(summary = "Remove um lembrete (e seus vínculos com medicamentos)")
    @DeleteMapping("/lembretes-consulta/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        lembreteConsultaService.remover(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Vincula um medicamento do mesmo idoso ao lembrete")
    @PostMapping("/lembretes-consulta/{id}/medicamentos/{idMedicamento}")
    public LembreteConsultaResposta vincularMedicamento(@PathVariable Long id, @PathVariable Long idMedicamento) {
        return lembreteConsultaService.vincularMedicamento(id, idMedicamento);
    }

    @Operation(summary = "Desvincula um medicamento do lembrete")
    @DeleteMapping("/lembretes-consulta/{id}/medicamentos/{idMedicamento}")
    public LembreteConsultaResposta desvincularMedicamento(@PathVariable Long id,
                                                           @PathVariable Long idMedicamento) {
        return lembreteConsultaService.desvincularMedicamento(id, idMedicamento);
    }
}
