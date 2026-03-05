package com.projeto_web1.jogos_internos.service.time;

import com.projeto_web1.jogos_internos.model.Equipe;
import com.projeto_web1.jogos_internos.service.time.dto.EquipeDTO;
import com.projeto_web1.jogos_internos.service.time.form.EquipeForm;

import java.util.List;

public interface EquipeService {

    EquipeDTO criarEquipe(EquipeForm form);

    List<Equipe> listarTodas();

    EquipeDTO atualizar(Long id, EquipeForm form);

    void deletar(Long id);
}
