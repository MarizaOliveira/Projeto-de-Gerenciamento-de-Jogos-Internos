package com.projeto_web1.jogos_internos.service.coordenador;

import com.projeto_web1.jogos_internos.model.Coordenador;

import java.util.List;

public interface CoordenadorService {

    void habilitarAtletaComoTecnico(Long idCoordenador, Long idAtleta);

    void desabilitarAtletaComoTecnico(Long idCoordenador, Long idAtleta);

    Coordenador buscarPorId(Long id);

    List<Coordenador> listarTodos();
}