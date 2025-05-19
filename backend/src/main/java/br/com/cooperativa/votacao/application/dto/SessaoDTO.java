package br.com.cooperativa.votacao.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Duration;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessaoDTO {
    @NotNull(message = "A pautaId é obrigatória")
    private Long pautaId;

    /**
     * Duração em minutos. Se não informado, assume 1 minuto.
     */
    private Duration duracao;
}