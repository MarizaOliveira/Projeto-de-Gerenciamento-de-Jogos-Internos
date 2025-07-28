package com.projeto_web1.jogos_internos.repository;


import com.projeto_web1.jogos_internos.model.Grupo;
import com.projeto_web1.jogos_internos.model.Jogo;
import org.springframework.data.jpa.repository.JpaRepository;
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


}
