package com.projeto_web1.jogos_internos.service.curso.form;

import lombok.Data;

@Data
public class CursoForm {
    private String nome;
    private String nivel;
    private Long idCampus;
    private Long idCoordenador;
}