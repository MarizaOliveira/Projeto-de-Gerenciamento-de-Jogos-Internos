package com.projeto_web1.jogos_internos.repository;


import com.projeto_web1.jogos_internos.model.Grupo;
import com.projeto_web1.jogos_internos.model.Jogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface JogoRepository extends JpaRepository<Jogo, Long> {

    //Busca uma lista de jogos que pertencem a um Grupo específico
    List<Jogo> findAllByGrupoAndStatusIn(Grupo grupo, List<String> status);


    boolean existsByFase(String fase);


    // Busca uma lista de jogos de uma fase específica, com status contidos em uma lista, e ordena os resultados.
    List<Jogo> findAllByFaseAndStatusInOrderByIdJogoAsc(String fase, List<String> status);


    void deleteAllByGrupo_IdGrupoIn(List<Long> idsDosGrupos);

    // NOVO MÉTODO: Verifica se existe algum jogo "AGENDADO" para um determinado
    // evento e esporte na fase de grupos.
    @Query("SELECT CASE WHEN COUNT(j) > 0 THEN TRUE ELSE FALSE END FROM Jogo j " +
            "WHERE j.fase = 'FASE_DE_GRUPOS' AND j.status = 'AGENDADO' " +
            "AND j.equipeA.evento.idEvento = :eventoId AND j.equipeA.esporte.idEsporte = :esporteId")
    boolean existsAgendadoInFaseDeGrupos(@Param("eventoId") Long eventoId, @Param("esporteId") Long esporteId);


}
