package com.projeto_web1.jogos_internos.controller;

import com.projeto_web1.jogos_internos.service.jogo.JogoService;
import com.projeto_web1.jogos_internos.service.jogo.dto.ResultadoDTO;
import com.projeto_web1.jogos_internos.service.jogo.form.ResultadoForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/jogos")
public class JogoController {

    @Autowired
    private JogoService jogoService;

    @PutMapping("/{jogoId}/resultado/arbitro/{idArbitro}")
    public ResponseEntity<ResultadoDTO> registrarResultado(
            @PathVariable Long jogoId,
            @PathVariable Long idArbitro, // <-- Novo parâmetro
            @RequestBody ResultadoForm form) {

        // Agora passamos o idArbitro também para o serviço
        ResultadoDTO resultadoDTO = jogoService.registrarResultado(jogoId, idArbitro, form);

        return ResponseEntity.ok(resultadoDTO);
    }

    // No seu ficheiro JogoController.java, substitua o endpoint

    @PutMapping("/{jogoId}/desfazer-wo/arbitro/{idArbitro}")
    public ResponseEntity<Void> desfazerWo(
            @PathVariable Long jogoId,
            @PathVariable Long idArbitro) {
        jogoService.desfazerWo(jogoId, idArbitro);
        return ResponseEntity.ok().build();
    }

}