package com.projeto_web1.jogos_internos.service.campus;

import com.projeto_web1.jogos_internos.model.Campus; // Importe a entidade Campus
import java.util.List; // Importe a classe List

public interface CampusService {

    // CREATE
    Campus salvar(Campus campus);

    // READ (Listar todos)
    List<Campus> listarTodos();

    // READ (Buscar por ID)
    Campus buscarPorId(Long id);

    // UPDATE
    Campus atualizar(Long id, Campus campus);

    // DELETE
    void deletar(Long id);
}