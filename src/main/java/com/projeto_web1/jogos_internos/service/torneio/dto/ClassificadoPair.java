package com.projeto_web1.jogos_internos.service.torneio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class ClassificadoPair {
    private TimeClassificacaoDTO primeiroColocado;
    private TimeClassificacaoDTO segundoColocado;
}