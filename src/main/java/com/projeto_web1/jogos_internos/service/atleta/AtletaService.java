package com.projeto_web1.jogos_internos.service.atleta;

import com.projeto_web1.jogos_internos.model.Atleta;
import java.util.List;

public interface AtletaService {

    // READ (Listar todos)
    List<Atleta> listarTodos();

    // READ (Buscar por ID)
    Atleta buscarPorId(Long id);

    // UPDATE
    Atleta atualizar(Long id, Atleta atleta);

    // DELETE
    void deletar(Long id);

    List<Atleta> listarPorCurso(Long idCurso);



}