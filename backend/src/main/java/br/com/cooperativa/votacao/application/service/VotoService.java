package br.com.cooperativa.votacao.application.service;

import br.com.cooperativa.votacao.application.dto.SessaoResultadoDTO;
import br.com.cooperativa.votacao.application.dto.VotoDTO;
import br.com.cooperativa.votacao.application.mapper.SessaoMapper;
import br.com.cooperativa.votacao.application.mapper.VotoMapper;
import br.com.cooperativa.votacao.domain.Sessao;
import br.com.cooperativa.votacao.domain.Voto;
import br.com.cooperativa.votacao.infrastructure.repository.SessaoRepository;
import br.com.cooperativa.votacao.infrastructure.repository.VotoRepository;
import br.com.cooperativa.votacao.infrastructure.client.CPFClientFake;
import br.com.cooperativa.votacao.domain.ResultadoVoto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class VotoService {
    private final VotoRepository votoRepo;
    private final SessaoRepository sessaoRepo;
    private final CPFClientFake cpfClient;

    @Transactional
    public void votar(VotoDTO dto) {
        Sessao sessao = sessaoRepo.findByPautaId(dto.getPautaId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sessão de votação não encontrada para a pauta " + dto.getPautaId()));

        if (sessao.getEncerramento().isBefore(Instant.now())) {
            throw new IllegalStateException("Sessão de votação já foi encerrada.");
        }

        if (votoRepo.existsByPautaIdAndAssociadoId(dto.getPautaId(), dto.getAssociadoId())) {
            throw new IllegalStateException("Associado já votou nesta pauta.");
        }

        try {
            cpfClient.validarCPF(dto.getAssociadoId());
        } catch (CPFClientFake.CPFNotFoundException | CPFClientFake.CPFUnableToVoteException e) {
            throw new EntityNotFoundException("CPF não encontrado no serviço externo.");
        }

        Voto voto = VotoMapper.toEntity(dto);
        votoRepo.save(voto);
    }

    @Transactional(readOnly = true)
    public SessaoResultadoDTO resultado(Long pautaId) {
        long sim = votoRepo.countByPautaIdAndVoto(pautaId, ResultadoVoto.SIM);
        long nao = votoRepo.countByPautaIdAndVoto(pautaId, ResultadoVoto.NAO);
        var sessao = sessaoRepo.findById(pautaId)
                .orElseThrow(() -> new IllegalArgumentException("Sessão não encontrada"));
        return SessaoMapper.toResultadoDto(sessao, sim, nao);
    }
}
