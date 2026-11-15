package on.fiap.com.br.AjudanteFamiliar.controller;

import on.fiap.com.br.AjudanteFamiliar.dto.DoencaRequisicao;
import on.fiap.com.br.AjudanteFamiliar.dto.DoencaResposta;
import on.fiap.com.br.AjudanteFamiliar.services.DoencaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DoencaController {

    private final DoencaService doencaService;

    @GetMapping("/idosos/{idIdoso}/doencas")
    public List<DoencaResposta> listar(@PathVariable Long idIdoso) {
        return doencaService.listarPorIdoso(idIdoso);
    }

    @PostMapping("/idosos/{idIdoso}/doencas")
    public ResponseEntity<DoencaResposta> cadastrar(@PathVariable Long idIdoso,
                                                    @RequestBody @Valid DoencaRequisicao requisicao,
                                                    UriComponentsBuilder uriBuilder) {
        DoencaResposta resposta = doencaService.cadastrar(idIdoso, requisicao);
        var uri = uriBuilder.path("/doencas/{id}").buildAndExpand(resposta.id()).toUri();
        return ResponseEntity.created(uri).body(resposta);
    }

    @GetMapping("/doencas/{id}")
    public DoencaResposta buscarPorId(@PathVariable Long id) {
        return doencaService.buscarPorId(id);
    }

    @PutMapping("/doencas/{id}")
    public DoencaResposta atualizar(@PathVariable Long id, @RequestBody @Valid DoencaRequisicao requisicao) {
        return doencaService.atualizar(id, requisicao);
    }

    @DeleteMapping("/doencas/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        doencaService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
