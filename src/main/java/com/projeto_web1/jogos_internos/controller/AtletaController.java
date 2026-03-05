package com.projeto_web1.jogos_internos.controller;

import com.projeto_web1.jogos_internos.model.Atleta;
import com.projeto_web1.jogos_internos.service.atleta.AtletaService;
import com.projeto_web1.jogos_internos.service.atleta.dto.AtletaDTO;
import com.projeto_web1.jogos_internos.service.atleta.form.AtletaForm;
import com.projeto_web1.jogos_internos.service.time.dto.EquipeDTO;
import com.projeto_web1.jogos_internos.service.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/atletas")
@CrossOrigin(origins = "*")
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

    private Set<EquipeDTO> equipes;

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
            dto.setIdCurso(atleta.getCurso().getIdCurso());
            dto.setNivelCurso(atleta.getCurso().getNivel());
        }
        dto.setTecnicoHabilitado(atleta.getTecnicoHabilitado());


        // LÓGICA ATUALIZADA PARA MAPEAMENTO COMPLETO DAS EQUIPES

        if (atleta.getEquipes() != null) {
            Set<EquipeDTO> equipesDTO = atleta.getEquipes().stream().map(equipe -> {
                EquipeDTO equipeDTO = new EquipeDTO();
                equipeDTO.setIdEquipe(equipe.getIdEquipe());
                equipeDTO.setNome(equipe.getNome());

                if (equipe.getEsporte() != null) {
                    equipeDTO.setNomeEsporte(equipe.getEsporte().getNome());
                }
                if (equipe.getCurso() != null) {
                    equipeDTO.setNomeCurso(equipe.getCurso().getNome());
                    if (equipe.getCurso().getCampus() != null) {
                        equipeDTO.setNomeCampus(equipe.getCurso().getCampus().getNome());
                    }
                }

                // Mapeia o técnico
                if (equipe.getAtletaTecnico() != null) {
                    UserDTO tecnicoDTO = new UserDTO();
                    tecnicoDTO.setId(equipe.getAtletaTecnico().getIdAtleta());
                    tecnicoDTO.setNomeCompleto(equipe.getAtletaTecnico().getNomeCompleto());
                    tecnicoDTO.setApelido(equipe.getAtletaTecnico().getApelido());
                    // Adicione outros campos do UserDTO se necessário
                    equipeDTO.setTecnico(tecnicoDTO);
                }

                // Mapeia a lista de atletas
                if (equipe.getAtletas() != null) {
                    List<UserDTO> atletasDTO = equipe.getAtletas().stream().map(atletaDaEquipe -> {
                        UserDTO atletaDTO = new UserDTO();
                        atletaDTO.setId(atletaDaEquipe.getIdAtleta());
                        atletaDTO.setNomeCompleto(atletaDaEquipe.getNomeCompleto());
                        atletaDTO.setApelido(atletaDaEquipe.getApelido());
                        return atletaDTO;
                    }).collect(Collectors.toList());
                    equipeDTO.setAtletas(atletasDTO);
                }

                return equipeDTO;
            }).collect(Collectors.toSet());
            dto.setEquipes(equipesDTO);
        }

        return dto;
    }
}