package com.projeto_web1.jogos_internos.service.user.impl;

import com.projeto_web1.jogos_internos.model.Atleta;
import com.projeto_web1.jogos_internos.model.Curso;
import com.projeto_web1.jogos_internos.model.TipoUsuario;
import com.projeto_web1.jogos_internos.model.Usuario;
import com.projeto_web1.jogos_internos.repository.AtletaRepository;
import com.projeto_web1.jogos_internos.repository.CursoRepository;
import com.projeto_web1.jogos_internos.repository.UsuarioRepository;
import com.projeto_web1.jogos_internos.service.user.UserService;
import com.projeto_web1.jogos_internos.service.user.dto.UserDTO;
import com.projeto_web1.jogos_internos.service.user.form.UserForm;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired // O Spring vai injetar uma instância do UsuarioRepository aqui.
    private UsuarioRepository usuarioRepository;

    @Autowired // O Spring vai injetar uma instância do AtletaRepository aqui.
    private AtletaRepository atletaRepository;

    @Autowired
    private CursoRepository cursoRepository;


    @Override
    @Transactional
    public UserDTO criarAtleta(UserForm dados) {
        // Buscar a entidade Curso ---
        Curso cursoDoAtleta = cursoRepository.findById(dados.getIdCurso())
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com o ID: " + dados.getIdCurso()));


        // Criar e salvar a entidade Usuario (lógica existente)
        Usuario novoUsuario = new Usuario();
        novoUsuario.setMatricula(dados.getMatricula());
        novoUsuario.setEmail(dados.getEmail());
        novoUsuario.setSenha(dados.getSenha());
        novoUsuario.setTipoUsuario(TipoUsuario.ATLETA);
        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);

        // Criar e salvar a entidade Atleta, agora ligada ao Usuario E ao Curso
        Atleta novoAtleta = new Atleta();
        novoAtleta.setUsuario(usuarioSalvo);
        novoAtleta.setNomeCompleto(dados.getNomeCompleto());
        novoAtleta.setApelido(dados.getApelido());
        novoAtleta.setTelefone(dados.getTelefone());
        novoAtleta.setTecnicoHabilitado(false);
        novoAtleta.setCurso(cursoDoAtleta);
        Atleta atletaSalvo = atletaRepository.save(novoAtleta);

        // Criar e retornar o DTO de resposta (lógica existente)
        UserDTO resposta = new UserDTO();
        resposta.setId(atletaSalvo.getIdAtleta());
        resposta.setMatricula(usuarioSalvo.getMatricula());
        resposta.setEmail(usuarioSalvo.getEmail());
        resposta.setTipoUsuario(usuarioSalvo.getTipoUsuario());
        resposta.setNomeCompleto(atletaSalvo.getNomeCompleto());
        resposta.setApelido(atletaSalvo.getApelido());

        return resposta;


    }
}
