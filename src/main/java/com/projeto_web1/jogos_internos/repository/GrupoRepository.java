package com.projeto_web1.jogos_internos.repository;

import com.projeto_web1.jogos_internos.model.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    @Query("SELECT DISTINCT g FROM Grupo g JOIN g.equipes e WHERE e.evento.idEvento = :eventoId AND e.esporte.idEsporte = :esporteId")
    List<Grupo> findGruposByEventoAndEsporte(@Param("eventoId") Long eventoId, @Param("esporteId") Long esporteId);


    void deleteAllByIdGrupoIn(List<Long> ids);



}
