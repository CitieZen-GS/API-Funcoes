package on.fiap.com.br.AjudanteFamiliar.controller;

import on.fiap.com.br.AjudanteFamiliar.dto.AtividadeRequisicao;
import on.fiap.com.br.AjudanteFamiliar.dto.AtividadeResposta;
import on.fiap.com.br.AjudanteFamiliar.services.AtividadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

@RestController
@Tag(name = "Atividades", description = "Atividades a serem feitas com o idoso")
@RequiredArgsConstructor
public class AtividadeController {

    private final AtividadeService atividadeService;

    /** ?data=2026-11-15 filtra as atividades do dia. */
    @Operation(summary = "Lista as atividades do idoso (filtro opcional por dia)")
    @GetMapping("/idosos/{idIdoso}/atividades")
    public List<AtividadeResposta> listar(@PathVariable Long idIdoso,
                                          @RequestParam(required = false)
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return atividadeService.listarPorIdoso(idIdoso, data);
    }

    @Operation(summary = "Cadastra uma atividade")
    @PostMapping("/idosos/{idIdoso}/atividades")
    public ResponseEntity<AtividadeResposta> cadastrar(@PathVariable Long idIdoso,
                                                       @RequestBody @Valid AtividadeRequisicao requisicao,
                                                       UriComponentsBuilder uriBuilder) {
        AtividadeResposta resposta = atividadeService.cadastrar(idIdoso, requisicao);
        var uri = uriBuilder.path("/atividades/{id}").buildAndExpand(resposta.id()).toUri();
        return ResponseEntity.created(uri).body(resposta);
    }

    @Operation(summary = "Busca uma atividade")
    @GetMapping("/atividades/{id}")
    public AtividadeResposta buscarPorId(@PathVariable Long id) {
        return atividadeService.buscarPorId(id);
    }

    @Operation(summary = "Atualiza uma atividade")
    @PutMapping("/atividades/{id}")
    public AtividadeResposta atualizar(@PathVariable Long id, @RequestBody @Valid AtividadeRequisicao requisicao) {
        return atividadeService.atualizar(id, requisicao);
    }

    @Operation(summary = "Remove uma atividade")
    @DeleteMapping("/atividades/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        atividadeService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
