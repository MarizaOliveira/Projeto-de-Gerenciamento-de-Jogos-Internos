package com.projeto_web1.jogos_internos.service.atleta.dto;

import com.projeto_web1.jogos_internos.service.time.dto.EquipeDTO;
import lombok.Data;

import java.util.Set;

@Data
public class AtletaDTO {
    private Long idAtleta;
    private String matricula;
    private String email;
    private String nomeCompleto;
    private String apelido;
    private String telefone;
    private String nomeCurso;
    private Boolean tecnicoHabilitado;

    private Long idCurso;
    private String nivelCurso;
    private Set<EquipeDTO> equipes;

}