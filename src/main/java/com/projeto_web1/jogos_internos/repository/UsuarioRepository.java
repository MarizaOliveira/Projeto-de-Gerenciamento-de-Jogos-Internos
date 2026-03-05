package com.projeto_web1.jogos_internos.repository;

import com.projeto_web1.jogos_internos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByMatricula(String matricula);


}
