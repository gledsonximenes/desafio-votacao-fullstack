package br.com.cooperativa.votacao.application.mapper;

import br.com.cooperativa.votacao.application.dto.PautaDTO;
import br.com.cooperativa.votacao.application.dto.SessaoDTO;
import br.com.cooperativa.votacao.application.dto.SessaoResultadoDTO;
import br.com.cooperativa.votacao.domain.ResultadoSessao;
import br.com.cooperativa.votacao.domain.Sessao;

import java.time.Duration;
import java.time.Instant;

public class SessaoMapper {

    public static Sessao toEntity(Long pautaId, Duration duracao) {
        Sessao sessao = new Sessao();
        sessao.setPauta(new PautaMapper().toEntity(new PautaDTO(pautaId, null)));
        sessao.setAbertura(Instant.now());
        sessao.setEncerramento(sessao.getAbertura().plus(duracao == null ? Duration.ofMinutes(1) : duracao));
        return sessao;
    }

    public static SessaoResultadoDTO toResultadoDto(Sessao sessao, long votosSim, long votosNao) {
        ResultadoSessao resultado;
        if (votosSim > votosNao) resultado = ResultadoSessao.APROVADA;
        else if (votosNao > votosSim) resultado = ResultadoSessao.REJEITADA;
        else resultado = ResultadoSessao.EMPATE;

        return SessaoResultadoDTO.builder()
                .pautaId(sessao.getPauta().getId())
                .assunto(sessao.getPauta().getAssunto())
                .votosSim(votosSim)
                .votosNao(votosNao)
                .resultado(resultado)
                .build();
    }
}
