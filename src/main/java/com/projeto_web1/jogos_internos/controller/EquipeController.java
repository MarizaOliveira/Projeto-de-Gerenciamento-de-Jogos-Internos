package com.projeto_web1.jogos_internos.controller;

import com.projeto_web1.jogos_internos.model.Equipe;
import com.projeto_web1.jogos_internos.service.time.EquipeService;
import com.projeto_web1.jogos_internos.service.time.dto.EquipeDTO;
import com.projeto_web1.jogos_internos.service.time.form.EquipeForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/equipes")
@CrossOrigin(origins = "*")
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
        // A lógica de mapeamento acontece aqui, vamos ajustá-la
        List<EquipeDTO> dtoList = equipes.stream().map(this::mapEquipeToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    //metodo auxiliar
    private EquipeDTO mapEquipeToDTO(Equipe equipe) {
        // Esta lógica de mapeamento já está no EquipeServiceImpl,
        EquipeDTO dto = new EquipeDTO();
        dto.setIdEquipe(equipe.getIdEquipe());
        dto.setNome(equipe.getNome());
        if (equipe.getEsporte() != null) {
            dto.setNomeEsporte(equipe.getEsporte().getNome());
            dto.setIdEsporte(equipe.getEsporte().getIdEsporte());
        }
        if (equipe.getEvento() != null) {
            dto.setIdEvento(equipe.getEvento().getIdEvento()); // ADICIONE ESTA LINHA
        }
        if (equipe.getCurso() != null) {
            dto.setNomeCurso(equipe.getCurso().getNome());
            if (equipe.getCurso().getCampus() != null) {
                dto.setNomeCampus(equipe.getCurso().getCampus().getNome());
            }
        }
        // Mapeamento do técnico e da lista de atletas seria maior, por enquanto deixa sem ele;
        return dto;
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<EquipeDTO> atualizarEquipe(@PathVariable Long id, @RequestBody EquipeForm form) {
        EquipeDTO equipeAtualizada = equipeService.atualizar(id, form); // Crie este método no seu service
        return ResponseEntity.ok(equipeAtualizada);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEquipe(@PathVariable Long id) {
        equipeService.deletar(id); // Crie este método no seu service
        return ResponseEntity.noContent().build();
    }




}