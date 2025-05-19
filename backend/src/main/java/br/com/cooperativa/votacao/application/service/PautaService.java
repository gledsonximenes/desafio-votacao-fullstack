package br.com.cooperativa.votacao.application.service;

import br.com.cooperativa.votacao.application.dto.PautaDTO;
import br.com.cooperativa.votacao.application.mapper.PautaMapper;
import br.com.cooperativa.votacao.domain.Pauta;
import br.com.cooperativa.votacao.infrastructure.repository.PautaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PautaService {
    private final PautaRepository repository;

    @Transactional
    public PautaDTO criar(PautaDTO dto) {
        var entidade = PautaMapper.toEntity(dto);
        var salva = repository.save(entidade);
        return PautaMapper.toDto(salva);
    }

    @Transactional(readOnly = true)
    public List<PautaDTO> listar() {
        return repository.findAll().stream()
                .map(PautaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void excluir(Long id) {
        Pauta pauta = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pauta não encontrada"));

        repository.delete(pauta);
    }

}
