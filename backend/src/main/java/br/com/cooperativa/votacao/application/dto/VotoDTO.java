package br.com.cooperativa.votacao.application.dto;

import br.com.cooperativa.votacao.domain.ResultadoVoto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VotoDTO {
    @NotNull(message = "A pautaId é obrigatória")
    private Long pautaId;

    @NotBlank(message = "O CPF do associado é obrigatório")
    private String associadoId;

    @NotNull(message = "O voto é obrigatório")
    private ResultadoVoto voto;  // SIM ou NAO
}
