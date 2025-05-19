package br.com.cooperativa.votacao.api;

import br.com.cooperativa.votacao.application.dto.SessaoDTO;
import br.com.cooperativa.votacao.application.dto.SessaoResultadoDTO;
import br.com.cooperativa.votacao.application.service.SessaoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sessoes")
@RequiredArgsConstructor
public class SessaoController {
    private final SessaoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Abre uma sessão de votação para uma pauta")
    public SessaoResultadoDTO abrirSessao(@Validated @RequestBody SessaoDTO dto) {
        return service.abrirSessao(dto);
    }

    @GetMapping("/{pautaId}/resultado")
    @Operation(summary = "Obtém resultado da sessão de votação de uma pauta")
    public SessaoResultadoDTO resultado(@PathVariable Long pautaId) {
        return service.resultado(pautaId);
    }

    @GetMapping
    @Operation(summary = "Lista todas as sessões de votação")
    public List<SessaoDTO> listar() {
        return service.listar();
    }
}
