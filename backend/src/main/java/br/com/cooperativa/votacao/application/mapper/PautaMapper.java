package br.com.cooperativa.votacao.application.mapper;

import br.com.cooperativa.votacao.application.dto.PautaDTO;
import br.com.cooperativa.votacao.domain.Pauta;

public class PautaMapper {
    public static PautaDTO toDto(Pauta pauta) {
        return PautaDTO.builder()
                .id(pauta.getId())
                .assunto(pauta.getAssunto())
                .build();
    }

    public static Pauta toEntity(PautaDTO dto) {
        return Pauta.builder()
                .id(dto.getId())
                .assunto(dto.getAssunto())
                .build();
    }
}
