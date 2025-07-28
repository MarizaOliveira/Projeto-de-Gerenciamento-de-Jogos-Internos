package com.projeto_web1.jogos_internos.repository;


import com.projeto_web1.jogos_internos.model.Equipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipeRepository extends JpaRepository<Equipe ,Long> {



    List<Equipe> findAllByEsporte_IdEsporte(Long esporteId);


    // Ele vai gerar um "SELECT ... WHERE evento_id = ? AND esporte_id = ?"
    List<Equipe> findAllByEvento_IdEventoAndEsporte_IdEsporte(Long eventoId, Long esporteId);



    boolean existsByEsporteIdEsporte(Long idEsporte);

    boolean existsByCursoIdCurso(Long idCurso);

    boolean existsByEventoIdEvento(Long idEvento);

    boolean existsByAtletas_IdAtleta(Long idAtleta);
}
