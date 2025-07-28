package com.projeto_web1.jogos_internos.service.esporte;

import com.projeto_web1.jogos_internos.model.Esporte;
import java.util.List;

public interface EsporteService {

    // CREATE
    Esporte salvar(Esporte esporte);

    // READ (Listar todos)
    List<Esporte> listarTodos();

    // READ (Buscar por ID)
    Esporte buscarPorId(Long id);

    // UPDATE
    Esporte atualizar(Long id, Esporte esporte);

    // DELETE
    void deletar(Long id);
}