package com.projeto_web1.jogos_internos.controller;

import com.projeto_web1.jogos_internos.model.Equipe;
import com.projeto_web1.jogos_internos.service.time.EquipeService;
import com.projeto_web1.jogos_internos.service.time.dto.EquipeDTO;
import com.projeto_web1.jogos_internos.service.time.form.EquipeForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/equipes")
public class EquipeController {

    @Autowired
    private EquipeService equipeService;

    @PostMapping
    public ResponseEntity<EquipeDTO> cadastrarEquipe(@RequestBody EquipeForm form, UriComponentsBuilder uriBuilder) {
        // Delega a criação para o nosso serviço
        EquipeDTO equipeDTO = equipeService.criarEquipe(form);
        // Constrói a URI para o novo recurso criado
        URI uri = uriBuilder.path("/equipes/{id}").buildAndExpand(equipeDTO.getIdEquipe()).toUri();
        // Retorna a resposta 201 Created
        return ResponseEntity.created(uri).body(equipeDTO);
    }

    @GetMapping
    public ResponseEntity<List<EquipeDTO>> listarEquipes() {
        List<Equipe> equipes = equipeService.listarTodas();
        // Precisamos de um método para converter a lista de Entidades para uma lista de DTOs
        // Vamos criar um método auxiliar para isso
        List<EquipeDTO> dtoList = equipes.stream().map(this::mapEquipeToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    // --- NOVO MÉTODO AUXILIAR ABAIXO ---
    private EquipeDTO mapEquipeToDTO(Equipe equipe) {
        // Esta lógica de mapeamento já está no seu EquipeServiceImpl,
        // o ideal seria movê-la para um "Mapper" dedicado, mas por simplicidade, podemos replicá-la aqui.
        EquipeDTO dto = new EquipeDTO();
        dto.setIdEquipe(equipe.getIdEquipe());
        dto.setNome(equipe.getNome());
        if (equipe.getEsporte() != null) {
            dto.setNomeEsporte(equipe.getEsporte().getNome());
        }
        if (equipe.getCurso() != null) {
            dto.setNomeCurso(equipe.getCurso().getNome());
            if (equipe.getCurso().getCampus() != null) {
                dto.setNomeCampus(equipe.getCurso().getCampus().getNome());
            }
        }
        // Mapeamento do técnico e da lista de atletas seria mais complexo e pode ser adicionado depois
        return dto;
    }




}