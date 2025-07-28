package com.projeto_web1.jogos_internos.service.jogo.form;

import lombok.Data;

@Data
public class ResultadoForm {

    // Para um jogo normal
    private Integer placarEquipeA;
    private Integer placarEquipeB;

    // Para um jogo normal
    private Boolean wo;
    private Long idEquipeVencedoraWo;// id da equipe que venceu por wo;



    // Para Mata mata - caso dê empate.
    //Resultado de penalti não pode ser igual, obrigatoriamente deve ser diferente.
    private Integer penaltiPlacarA;
    private Integer penaltiPlacarB;


}
