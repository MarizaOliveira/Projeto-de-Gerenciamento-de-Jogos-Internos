package com.projeto_web1.jogos_internos.service.time.form;

import lombok.Data;
import java.util.List;

@Data
public class EquipeForm {

    private String nome;
    private Long idEsporte;
    private Long idCurso;
    private Long idEvento;
    private Long idAtletaTecnico;

    private List<Long> idAtletas; // Lista com os IDs de todos os atletas da equipe (incluindo o técnico)
}