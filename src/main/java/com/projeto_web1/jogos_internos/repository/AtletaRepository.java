package com.projeto_web1.jogos_internos.repository;

import com.projeto_web1.jogos_internos.model.Atleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtletaRepository extends JpaRepository<Atleta, Long> {

}
