package on.fiap.com.br.AjudanteFamiliar.controller;

import on.fiap.com.br.AjudanteFamiliar.dto.MedicamentoRequisicao;
import on.fiap.com.br.AjudanteFamiliar.dto.MedicamentoResposta;
import on.fiap.com.br.AjudanteFamiliar.services.MedicamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@Tag(name = "Medicamentos", description = "Medicamentos e horários de dose do idoso")
@RequiredArgsConstructor
public class MedicamentoController {

    private final MedicamentoService medicamentoService;

    @Operation(summary = "Lista os medicamentos do idoso (ordenados por horário)")
    @GetMapping("/idosos/{idIdoso}/medicamentos")
    public List<MedicamentoResposta> listar(@PathVariable Long idIdoso) {
        return medicamentoService.listarPorIdoso(idIdoso);
    }

    @Operation(summary = "Cadastra um medicamento no idoso")
    @PostMapping("/idosos/{idIdoso}/medicamentos")
    public ResponseEntity<MedicamentoResposta> cadastrar(@PathVariable Long idIdoso,
                                                         @RequestBody @Valid MedicamentoRequisicao requisicao,
                                                         UriComponentsBuilder uriBuilder) {
        MedicamentoResposta resposta = medicamentoService.cadastrar(idIdoso, requisicao);
        var uri = uriBuilder.path("/medicamentos/{id}").buildAndExpand(resposta.id()).toUri();
        return ResponseEntity.created(uri).body(resposta);
    }

    @Operation(summary = "Busca um medicamento")
    @GetMapping("/medicamentos/{id}")
    public MedicamentoResposta buscarPorId(@PathVariable Long id) {
        return medicamentoService.buscarPorId(id);
    }

    @Operation(summary = "Atualiza um medicamento")
    @PutMapping("/medicamentos/{id}")
    public MedicamentoResposta atualizar(@PathVariable Long id,
                                         @RequestBody @Valid MedicamentoRequisicao requisicao) {
        return medicamentoService.atualizar(id, requisicao);
    }

    @Operation(summary = "Remove um medicamento (e seus vínculos com lembretes)")
    @DeleteMapping("/medicamentos/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        medicamentoService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
