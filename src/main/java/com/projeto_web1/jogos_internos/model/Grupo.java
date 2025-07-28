package com.projeto_web1.jogos_internos.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "grupos")
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGrupo;

    @Column(nullable = false, length = 100)
    private String nome;

    @ManyToMany
    @JoinTable(
            name = "grupo_equipes",
            joinColumns = @JoinColumn(name = "id_grupo"),
            inverseJoinColumns = @JoinColumn(name = "id_equipe")
    )
    private Set<Equipe> equipes = new HashSet<>();
}