package com.projeto_web1.jogos_internos.service.atleta.dto;

import lombok.Data;

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

}