package com.projeto_web1.jogos_internos.service.jogo;

import com.projeto_web1.jogos_internos.service.jogo.dto.ResultadoDTO;
import com.projeto_web1.jogos_internos.service.jogo.form.ResultadoForm;

public interface JogoService {

    ResultadoDTO registrarResultado(Long jogoId, Long idArbitro, ResultadoForm form);

    // Desfazer WO
    void desfazerWo(Long jogoId, Long idArbitro);


}
