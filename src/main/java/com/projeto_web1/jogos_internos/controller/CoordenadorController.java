package com.projeto_web1.jogos_internos.controller;

import com.projeto_web1.jogos_internos.model.Coordenador;
import com.projeto_web1.jogos_internos.service.coordenador.CoordenadorDTO;
import com.projeto_web1.jogos_internos.service.coordenador.CoordenadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/coordenadores")

public class CoordenadorController {

    @Autowired
    private CoordenadorService coordenadorService;

    @GetMapping
    public ResponseEntity<List<CoordenadorDTO>> listarCoordenadores() {
        List<Coordenador> coordenadores = coordenadorService.listarTodos();
        List<CoordenadorDTO> dtoList = coordenadores.stream().map(coord -> {
            CoordenadorDTO dto = new CoordenadorDTO();
            dto.setIdCoordenador(coord.getIdCoordenador());
            dto.setNomeCompleto(coord.getNomeCompleto());
            return dto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @PostMapping("/{idCoordenador}/habilitar-tecnico/{idAtleta}")
    public ResponseEntity<Void> habilitarTecnico(
            @PathVariable Long idCoordenador,
            @PathVariable Long idAtleta) {

        coordenadorService.habilitarAtletaComoTecnico(idCoordenador, idAtleta);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{idCoordenador}/desabilitar-tecnico/{idAtleta}")
    public ResponseEntity<Void> desabilitarTecnico(
            @PathVariable Long idCoordenador,
            @PathVariable Long idAtleta) {

        coordenadorService.desabilitarAtletaComoTecnico(idCoordenador, idAtleta);
        return ResponseEntity.ok().build();
    }
}
