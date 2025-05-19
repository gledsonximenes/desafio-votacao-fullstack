package br.com.cooperativa.votacao.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "sessao")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Sessao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "pauta_id", nullable = false)
    private Pauta pauta;

    @Column(nullable = false)
    private Instant abertura;

    @Column(nullable = false)
    private Instant encerramento;
}
