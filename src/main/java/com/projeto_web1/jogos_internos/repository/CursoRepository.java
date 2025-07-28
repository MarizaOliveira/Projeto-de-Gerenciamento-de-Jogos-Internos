package com.projeto_web1.jogos_internos.repository;

import com.projeto_web1.jogos_internos.model.Campus;
import com.projeto_web1.jogos_internos.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    // Verifica se existe cursos associados a este campus
    boolean existsByCampusIdCampus(Long idCampus);
   // Verifica se não tem mais criado
    boolean existsByNomeAndCampus(String nome, Campus campus);

    Optional<Curso> findByCoordenadorIdCoordenador(Long idCoordenador);
}