package com.projeto_web1.jogos_internos.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "coordenadores")
public class Coordenador {

    @Id
    private Long idCoordenador;

    @Column(name = "nome_completo", nullable = false)
    private String nomeCompleto;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id_coordenador")
    private Usuario usuario;
}