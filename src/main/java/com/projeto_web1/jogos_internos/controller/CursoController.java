package com.projeto_web1.jogos_internos.controller;

import com.projeto_web1.jogos_internos.model.Atleta;
import com.projeto_web1.jogos_internos.model.Campus;
import com.projeto_web1.jogos_internos.model.Coordenador;
import com.projeto_web1.jogos_internos.model.Curso;
import com.projeto_web1.jogos_internos.service.atleta.AtletaService;
import com.projeto_web1.jogos_internos.service.atleta.dto.AtletaDTO;
import com.projeto_web1.jogos_internos.service.campus.CampusService;
import com.projeto_web1.jogos_internos.service.coordenador.CoordenadorService;
import com.projeto_web1.jogos_internos.service.curso.CursoService;
import com.projeto_web1.jogos_internos.service.curso.dto.CursoDTO;
import com.projeto_web1.jogos_internos.service.curso.form.CursoForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @Autowired
    private CampusService campusService; // Necessário para buscar o Campus

    @Autowired
    private CoordenadorService coordenadorService; // Necessário para buscar o Coordenador

    @Autowired
    private AtletaService atletaService;

    // CREATE
    @PostMapping
    public ResponseEntity<CursoDTO> criarCurso(@RequestBody CursoForm form, UriComponentsBuilder uriBuilder) {
        // Busca as entidades relacionadas para associar ao novo curso
        Campus campus = campusService.buscarPorId(form.getIdCampus());
        Coordenador coordenador = coordenadorService.buscarPorId(form.getIdCoordenador());

        Curso curso = new Curso();
        curso.setNome(form.getNome());
        curso.setNivel(form.getNivel());
        curso.setCampus(campus);
        curso.setCoordenador(coordenador);

        Curso cursoSalvo = cursoService.salvar(curso);

        CursoDTO dto = mapToDTO(cursoSalvo);

        URI uri = uriBuilder.path("/cursos/{id}").buildAndExpand(dto.getIdCurso()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    // READ (Listar todos)
    @GetMapping
    public ResponseEntity<List<CursoDTO>> listarCursos() {
        List<Curso> cursos = cursoService.listarTodos();
        List<CursoDTO> dtoList = cursos.stream().map(this::mapToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}/atletas")
    public ResponseEntity<List<AtletaDTO>> listarAtletasPorCurso(@PathVariable Long id) {
        List<Atleta> atletas = atletaService.listarPorCurso(id);
        List<AtletaDTO> dtoList = atletas.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    private AtletaDTO mapToDTO(Atleta atleta) {
        AtletaDTO dto = new AtletaDTO();
        dto.setIdAtleta(atleta.getIdAtleta());
        dto.setMatricula(atleta.getUsuario().getMatricula());
        dto.setEmail(atleta.getUsuario().getEmail());
        dto.setNomeCompleto(atleta.getNomeCompleto());
        dto.setApelido(atleta.getApelido());
        dto.setTelefone(atleta.getTelefone());
        dto.setNomeCurso(atleta.getCurso().getNome());
        dto.setTecnicoHabilitado(atleta.getTecnicoHabilitado());
        return dto;
    }


    // READ (Buscar por ID)
    @GetMapping("/{id}")
    public ResponseEntity<CursoDTO> buscarCursoPorId(@PathVariable Long id) {
        Curso curso = cursoService.buscarPorId(id);
        return ResponseEntity.ok(mapToDTO(curso));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<CursoDTO> atualizarCurso(@PathVariable Long id, @RequestBody CursoForm form) {
        Campus campus = campusService.buscarPorId(form.getIdCampus());
        Coordenador coordenador = coordenadorService.buscarPorId(form.getIdCoordenador());

        Curso curso = new Curso();
        curso.setNome(form.getNome());
        curso.setNivel(form.getNivel());
        curso.setCampus(campus);
        curso.setCoordenador(coordenador);

        Curso cursoAtualizado = cursoService.atualizar(id, curso);
        return ResponseEntity.ok(mapToDTO(cursoAtualizado));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCurso(@PathVariable Long id) {
        cursoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // Método auxiliar para converter Entidade em DTO
    private CursoDTO mapToDTO(Curso curso) {
        CursoDTO dto = new CursoDTO();
        dto.setIdCurso(curso.getIdCurso());
        dto.setNome(curso.getNome());
        dto.setNivel(curso.getNivel());
        dto.setNomeCampus(curso.getCampus().getNome());
        dto.setNomeCoordenador(curso.getCoordenador().getNomeCompleto());
        return dto;
    }

    // Dentro da classe CursoController.java

    @GetMapping("/campus/{idCampus}")
    public ResponseEntity<List<CursoDTO>> listarCursosPorCampus(@PathVariable Long idCampus) {
        List<Curso> cursos = cursoService.listarPorCampus(idCampus);
        List<CursoDTO> dtoList = cursos.stream().map(this::mapToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }
}