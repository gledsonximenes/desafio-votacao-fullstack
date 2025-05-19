package br.com.cooperativa.votacao.api;

import br.com.cooperativa.votacao.application.dto.SessaoResultadoDTO;
import br.com.cooperativa.votacao.application.dto.VotoDTO;
import br.com.cooperativa.votacao.application.service.VotoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/votos")
@RequiredArgsConstructor
public class VotoController {
    private final VotoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registra um voto em uma pauta")
    public void votar(@Validated @RequestBody VotoDTO dto) {
        service.votar(dto);
    }

    @GetMapping("/{pautaId}/resultado")
    @Operation(summary = "Reconta votos de uma pauta")
    public SessaoResultadoDTO resultado(@PathVariable Long pautaId) {
        return service.resultado(pautaId);
    }
}
