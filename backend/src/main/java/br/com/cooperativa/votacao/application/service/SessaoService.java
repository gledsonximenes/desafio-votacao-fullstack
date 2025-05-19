package br.com.cooperativa.votacao.application.service;

import br.com.cooperativa.votacao.application.dto.SessaoDTO;
import br.com.cooperativa.votacao.application.dto.SessaoResultadoDTO;
import br.com.cooperativa.votacao.application.mapper.SessaoMapper;
import br.com.cooperativa.votacao.domain.Pauta;
import br.com.cooperativa.votacao.domain.ResultadoVoto;
import br.com.cooperativa.votacao.domain.Sessao;
import br.com.cooperativa.votacao.infrastructure.repository.PautaRepository;
import br.com.cooperativa.votacao.infrastructure.repository.SessaoRepository;
import br.com.cooperativa.votacao.infrastructure.repository.VotoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessaoService {
    private final SessaoRepository sessaoRepo;
    private final VotoRepository votoRepo;
    private final PautaRepository pautaRepo;

    @Transactional
    public SessaoResultadoDTO abrirSessao(SessaoDTO dto) {
        Duration duracao = dto.getDuracao() == null
                ? Duration.ofMinutes(1)
                : dto.getDuracao();

        Pauta pauta = pautaRepo.findById(dto.getPautaId())
                .orElseThrow(() -> new EntityNotFoundException("Pauta não encontrada"));

        Sessao sessao = Sessao.builder()
                .pauta(pauta)
                .abertura(Instant.now())
                .encerramento(Instant.now().plus(duracao))
                .build();

        sessao = sessaoRepo.save(sessao);
        return SessaoMapper.toResultadoDto(sessao, 0, 0);
    }

    @Transactional(readOnly = true)
    public SessaoResultadoDTO resultado(Long pautaId) {
        Sessao sessao = sessaoRepo.findByPautaId(pautaId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sessão de votação não encontrada para a pauta " + pautaId));

        long votosSim = votoRepo.countByPautaIdAndVoto(pautaId, ResultadoVoto.SIM);
        long votosNao = votoRepo.countByPautaIdAndVoto(pautaId, ResultadoVoto.NAO);

        return SessaoMapper.toResultadoDto(sessao, votosSim, votosNao);
    }

    @Transactional(readOnly = true)
    public List<SessaoDTO> listar() {
        return sessaoRepo.findAll().stream()
                .map(sessao -> SessaoDTO.builder()
                        .pautaId(sessao.getPauta().getId())
                        .duracao(Duration.between(sessao.getAbertura(), sessao.getEncerramento()))
                        .build())
                .collect(Collectors.toList());
    }
}
