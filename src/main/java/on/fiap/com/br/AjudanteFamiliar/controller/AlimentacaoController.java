package on.fiap.com.br.AjudanteFamiliar.controller;

import on.fiap.com.br.AjudanteFamiliar.dto.AlimentacaoRequisicao;
import on.fiap.com.br.AjudanteFamiliar.dto.AlimentacaoResposta;
import on.fiap.com.br.AjudanteFamiliar.models.enums.TipoAlimentacao;
import on.fiap.com.br.AjudanteFamiliar.services.AlimentacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AlimentacaoController {

    private final AlimentacaoService alimentacaoService;

    /** Filtros opcionais: ?data=2026-11-15&tipo=ALMOCO */
    @GetMapping("/idosos/{idIdoso}/alimentacoes")
    public List<AlimentacaoResposta> listar(@PathVariable Long idIdoso,
                                            @RequestParam(required = false)
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                                            @RequestParam(required = false) TipoAlimentacao tipo) {
        return alimentacaoService.listarPorIdoso(idIdoso, data, tipo);
    }

    @PostMapping("/idosos/{idIdoso}/alimentacoes")
    public ResponseEntity<AlimentacaoResposta> cadastrar(@PathVariable Long idIdoso,
                                                         @RequestBody @Valid AlimentacaoRequisicao requisicao,
                                                         UriComponentsBuilder uriBuilder) {
        AlimentacaoResposta resposta = alimentacaoService.cadastrar(idIdoso, requisicao);
        var uri = uriBuilder.path("/alimentacoes/{id}").buildAndExpand(resposta.id()).toUri();
        return ResponseEntity.created(uri).body(resposta);
    }

    @GetMapping("/alimentacoes/{id}")
    public AlimentacaoResposta buscarPorId(@PathVariable Long id) {
        return alimentacaoService.buscarPorId(id);
    }

    @PutMapping("/alimentacoes/{id}")
    public AlimentacaoResposta atualizar(@PathVariable Long id,
                                         @RequestBody @Valid AlimentacaoRequisicao requisicao) {
        return alimentacaoService.atualizar(id, requisicao);
    }

    @DeleteMapping("/alimentacoes/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        alimentacaoService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
