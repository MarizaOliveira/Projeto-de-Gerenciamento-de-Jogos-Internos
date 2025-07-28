package com.projeto_web1.jogos_internos.service.evento.impl;

import com.projeto_web1.jogos_internos.model.Evento;
import com.projeto_web1.jogos_internos.repository.EquipeRepository;
import com.projeto_web1.jogos_internos.repository.EventoRepository;
import com.projeto_web1.jogos_internos.service.evento.EventoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventoServiceImpl implements EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private EquipeRepository equipeRepository; // Para a validação de segurança

    // CREATE
    @Override
    @Transactional
    public Evento salvar(Evento evento) {
        return eventoRepository.save(evento);
    }

    // READ (Listar todos)
    @Override
    @Transactional(readOnly = true)
    public List<Evento> listarTodos() {
        return eventoRepository.findAll();
    }

    // READ (Buscar por ID)
    @Override
    @Transactional(readOnly = true)
    public Evento buscarPorId(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado com o ID: " + id));
    }

    // UPDATE
    @Override
    @Transactional
    public Evento atualizar(Long id, Evento evento) {
        Evento eventoExistente = buscarPorId(id);

        // Atualiza os dados
        eventoExistente.setNome(evento.getNome());
        eventoExistente.setNivelCompeticao(evento.getNivelCompeticao());

        return eventoRepository.save(eventoExistente);
    }

    // DELETE
    @Override
    @Transactional
    public void deletar(Long id) {
        // --- VALIDAÇÃO DE SEGURANÇA ---
        // Antes de apagar, verifica se existe alguma equipe associada a este evento
        boolean existeEquipeNoEvento = equipeRepository.existsByEventoIdEvento(id);

        if (existeEquipeNoEvento) {
            throw new IllegalStateException("Não é possível apagar este evento, pois ele já possui equipes associadas.");
        }

        eventoRepository.deleteById(id);
    }
}