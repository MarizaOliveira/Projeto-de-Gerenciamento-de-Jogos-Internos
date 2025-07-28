package com.projeto_web1.jogos_internos.controller;

import com.projeto_web1.jogos_internos.service.torneio.TorneioService;
import com.projeto_web1.jogos_internos.service.torneio.dto.TimeClassificacaoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/torneios")
public class TorneioController {

    @Autowired
    private TorneioService tournamentService;
// Gerar grupos automatico.
    @PostMapping("/{eventoId}/esportes/{esporteId}/gerar-grupos")
    public ResponseEntity<Void> gerarGruposEJogos(
            @PathVariable Long eventoId,
            @PathVariable Long esporteId) {

        tournamentService.gerarGruposEJogos(eventoId, esporteId);

        return ResponseEntity.ok().build();// Retorna 200 OK com corpo vazio


    }

    // Endpoint para ver a tabela de classificação de um grupo
    @GetMapping("/grupos/{grupoId}/classificacao")
    public ResponseEntity<List<TimeClassificacaoDTO>> getTabelaClassificacao(@PathVariable Long grupoId) {
        List<TimeClassificacaoDTO> tabela = tournamentService.getTabelaDeClassificacao(grupoId);
        return ResponseEntity.ok(tabela);
    }

    // Endpoint para gerar a primeira fase do mata-mata (quartas)
    @PostMapping("/{eventoId}/esportes/{esporteId}/gerar-quartas")
    public ResponseEntity<Void> gerarQuartas(
            @PathVariable Long eventoId,
            @PathVariable Long esporteId) {
        tournamentService.gerarQuartasDeFinal(eventoId, esporteId);
        return ResponseEntity.ok().build();
    }

    //Semifinal
    @PostMapping("/{eventoId}/esportes/{esporteId}/gerar-semifinal")
    public ResponseEntity<Void> gerarSemifinal(
            @PathVariable Long eventoId,
            @PathVariable Long esporteId) {
        tournamentService.gerarSemifinal(eventoId, esporteId);
        return ResponseEntity.ok().build();
    }

    // Final
    @PostMapping("/{eventoId}/esportes/{esporteId}/gerar-final")
    public ResponseEntity<Void> gerarFinal(
            @PathVariable Long eventoId,
            @PathVariable Long esporteId) {
        tournamentService.gerarFinal(eventoId, esporteId);
        return ResponseEntity.ok().build();
    }





}

