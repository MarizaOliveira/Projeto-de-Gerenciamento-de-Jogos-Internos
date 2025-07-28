package com.projeto_web1.jogos_internos.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "esportes")
public class Esporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEsporte;

    @Column(nullable = false, unique = true, length = 100)
    private String nome;

    @Column(name = "min_atletas", nullable = false)
    private Integer minAtletas;

    @Column(name = "max_atletas", nullable = false)
    private Integer maxAtletas;
}