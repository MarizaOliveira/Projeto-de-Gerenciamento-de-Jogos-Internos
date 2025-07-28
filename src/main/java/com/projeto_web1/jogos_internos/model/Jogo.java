package com.projeto_web1.jogos_internos.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "jogos")
public class Jogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idJogo;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(nullable = false, length = 50)
    private String fase;

    @Column(name = "placar_equipe_a")
    private Integer placarEquipeA;

    @Column(name = "placar_equipe_b")
    private Integer placarEquipeB;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_equipe_a", nullable = false)
    private Equipe equipeA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_equipe_b", nullable = false)
    private Equipe equipeB;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_arbitro")
    private Usuario arbitro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_grupo")
    private Grupo grupo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipe_vencedora_wo")
    private Equipe equipeVencedoraWo;

    @Column(name = "penalti_placar_a")
    private Integer penaltiPlacarA;

    @Column(name = "penalti_placar_b")
    private Integer penaltiPlacarB;
}