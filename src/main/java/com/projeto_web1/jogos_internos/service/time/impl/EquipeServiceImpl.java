package com.projeto_web1.jogos_internos.service.time.impl;

import com.projeto_web1.jogos_internos.model.*;
import com.projeto_web1.jogos_internos.repository.*;
import com.projeto_web1.jogos_internos.service.time.EquipeService;
import com.projeto_web1.jogos_internos.service.time.dto.EquipeDTO;
import com.projeto_web1.jogos_internos.service.time.form.EquipeForm;
import com.projeto_web1.jogos_internos.service.user.dto.UserDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipeServiceImpl implements EquipeService {

    @Autowired
    private EquipeRepository equipeRepository;
    @Autowired
    private EsporteRepository esporteRepository;
    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private AtletaRepository atletaRepository;

    @Autowired
    private EventoRepository eventoRepository;


    @Override
    @Transactional
    public EquipeDTO criarEquipe(EquipeForm form) {
        // Busca as entidades relacionadas pelo ID
        Esporte esporte = esporteRepository.findById(form.getIdEsporte())
                .orElseThrow(() -> new EntityNotFoundException("Esporte não encontrado."));
        Curso cursoDoTecnico = cursoRepository.findById(form.getIdCurso())
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado."));
        Atleta tecnico = atletaRepository.findById(form.getIdAtletaTecnico())
                .orElseThrow(() -> new EntityNotFoundException("Atleta técnico não encontrado."));
        Evento evento = eventoRepository.findById(form.getIdEvento())
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado."));

        // Validações
        if (!tecnico.getTecnicoHabilitado()) {
            throw new IllegalArgumentException("Este atleta não está habilitado para ser técnico.");
        }
        if (form.getIdAtletas().size() < esporte.getMinAtletas() || form.getIdAtletas().size() > esporte.getMaxAtletas()) {
            throw new IllegalArgumentException("Número de atletas inválido para o esporte " + esporte.getNome());
        }
        if (!form.getIdAtletas().contains(tecnico.getIdAtleta())) {
            throw new IllegalArgumentException("O técnico deve estar incluído na lista de atletas da equipe.");
        }

        //LÓGICA DE VALIDAÇÃO DE CURSO
        List<Atleta> atletasDaEquipe = atletaRepository.findAllById(form.getIdAtletas());
        for (Atleta atleta : atletasDaEquipe) {
            // Verifica se o curso do atleta é o mesmo curso da equipe que está sendo criada
            if (atleta.getCurso() == null || !atleta.getCurso().getIdCurso().equals(cursoDoTecnico.getIdCurso())) {
                throw new IllegalStateException("O atleta " + atleta.getNomeCompleto() + " não pertence ao curso " + cursoDoTecnico.getNome() + " e não pode ser adicionado a esta equipe.");
            }
        }


        // CRIAÇÃO
        Equipe novaEquipe = new Equipe();
        novaEquipe.setNome(form.getNome());
        novaEquipe.setEsporte(esporte);
        novaEquipe.setCurso(cursoDoTecnico);
        novaEquipe.setEvento(evento);
        novaEquipe.setAtletaTecnico(tecnico);
        novaEquipe.getAtletas().addAll(atletasDaEquipe);

        try {
            Equipe equipeSalva = equipeRepository.save(novaEquipe);
            return mapToEquipeDTO(equipeSalva, atletasDaEquipe);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Já existe uma equipe para este curso e esporte neste evento.");
        }
    }
    // Método auxiliar para converter a Entidade em DTO

    private EquipeDTO mapToEquipeDTO(Equipe equipe, List<Atleta> atletas) {
        EquipeDTO dto = new EquipeDTO();
        dto.setIdEquipe(equipe.getIdEquipe());
        dto.setNome(equipe.getNome());
        dto.setNomeEsporte(equipe.getEsporte().getNome());
        dto.setNomeCurso(equipe.getCurso().getNome());
        dto.setNomeCampus(equipe.getCurso().getCampus().getNome());

        // Mapeia o técnico, agora incluindo os dados do Usuario
        UserDTO tecnicoDto = new UserDTO();
        Usuario usuarioTecnico = equipe.getAtletaTecnico().getUsuario(); // Pega o Usuario associado
        tecnicoDto.setId(equipe.getAtletaTecnico().getIdAtleta());
        tecnicoDto.setMatricula(usuarioTecnico.getMatricula());
        tecnicoDto.setEmail(usuarioTecnico.getEmail());
        tecnicoDto.setTipoUsuario(usuarioTecnico.getTipoUsuario());
        tecnicoDto.setNomeCompleto(equipe.getAtletaTecnico().getNomeCompleto());
        tecnicoDto.setApelido(equipe.getAtletaTecnico().getApelido());
        dto.setTecnico(tecnicoDto);

        // Mapeia a lista de atletas, agora incluindo os dados do Usuario de cada um
        List<UserDTO> atletaDtos = atletas.stream().map(atleta -> {
            UserDTO atletaDto = new UserDTO();
            Usuario usuarioAtleta = atleta.getUsuario(); // Pega o Usuario associado
            atletaDto.setId(atleta.getIdAtleta());
            atletaDto.setMatricula(usuarioAtleta.getMatricula());
            atletaDto.setEmail(usuarioAtleta.getEmail());
            atletaDto.setTipoUsuario(usuarioAtleta.getTipoUsuario());
            atletaDto.setNomeCompleto(atleta.getNomeCompleto());
            atletaDto.setApelido(atleta.getApelido());
            return atletaDto;
        }).collect(Collectors.toList());
        dto.setAtletas(atletaDtos);

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Equipe> listarTodas() {
        return equipeRepository.findAll();
    }

    // Quadro de código para adicionar ao EquipeServiceImpl.java


    // Cole este código no lugar do seu método atualizar() existente

    @Override
    @Transactional
    public EquipeDTO atualizar(Long id, EquipeForm form) {
        // 1. Encontra a equipe existente no banco de dados
        Equipe equipeExistente = equipeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipe não encontrada com o ID: " + id));

        // 2. Busca as novas entidades relacionadas (exatamente como no método de criar)
        Esporte esporte = esporteRepository.findById(form.getIdEsporte())
                .orElseThrow(() -> new EntityNotFoundException("Esporte não encontrado."));
        Curso cursoDoTecnico = cursoRepository.findById(form.getIdCurso())
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado."));
        Atleta tecnico = atletaRepository.findById(form.getIdAtletaTecnico())
                .orElseThrow(() -> new EntityNotFoundException("Atleta técnico não encontrado."));
        Evento evento = eventoRepository.findById(form.getIdEvento())
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado."));

        // 3. Re-aplica as mesmas validações do método de criar
        if (!tecnico.getTecnicoHabilitado()) {
            throw new IllegalArgumentException("Este atleta não está habilitado para ser técnico.");
        }
        if (form.getIdAtletas().size() < esporte.getMinAtletas() || form.getIdAtletas().size() > esporte.getMaxAtletas()) {
            throw new IllegalArgumentException("Número de atletas inválido para o esporte " + esporte.getNome());
        }
        if (!form.getIdAtletas().contains(tecnico.getIdAtleta())) {
            throw new IllegalArgumentException("O técnico deve estar incluído na lista de atletas da equipe.");
        }

        List<Atleta> novosAtletasDaEquipe = atletaRepository.findAllById(form.getIdAtletas());
        for (Atleta atleta : novosAtletasDaEquipe) {
            if (atleta.getCurso() == null || !atleta.getCurso().getIdCurso().equals(cursoDoTecnico.getIdCurso())) {
                throw new IllegalStateException("O atleta " + atleta.getNomeCompleto() + " não pertence ao curso " + cursoDoTecnico.getNome() + " e não pode ser adicionado a esta equipe.");
            }
        }

        // 4. Atualiza os campos da equipe existente
        equipeExistente.setNome(form.getNome());
        equipeExistente.setEsporte(esporte);
        equipeExistente.setCurso(cursoDoTecnico);
        equipeExistente.setEvento(evento);
        equipeExistente.setAtletaTecnico(tecnico);

        // Limpa a lista de atletas antiga e adiciona a nova
        equipeExistente.getAtletas().clear();
        equipeExistente.getAtletas().addAll(novosAtletasDaEquipe);

        try {
            Equipe equipeSalva = equipeRepository.save(equipeExistente);
            // Reutiliza o DTO mapper para retornar a resposta
            return mapToEquipeDTO(equipeSalva, novosAtletasDaEquipe);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Já existe uma equipe para este curso e esporte neste evento.");
        }
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!equipeRepository.existsById(id)) {
            throw new EntityNotFoundException("Equipa não encontrada com o ID: " + id);
        }
        // O Spring Data JPA lida com a remoção das associações na tabela 'equipe_atletas'
        equipeRepository.deleteById(id);
    }
}