package br.com.cooperativa.votacao.application.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CPFStatusDTO {
    private String status; // ABLE_TO_VOTE ou UNABLE_TO_VOTE
}