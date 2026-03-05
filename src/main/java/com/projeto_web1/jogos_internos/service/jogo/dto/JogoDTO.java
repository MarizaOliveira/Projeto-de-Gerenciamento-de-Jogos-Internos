package com.projeto_web1.jogos_internos.service.jogo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class JogoDTO {
    private Long idJogo;
    private LocalDateTime dataHora;
    private String status;
    private String fase;
    private String nomeEquipeA;
    private String nomeEquipeB;
    private Long idEquipeA; // <-- ADICIONE ESTA LINHA
    private Long idEquipeB; // <-- ADICIONE ESTA LINHA
    private Integer placarEquipeA;      // <-- ADICIONE ESTA LINHA
    private Integer placarEquipeB;      // <-- ADICIONE ESTA LINHA
    private Integer penaltiPlacarA;   // <-- ADICIONE ESTA LINHA
    private Integer penaltiPlacarB;   // <-- ADICIONE ESTA LINHA
    private Long idEvento;
    private String nomeEvento;
    private Long idEsporte;
    private String nomeEsporte;
    private Long idGrupo;
    private String nomeGrupo;
    private String nomeArbitro;
}