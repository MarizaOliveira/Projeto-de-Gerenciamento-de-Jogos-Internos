package com.projeto_web1.jogos_internos.controller;

import com.projeto_web1.jogos_internos.model.Atleta;
import com.projeto_web1.jogos_internos.service.atleta.AtletaService;
import com.projeto_web1.jogos_internos.service.atleta.dto.AtletaDTO;
import com.projeto_web1.jogos_internos.service.atleta.form.AtletaForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/atletas")
public class AtletaController {

    @Autowired
    private AtletaService atletaService;

    // READ (Listar todos)
    @GetMapping
    public ResponseEntity<List<AtletaDTO>> listarAtletas() {
        List<Atleta> atletas = atletaService.listarTodos();
        List<AtletaDTO> dtoList = atletas.stream().map(this::mapToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    // READ (Buscar por ID)
    @GetMapping("/{id}")
    public ResponseEntity<AtletaDTO> buscarAtletaPorId(@PathVariable Long id) {
        Atleta atleta = atletaService.buscarPorId(id);
        return ResponseEntity.ok(mapToDTO(atleta));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<AtletaDTO> atualizarAtleta(@PathVariable Long id, @RequestBody AtletaForm form) {
        // Primeiro, criamos um objeto Atleta temporário com os dados do formulário
        Atleta atletaParaAtualizar = new Atleta();
        atletaParaAtualizar.setNomeCompleto(form.getNomeCompleto());
        atletaParaAtualizar.setApelido(form.getApelido());
        atletaParaAtualizar.setTelefone(form.getTelefone());

        // O serviço cuida da lógica de buscar e atualizar o atleta existente
        Atleta atletaAtualizado = atletaService.atualizar(id, atletaParaAtualizar);

        return ResponseEntity.ok(mapToDTO(atletaAtualizado));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAtleta(@PathVariable Long id) {
        atletaService.deletar(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }

    // Método auxiliar para converter Entidade Atleta em AtletaDTO
    private AtletaDTO mapToDTO(Atleta atleta) {
        AtletaDTO dto = new AtletaDTO();
        dto.setIdAtleta(atleta.getIdAtleta());
        dto.setMatricula(atleta.getUsuario().getMatricula());
        dto.setEmail(atleta.getUsuario().getEmail());
        dto.setNomeCompleto(atleta.getNomeCompleto());
        dto.setApelido(atleta.getApelido());
        dto.setTelefone(atleta.getTelefone());
        if (atleta.getCurso() != null) {
            dto.setNomeCurso(atleta.getCurso().getNome());
        }
        dto.setTecnicoHabilitado(atleta.getTecnicoHabilitado());
        return dto;
    }
}