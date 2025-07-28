package com.projeto_web1.jogos_internos.service.user.dto;

import com.projeto_web1.jogos_internos.model.TipoUsuario;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String matricula;
    private String email;
    private TipoUsuario tipoUsuario;
    private String nomeCompleto;
    private String apelido;
}
