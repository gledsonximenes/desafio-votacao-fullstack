package br.com.cooperativa.votacao.api;

import br.com.cooperativa.votacao.application.dto.PautaDTO;
import br.com.cooperativa.votacao.application.service.PautaService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pautas")
@RequiredArgsConstructor
public class PautaController {
    private final PautaService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cria uma nova pauta")
    public PautaDTO criar(@Validated @RequestBody PautaDTO dto) {
        return service.criar(dto);
    }

    @GetMapping
    @Operation(summary = "Lista todas as pautas")
    public List<PautaDTO> listar() {
        return service.listar();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Exclui uma pauta e todos os dados relacionados")
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }

}