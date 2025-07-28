package com.projeto_web1.jogos_internos.service.time.dto;

import com.projeto_web1.jogos_internos.service.user.dto.UserDTO;
import lombok.Data;
import java.util.List;

@Data
public class EquipeDTO {

    private Long idEquipe;
    private String nome;
    private String nomeEsporte;
    private String nomeCurso;
    private String nomeCampus;
    private UserDTO tecnico; // dados do técnico


    // Lista de objetos com os dados de todos os atletas
    private List<UserDTO> atletas;
}