package com.projeto_web1.jogos_internos.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCurso;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, length = 50)
    private String nivel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_campus", nullable = false)
    private Campus campus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_coordenador", nullable = false, unique = true)
    private Coordenador coordenador;



}