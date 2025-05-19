package br.com.cooperativa.votacao.application.service;

import br.com.cooperativa.votacao.application.dto.PautaDTO;
import br.com.cooperativa.votacao.domain.Pauta;
import br.com.cooperativa.votacao.infrastructure.repository.PautaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class PautaServiceTest {

    @Mock
    private PautaRepository repository;

    @InjectMocks
    private PautaService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarPauta() {
        PautaDTO dto = new PautaDTO(null, "Assunto Teste");

        Pauta saved = Pauta.builder()
                .id(1L)
                .assunto("Assunto Teste")
                .build();

        given(repository.save(any())).willReturn(saved);

        PautaDTO result = service.criar(dto);

        assertEquals(1L, result.getId());
        assertEquals("Assunto Teste", result.getAssunto());
        then(repository).should().save(any(Pauta.class));
    }

    @Test
    void deveListarPautas() {
        Pauta p1 = Pauta.builder().id(1L).assunto("A").build();
        Pauta p2 = Pauta.builder().id(2L).assunto("B").build();

        given(repository.findAll()).willReturn(List.of(p1, p2));

        List<PautaDTO> list = service.listar();

        assertEquals(2, list.size());
        assertTrue(list.stream().anyMatch(p -> p.getAssunto().equals("A")));
        assertTrue(list.stream().anyMatch(p -> p.getAssunto().equals("B")));
    }
}
