package com.projeto_web1.jogos_internos.service.user;

import com.projeto_web1.jogos_internos.service.user.dto.UserDTO;
import com.projeto_web1.jogos_internos.service.user.form.LoginForm;
import com.projeto_web1.jogos_internos.service.user.form.UserForm;

public interface UserService {
    UserDTO criarAtleta(UserForm dados);

    UserDTO login(LoginForm form);
}