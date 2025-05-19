package br.com.cooperativa.votacao.application.mapper;

import br.com.cooperativa.votacao.application.dto.VotoDTO;
import br.com.cooperativa.votacao.domain.Pauta;
import br.com.cooperativa.votacao.domain.Voto;

public class VotoMapper {

    public static Voto toEntity(VotoDTO dto) {
        return Voto.builder()
                .pauta(Pauta.builder().id(dto.getPautaId()).build())
                .associadoId(dto.getAssociadoId())
                .voto(dto.getVoto())
                .build();
    }

    public static VotoDTO toDto(Voto voto) {
        return VotoDTO.builder()
                .pautaId(voto.getPauta().getId())
                .associadoId(voto.getAssociadoId())
                .voto(voto.getVoto())
                .build();
    }
}
