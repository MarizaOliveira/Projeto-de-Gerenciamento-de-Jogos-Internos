package com.projeto_web1.jogos_internos.controller;

import com.projeto_web1.jogos_internos.service.coordenador.CoordenadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coordenadores")

public class CoordenadorController {

    @Autowired
    private CoordenadorService coordenadorService;

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
