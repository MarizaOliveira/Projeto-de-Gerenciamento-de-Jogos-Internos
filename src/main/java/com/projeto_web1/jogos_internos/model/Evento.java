package com.projeto_web1.jogos_internos.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEvento;

    @Column(nullable = false)
    private String nome;

    @Column(name = "nivel_competicao", nullable = false, length = 50)
    private String nivelCompeticao;

    @ManyToMany
    @JoinTable(
            name = "evento_esportes",
            joinColumns = @JoinColumn(name = "id_evento"),
            inverseJoinColumns = @JoinColumn(name = "id_esporte")
    )
    private Set<Esporte> esportes = new HashSet<>();
}