package br.com.cooperativa.votacao.application.dto;

import br.com.cooperativa.votacao.domain.ResultadoSessao;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessaoResultadoDTO {
    private Long pautaId;
    private String assunto;
    private long votosSim;
    private long votosNao;
    private ResultadoSessao resultado; // Enum: APROVADA, REJEITADA, EMPATE
}
