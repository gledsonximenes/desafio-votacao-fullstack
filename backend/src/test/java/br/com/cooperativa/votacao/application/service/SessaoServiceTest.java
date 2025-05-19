package br.com.cooperativa.votacao.application.service;

import br.com.cooperativa.votacao.application.dto.SessaoDTO;
import br.com.cooperativa.votacao.application.dto.SessaoResultadoDTO;
import br.com.cooperativa.votacao.domain.Sessao;
import br.com.cooperativa.votacao.domain.Pauta;
import br.com.cooperativa.votacao.domain.ResultadoVoto;
import br.com.cooperativa.votacao.infrastructure.repository.PautaRepository;
import br.com.cooperativa.votacao.infrastructure.repository.SessaoRepository;
import br.com.cooperativa.votacao.infrastructure.repository.VotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class SessaoServiceTest {

    @Mock
    private SessaoRepository sessaoRepo;

    @Mock
    private VotoRepository votoRepo;

    @Mock
    private PautaRepository pautaRepo;

    @InjectMocks
    private SessaoService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveAbrirSessaoComDuracaoPadrao() {
        SessaoDTO dto = new SessaoDTO(1L, null);
        given(pautaRepo.findById(1L)).willReturn(
                Optional.of(Pauta.builder().id(1L).assunto("Teste").build())
        );
        given(sessaoRepo.save(any(Sessao.class))).willAnswer(invocation -> {
            Sessao sessao = invocation.getArgument(0);
            sessao.setId(1L);
            return sessao;
        });

        SessaoResultadoDTO resultado = service.abrirSessao(dto);

        assertEquals(1L, resultado.getPautaId());
        assertEquals(0, resultado.getVotosSim());
        assertEquals(0, resultado.getVotosNao());
        assertNotNull(resultado.getResultado());
        then(pautaRepo).should().findById(1L);
        then(sessaoRepo).should().save(any(Sessao.class));
    }

    @Test
    void deveObterResultadoCorreto() {
        Instant now = Instant.now();
        Sessao sessao = Sessao.builder()
                .id(1L)
                .pauta(Pauta.builder().id(1L).assunto("Teste").build())
                .abertura(now.minusSeconds(60))
                .encerramento(now.plusSeconds(60))
                .build();
        given(sessaoRepo.findByPautaId(1L)).willReturn(Optional.of(sessao));
        given(votoRepo.countByPautaIdAndVoto(1L, ResultadoVoto.SIM)).willReturn(2L);
        given(votoRepo.countByPautaIdAndVoto(1L, ResultadoVoto.NAO)).willReturn(1L);

        SessaoResultadoDTO dto = service.resultado(1L);

        assertEquals(2, dto.getVotosSim());
        assertEquals(1, dto.getVotosNao());
        assertEquals(br.com.cooperativa.votacao.domain.ResultadoSessao.APROVADA, dto.getResultado());
        then(sessaoRepo).should().findByPautaId(1L);
    }
}