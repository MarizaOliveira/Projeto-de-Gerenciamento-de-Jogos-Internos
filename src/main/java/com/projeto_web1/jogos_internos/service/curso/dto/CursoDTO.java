package com.projeto_web1.jogos_internos.service.curso.dto;

import lombok.Data;

@Data
public class CursoDTO {
    private Long idCurso;
    private String nome;
    private String nivel;
    private String nomeCampus;
    private String nomeCoordenador;

}