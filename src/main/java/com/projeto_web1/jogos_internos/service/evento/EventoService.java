package com.projeto_web1.jogos_internos.service.evento;

import com.projeto_web1.jogos_internos.model.Evento;
import java.util.List;

public interface EventoService {

    // CREATE
    Evento salvar(Evento evento);

    // READ (Listar todos)
    List<Evento> listarTodos();

    // READ (Buscar por ID)
    Evento buscarPorId(Long id);

    // UPDATE
    Evento atualizar(Long id, Evento evento);

    // DELETE
    void deletar(Long id);
}