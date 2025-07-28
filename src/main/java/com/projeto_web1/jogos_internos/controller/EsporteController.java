package com.projeto_web1.jogos_internos.controller;

import com.projeto_web1.jogos_internos.model.Esporte;
import com.projeto_web1.jogos_internos.service.esporte.EsporteService;
import com.projeto_web1.jogos_internos.service.esporte.dto.EsporteDTO;
import com.projeto_web1.jogos_internos.service.esporte.form.EsporteForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/esportes")
public class EsporteController {

    @Autowired
    private EsporteService esporteService;

    // CREATE
    @PostMapping
    public ResponseEntity<EsporteDTO> criarEsporte(@RequestBody EsporteForm form, UriComponentsBuilder uriBuilder) {
        Esporte esporte = new Esporte();
        esporte.setNome(form.getNome());
        esporte.setMinAtletas(form.getMinAtletas());
        esporte.setMaxAtletas(form.getMaxAtletas());
        Esporte esporteSalvo = esporteService.salvar(esporte);

        EsporteDTO dto = mapToDTO(esporteSalvo);

        URI uri = uriBuilder.path("/esportes/{id}").buildAndExpand(dto.getIdEsporte()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    // READ (Listar todos)
    @GetMapping
    public ResponseEntity<List<EsporteDTO>> listarEsportes() {
        List<Esporte> esportes = esporteService.listarTodos();
        List<EsporteDTO> dtoList = esportes.stream().map(this::mapToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    // READ (Buscar por ID)
    @GetMapping("/{id}")
    public ResponseEntity<EsporteDTO> buscarEsportePorId(@PathVariable Long id) {
        Esporte esporte = esporteService.buscarPorId(id);
        return ResponseEntity.ok(mapToDTO(esporte));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<EsporteDTO> atualizarEsporte(@PathVariable Long id, @RequestBody EsporteForm form) {
        Esporte esporte = new Esporte();
        esporte.setNome(form.getNome());
        esporte.setMinAtletas(form.getMinAtletas());
        esporte.setMaxAtletas(form.getMaxAtletas());
        Esporte esporteAtualizado = esporteService.atualizar(id, esporte);
        return ResponseEntity.ok(mapToDTO(esporteAtualizado));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEsporte(@PathVariable Long id) {
        esporteService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // Método auxiliar para converter Entidade em DTO
    private EsporteDTO mapToDTO(Esporte esporte) {
        EsporteDTO dto = new EsporteDTO();
        dto.setIdEsporte(esporte.getIdEsporte());
        dto.setNome(esporte.getNome());
        dto.setMinAtletas(esporte.getMinAtletas());
        dto.setMaxAtletas(esporte.getMaxAtletas());
        return dto;
    }
}