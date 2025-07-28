package com.projeto_web1.jogos_internos.service.torneio.dto;

import com.projeto_web1.jogos_internos.model.Equipe;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TimeClassificacaoDTO {

    private Long equipeId;
    private String equipeNome;


    private int pontos = 0;
    private int jogosJogados = 0;
    private int vitorias = 0;
    private int empates = 0;
    private int derrotas = 0;
    private int golsPro = 0;
    private int golsContra = 0;
    private int saldoDeGols = 0;
    private int posicaoNoGrupo;

    // Construtor para facilitar a criação
    public TimeClassificacaoDTO(Equipe equipe) {
        this.equipeId = equipe.getIdEquipe(); // Pega o ID da equipe
        this.equipeNome = equipe.getNome();   // Pega o Nome da equipe
    }


    // Método auxiliar para calcular o saldo de gols no final
    public void calcularSaldoDeGols() {
        this.saldoDeGols = this.golsPro - this.golsContra;
    }



}
