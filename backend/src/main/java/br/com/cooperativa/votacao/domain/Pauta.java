package br.com.cooperativa.votacao.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pauta")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O assunto da pauta é obrigatório")
    @Column(nullable = false)
    private String assunto;

    @OneToOne(mappedBy = "pauta", cascade = CascadeType.ALL, orphanRemoval = true)
    private Sessao sessao;

    @OneToMany(mappedBy = "pauta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Voto> votos = new ArrayList<>();

}
