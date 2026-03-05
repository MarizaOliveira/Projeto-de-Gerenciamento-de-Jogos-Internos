package com.projeto_web1.jogos_internos.controller;

import com.projeto_web1.jogos_internos.model.Equipe;
import com.projeto_web1.jogos_internos.model.Jogo;
import com.projeto_web1.jogos_internos.service.jogo.JogoService;
import com.projeto_web1.jogos_internos.service.jogo.dto.JogoDTO;
import com.projeto_web1.jogos_internos.service.jogo.dto.ResultadoDTO;
import com.projeto_web1.jogos_internos.service.jogo.form.ResultadoForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/jogos")
@CrossOrigin(origins = "*")
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

    @GetMapping
    public ResponseEntity<List<JogoDTO>> listarJogos() {
        List<Jogo> jogos = jogoService.listarTodos(); // Você precisará de adicionar este método ao seu service
        List<JogoDTO> dtoList = jogos.stream().map(this::mapToJogoDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    // Dentro do método mapToJogoDTO em JogoController.java

    // Dentro da classe JogoController.java

    private JogoDTO mapToJogoDTO(Jogo jogo) {
        JogoDTO dto = new JogoDTO();
        dto.setIdJogo(jogo.getIdJogo());
        dto.setDataHora(jogo.getDataHora());
        dto.setStatus(jogo.getStatus());
        dto.setFase(jogo.getFase());
        if (jogo.getEquipeA() != null) {
            dto.setNomeEquipeA(jogo.getEquipeA().getNome());
            dto.setIdEquipeA(jogo.getEquipeA().getIdEquipe());
        }
        if (jogo.getEquipeB() != null) {
            dto.setNomeEquipeB(jogo.getEquipeB().getNome());
            dto.setIdEquipeB(jogo.getEquipeB().getIdEquipe());
        }

        dto.setPlacarEquipeA(jogo.getPlacarEquipeA());
        dto.setPlacarEquipeB(jogo.getPlacarEquipeB());
        dto.setPenaltiPlacarA(jogo.getPenaltiPlacarA());
        dto.setPenaltiPlacarB(jogo.getPenaltiPlacarB());

        if (jogo.getEquipeA() != null) {
            Equipe equipeReferencia = jogo.getEquipeA();
            if (equipeReferencia.getEvento() != null) {
                dto.setIdEvento(equipeReferencia.getEvento().getIdEvento());
                dto.setNomeEvento(equipeReferencia.getEvento().getNome());
            }
            if (equipeReferencia.getEsporte() != null) {
                dto.setIdEsporte(equipeReferencia.getEsporte().getIdEsporte());
                dto.setNomeEsporte(equipeReferencia.getEsporte().getNome());
            }
        }

        if (jogo.getGrupo() != null) {
            dto.setIdGrupo(jogo.getGrupo().getIdGrupo());
            dto.setNomeGrupo(jogo.getGrupo().getNome());
        }

        if (jogo.getArbitro() != null) {
            dto.setNomeArbitro(jogo.getArbitro().getEmail());
        }

        return dto;
    }
}