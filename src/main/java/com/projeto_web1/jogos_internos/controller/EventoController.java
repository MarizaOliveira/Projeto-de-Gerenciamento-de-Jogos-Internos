package com.projeto_web1.jogos_internos.controller;

import com.projeto_web1.jogos_internos.model.Evento;
import com.projeto_web1.jogos_internos.service.evento.EventoService;
import com.projeto_web1.jogos_internos.service.evento.dto.EventoDTO;
import com.projeto_web1.jogos_internos.service.evento.form.EventoForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/eventos")
@CrossOrigin(origins = "*")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    // CREATE
    @PostMapping
    public ResponseEntity<EventoDTO> criarEvento(@RequestBody EventoForm form, UriComponentsBuilder uriBuilder) {
        Evento evento = new Evento();
        evento.setNome(form.getNome());
        evento.setNivelCompeticao(form.getNivelCompeticao());
        Evento eventoSalvo = eventoService.salvar(evento);

        EventoDTO dto = mapToDTO(eventoSalvo);

        URI uri = uriBuilder.path("/eventos/{id}").buildAndExpand(dto.getIdEvento()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    // READ (Listar todos)
    @GetMapping
    public ResponseEntity<List<EventoDTO>> listarEventos() {
        List<Evento> eventos = eventoService.listarTodos();
        List<EventoDTO> dtoList = eventos.stream().map(this::mapToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    // READ (Buscar por ID)
    @GetMapping("/{id}")
    public ResponseEntity<EventoDTO> buscarEventoPorId(@PathVariable Long id) {
        Evento evento = eventoService.buscarPorId(id);
        return ResponseEntity.ok(mapToDTO(evento));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<EventoDTO> atualizarEvento(@PathVariable Long id, @RequestBody EventoForm form) {
        Evento evento = new Evento();
        evento.setNome(form.getNome());
        evento.setNivelCompeticao(form.getNivelCompeticao());
        Evento eventoAtualizado = eventoService.atualizar(id, evento);
        return ResponseEntity.ok(mapToDTO(eventoAtualizado));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEvento(@PathVariable Long id) {
        eventoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // Método auxiliar para converter Entidade em DTO
    private EventoDTO mapToDTO(Evento evento) {
        EventoDTO dto = new EventoDTO();
        dto.setIdEvento(evento.getIdEvento());
        dto.setNome(evento.getNome());
        dto.setNivelCompeticao(evento.getNivelCompeticao());
        return dto;
    }
}