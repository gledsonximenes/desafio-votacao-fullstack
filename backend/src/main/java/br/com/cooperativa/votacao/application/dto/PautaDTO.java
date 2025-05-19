package br.com.cooperativa.votacao.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PautaDTO {
    private Long id;

    @NotBlank(message = "O assunto da pauta é obrigatório")
    private String assunto;
}
