package com.projeto_web1.jogos_internos.repository;

import com.projeto_web1.jogos_internos.model.Campus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CampusRepository extends JpaRepository<Campus, Long> {


    boolean existsByNome(String nome);




}
