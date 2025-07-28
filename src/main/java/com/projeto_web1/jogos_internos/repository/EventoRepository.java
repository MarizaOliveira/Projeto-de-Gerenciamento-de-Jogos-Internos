package com.projeto_web1.jogos_internos.repository;


import com.projeto_web1.jogos_internos.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

}
