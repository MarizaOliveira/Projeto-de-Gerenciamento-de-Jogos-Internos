package com.projeto_web1.jogos_internos.service.curso;

import com.projeto_web1.jogos_internos.model.Curso;
import java.util.List;

public interface CursoService {

    // CREATE
    Curso salvar(Curso curso);

    // READ (Listar todos)
    List<Curso> listarTodos();

    // READ (Buscar por ID)
    Curso buscarPorId(Long id);

    // UPDATE
    Curso atualizar(Long id, Curso curso);

    // DELETE
    void deletar(Long id);
}