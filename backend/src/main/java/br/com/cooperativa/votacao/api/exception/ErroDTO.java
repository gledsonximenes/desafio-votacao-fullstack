package br.com.cooperativa.votacao.api.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class ErroDTO {
    private LocalDateTime timestamp;
    private int status;
    private String erro;
    private String mensagem;
    private Map<String, String> detalhes;
}
