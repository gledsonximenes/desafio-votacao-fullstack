package br.com.cooperativa.votacao.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="voto",
        uniqueConstraints=@UniqueConstraint(columnNames={"pauta_id","associado_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Voto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Pauta pauta;

    @Column(name="associado_id", nullable=false)
    private String associadoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private ResultadoVoto voto;
}