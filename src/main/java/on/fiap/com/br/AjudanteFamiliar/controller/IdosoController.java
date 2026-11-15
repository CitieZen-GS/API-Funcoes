package on.fiap.com.br.AjudanteFamiliar.controller;

import on.fiap.com.br.AjudanteFamiliar.dto.IdosoRequisicao;
import on.fiap.com.br.AjudanteFamiliar.dto.IdosoResposta;
import on.fiap.com.br.AjudanteFamiliar.dto.ResumoIdosoResposta;
import on.fiap.com.br.AjudanteFamiliar.services.IdosoService;
import on.fiap.com.br.AjudanteFamiliar.services.ResumoIdosoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/idosos")
@RequiredArgsConstructor
public class IdosoController {

    private final IdosoService idosoService;
    private final ResumoIdosoService resumoIdosoService;

    @GetMapping
    public List<IdosoResposta> listar(@RequestParam(required = false) Long idCuidador) {
        return idosoService.listar(idCuidador);
    }

    @GetMapping("/{id}")
    public IdosoResposta buscarPorId(@PathVariable Long id) {
        return idosoService.buscarPorId(id);
    }

    /** Busca de dados: resumo do idoso no dia (padrão: hoje). */
    @GetMapping("/{id}/resumo")
    public ResumoIdosoResposta resumo(@PathVariable Long id,
                                      @RequestParam(required = false)
                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return resumoIdosoService.gerarResumo(id, data);
    }

    @PostMapping
    public ResponseEntity<IdosoResposta> cadastrar(@RequestBody @Valid IdosoRequisicao requisicao,
                                                   UriComponentsBuilder uriBuilder) {
        IdosoResposta resposta = idosoService.cadastrar(requisicao);
        var uri = uriBuilder.path("/idosos/{id}").buildAndExpand(resposta.id()).toUri();
        return ResponseEntity.created(uri).body(resposta);
    }

    @PutMapping("/{id}")
    public IdosoResposta atualizar(@PathVariable Long id, @RequestBody @Valid IdosoRequisicao requisicao) {
        return idosoService.atualizar(id, requisicao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        idosoService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
