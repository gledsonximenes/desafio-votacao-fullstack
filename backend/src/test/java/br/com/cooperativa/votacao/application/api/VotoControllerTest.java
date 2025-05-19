package br.com.cooperativa.votacao.application.api;

import br.com.cooperativa.votacao.api.VotoController;
import br.com.cooperativa.votacao.application.dto.VotoDTO;
import br.com.cooperativa.votacao.application.dto.SessaoResultadoDTO;
import br.com.cooperativa.votacao.application.service.VotoService;
import br.com.cooperativa.votacao.domain.ResultadoSessao;
import br.com.cooperativa.votacao.domain.ResultadoVoto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VotoController.class)
class VotoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private VotoService service;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void deveVotarEndpoint() throws Exception {
        VotoDTO dto = new VotoDTO(1L, "12345678901", ResultadoVoto.SIM);
        mvc.perform(post("/api/v1/votos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
        then(service).should().votar(any());
    }

    @Test
    void deveObterResultadoVotoEndpoint() throws Exception {
        SessaoResultadoDTO result = new SessaoResultadoDTO(1L, "Assunto", 5, 3, ResultadoSessao.APROVADA);
        given(service.resultado(1L)).willReturn(result);

        mvc.perform(get("/api/v1/votos/1/resultado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.votosNao").value(3));
    }
}