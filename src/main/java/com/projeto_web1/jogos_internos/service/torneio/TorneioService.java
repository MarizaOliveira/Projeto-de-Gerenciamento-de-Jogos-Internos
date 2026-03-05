package com.projeto_web1.jogos_internos.service.torneio;


import com.projeto_web1.jogos_internos.service.torneio.dto.GrupoDTO;
import com.projeto_web1.jogos_internos.service.torneio.dto.TimeClassificacaoDTO;
import java.util.List;

public interface TorneioService {

    void gerarGruposEJogos(Long eventoId, Long esporteId);

    List<TimeClassificacaoDTO> getTabelaDeClassificacao(Long grupoId);

    List<GrupoDTO> listarGrupos(Long eventoId, Long esporteId);

    // Métodos para o mata - mata
    void gerarQuartasDeFinal(Long eventoId, Long esporteId);
    void gerarSemifinal(Long eventoId, Long esporteId);
    void gerarFinal(Long eventoId, Long esporteId);



}