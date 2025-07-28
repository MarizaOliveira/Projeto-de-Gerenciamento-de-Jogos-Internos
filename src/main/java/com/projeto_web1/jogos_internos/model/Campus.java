package com.projeto_web1.jogos_internos.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "campus")
public class Campus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCampus;

    @Column(nullable = false, unique = true)
    private String nome;
}