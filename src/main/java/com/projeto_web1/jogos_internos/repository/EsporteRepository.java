package com.projeto_web1.jogos_internos.repository;

import com.projeto_web1.jogos_internos.model.Esporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EsporteRepository extends JpaRepository<Esporte, Long> {


    boolean existsByNome(String nome);

}
