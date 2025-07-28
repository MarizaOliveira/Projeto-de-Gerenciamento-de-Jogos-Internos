package com.projeto_web1.jogos_internos.controller;

import com.projeto_web1.jogos_internos.service.user.UserService;
import com.projeto_web1.jogos_internos.service.user.dto.UserDTO;
import com.projeto_web1.jogos_internos.service.user.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
        import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController // Avisa ao Spring que esta classe é um Controller REST.
@RequestMapping("/usuarios") // Define que todos os endpoints nesta classe começarão com /usuarios.
public class UsuarioController {

    @Autowired
    private UserService userService;

    @PostMapping // Mapeia este método para requisições HTTP POST para /usuarios.
    public ResponseEntity<UserDTO> cadastrarAtleta(@RequestBody UserForm form, UriComponentsBuilder uriBuilder) {
        // Chama o serviço para criar o atleta
        UserDTO userDTO = userService.criarAtleta(form);

        // Constrói a URI de resposta
        URI uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(userDTO.getId()).toUri();

        // Retorna o status 201 Created com a URI e o corpo da resposta
        return ResponseEntity.created(uri).body(userDTO);
    }



}