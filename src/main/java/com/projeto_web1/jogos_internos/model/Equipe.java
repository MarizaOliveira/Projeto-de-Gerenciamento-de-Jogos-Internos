package com.projeto_web1.jogos_internos.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"atletas", "grupos"}) // Exclui as coleções para evitar o loop

@Entity
@Table(name = "equipes", uniqueConstraints = {
        // A restrição UNIQUE inclui o evento!
        @UniqueConstraint(columnNames = {"id_esporte", "id_curso", "id_evento"})
})
public class Equipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEquipe;

    @Column(nullable = false)
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_esporte", nullable = false)
    private Esporte esporte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_curso", nullable = false)
    private Curso curso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evento", nullable = false)
    private Evento evento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_atleta_tecnico", nullable = false)
    private Atleta atletaTecnico;

    @ManyToMany
    @JoinTable(
            name = "equipe_atletas",
            joinColumns = @JoinColumn(name = "id_equipe"),
            inverseJoinColumns = @JoinColumn(name = "id_atleta")
    )
    private Set<Atleta> atletas = new HashSet<>();

    @ManyToMany(mappedBy = "equipes")
    private Set<Grupo> grupos = new HashSet<>();
}