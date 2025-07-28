package com.projeto_web1.jogos_internos.controller;

import com.projeto_web1.jogos_internos.model.Campus;
import com.projeto_web1.jogos_internos.service.campus.CampusService;
import com.projeto_web1.jogos_internos.service.campus.dto.CampusDTO;
import com.projeto_web1.jogos_internos.service.campus.form.CampusForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/campus")
public class CampusController {

    @Autowired
    private CampusService campusService;

    // CREATE
    @PostMapping
    public ResponseEntity<CampusDTO> criarCampus(@RequestBody CampusForm form, UriComponentsBuilder uriBuilder) {
        Campus campus = new Campus();
        campus.setNome(form.getNome());
        Campus campusSalvo = campusService.salvar(campus);

        CampusDTO dto = new CampusDTO();
        dto.setIdCampus(campusSalvo.getIdCampus());
        dto.setNome(campusSalvo.getNome());

        URI uri = uriBuilder.path("/campus/{id}").buildAndExpand(dto.getIdCampus()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    // READ (Listar todos)
    @GetMapping
    public ResponseEntity<List<CampusDTO>> listarCampi() {
        List<Campus> campi = campusService.listarTodos();
        List<CampusDTO> dtoList = campi.stream().map(campus -> {
            CampusDTO dto = new CampusDTO();
            dto.setIdCampus(campus.getIdCampus());
            dto.setNome(campus.getNome());
            return dto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    // READ (Buscar por ID)
    @GetMapping("/{id}")
    public ResponseEntity<CampusDTO> buscarCampusPorId(@PathVariable Long id) {
        Campus campus = campusService.buscarPorId(id);
        CampusDTO dto = new CampusDTO();
        dto.setIdCampus(campus.getIdCampus());
        dto.setNome(campus.getNome());
        return ResponseEntity.ok(dto);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<CampusDTO> atualizarCampus(@PathVariable Long id, @RequestBody CampusForm form) {
        Campus campus = new Campus();
        campus.setNome(form.getNome());
        Campus campusAtualizado = campusService.atualizar(id, campus);

        CampusDTO dto = new CampusDTO();
        dto.setIdCampus(campusAtualizado.getIdCampus());
        dto.setNome(campusAtualizado.getNome());
        return ResponseEntity.ok(dto);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCampus(@PathVariable Long id) {
        campusService.deletar(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }

}

