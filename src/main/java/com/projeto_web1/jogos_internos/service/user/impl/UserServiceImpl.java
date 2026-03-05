package com.projeto_web1.jogos_internos.service.user.impl;

import com.projeto_web1.jogos_internos.model.Atleta;
import com.projeto_web1.jogos_internos.model.Coordenador;
import com.projeto_web1.jogos_internos.model.Curso;
import com.projeto_web1.jogos_internos.model.TipoUsuario;
import com.projeto_web1.jogos_internos.model.Usuario;
import com.projeto_web1.jogos_internos.repository.AtletaRepository;
import com.projeto_web1.jogos_internos.repository.CoordenadorRepository;
import com.projeto_web1.jogos_internos.repository.CursoRepository;
import com.projeto_web1.jogos_internos.repository.UsuarioRepository;
import com.projeto_web1.jogos_internos.service.user.UserService;
import com.projeto_web1.jogos_internos.service.user.dto.UserDTO;
import com.projeto_web1.jogos_internos.service.user.form.LoginForm;
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

    @Autowired
    private CoordenadorRepository coordenadorRepository;


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

    // Adicione este novo método na sua classe UserServiceImpl



        @Override
        @Transactional(readOnly = true)
        public UserDTO login(LoginForm form) {
            // 1. Busca o usuário no banco de dados pela matrícula
            Usuario usuario = usuarioRepository.findByMatricula(form.getMatricula())
                    .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado ou matrícula inválida."));

            // 2. Verifica se a senha fornecida é igual à senha guardada no banco
            if (!form.getSenha().equals(usuario.getSenha())) {
                throw new IllegalArgumentException("Senha inválida.");
            }

            // 3. Monta e devolve os dados do usuário com base no tipo
            UserDTO userDTO = new UserDTO();
            userDTO.setId(usuario.getIdUsuario());
            userDTO.setMatricula(usuario.getMatricula());
            userDTO.setEmail(usuario.getEmail());
            userDTO.setTipoUsuario(usuario.getTipoUsuario());

            switch (usuario.getTipoUsuario()) {
                case ATLETA:
                    Atleta atleta = atletaRepository.findById(usuario.getIdUsuario())
                            .orElseThrow(() -> new EntityNotFoundException("Dados de atleta não encontrados."));
                    userDTO.setNomeCompleto(atleta.getNomeCompleto());
                    userDTO.setApelido(atleta.getApelido());
                    // Adicione o idCurso para o atleta, se necessário
                    if (atleta.getCurso() != null) {
                        userDTO.setIdCurso(atleta.getCurso().getIdCurso());
                    }
                    break;
                case COORDENADOR:
                    // Primeiro, buscamos o objeto Coordenador para ter acesso ao nome.
                    Coordenador coordenador = coordenadorRepository.findById(usuario.getIdUsuario())
                            .orElseThrow(() -> new EntityNotFoundException("Dados de coordenador não encontrados."));
                    userDTO.setNomeCompleto(coordenador.getNomeCompleto());

                    // Em seguida, fazemos uma nova busca para encontrar o Curso do Coordenador.
                    // Esta é a forma correta de acessar a informação sem ter a coluna no Coordenador.
                    Curso cursoDoCoordenador = cursoRepository.findByCoordenadorIdCoordenador(coordenador.getIdCoordenador())
                            .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado para este coordenador."));
                    userDTO.setIdCurso(cursoDoCoordenador.getIdCurso());
                    break;
                // Adicione outros casos (ARBITRO, por exemplo) se eles também tiverem um nome
                case ADMINISTRADOR:
                    userDTO.setNomeCompleto("Administrador do Sistema"); // Ou use usuario.getEmail()
                    break;

                default:
                    // Para outros tipos de usuário, o nome pode ser o mesmo do email ou outro campo
                    // Por enquanto, deixamos em branco.
                    break;
            }

        return userDTO;
    }


}
