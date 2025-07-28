package com.projeto_web1.jogos_internos.service.user.form;

import lombok.Data;

@Data
public class UserForm {

    private String matricula;
    private String senha;
    private String email;
    private String nomeCompleto;
    private String apelido;
    private String telefone;
    private Long idCurso;

}