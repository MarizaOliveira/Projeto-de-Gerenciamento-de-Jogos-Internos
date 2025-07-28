package com.projeto_web1.jogos_internos.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

// Substituímos @Data por anotações mais específicas
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "equipes") // Exclui a coleção para evitar o loop

@Entity
@Table(name = "atletas")
public class Atleta {

    @Id
    private Long idAtleta;

    @Column(name = "nome_completo", nullable = false)
    private String nomeCompleto;

    private String apelido;

    private String telefone;

    @Column(name = "tecnico_habilitado", nullable = false) // para o coordenador definir
    private Boolean tecnicoHabilitado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_curso")
    private Curso curso;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id_atleta")
    private Usuario usuario;

    @ManyToMany(mappedBy = "atletas")
    private Set<Equipe> equipes = new HashSet<>();


}