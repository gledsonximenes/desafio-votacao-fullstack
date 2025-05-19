package br.com.cooperativa.votacao.infrastructure.repository;

import br.com.cooperativa.votacao.domain.ResultadoVoto;
import br.com.cooperativa.votacao.domain.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {
    boolean existsByPautaIdAndAssociadoId(Long pautaId, String associadoId);
    long countByPautaIdAndVoto(Long pautaId, ResultadoVoto voto);
}
