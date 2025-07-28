package com.projeto_web1.jogos_internos.service.esporte.dto;

import lombok.Data;

@Data
public class EsporteDTO {
    private Long idEsporte;
    private String nome;
    private Integer minAtletas;
    private Integer maxAtletas;
}