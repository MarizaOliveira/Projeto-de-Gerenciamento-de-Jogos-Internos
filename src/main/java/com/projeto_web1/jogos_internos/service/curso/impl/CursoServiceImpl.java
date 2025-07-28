package com.projeto_web1.jogos_internos.service.curso.impl;

import com.projeto_web1.jogos_internos.model.Curso;
import com.projeto_web1.jogos_internos.repository.CampusRepository;
import com.projeto_web1.jogos_internos.repository.CoordenadorRepository;
import com.projeto_web1.jogos_internos.repository.CursoRepository;
import com.projeto_web1.jogos_internos.repository.EquipeRepository;
import com.projeto_web1.jogos_internos.service.curso.CursoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CursoServiceImpl implements CursoService {

    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private CampusRepository campusRepository;
    @Autowired
    private CoordenadorRepository coordenadorRepository;
    @Autowired
    private EquipeRepository equipeRepository; // Para a validação de segurança

    // CREATE
    @Override
    @Transactional
    public Curso salvar(Curso curso) {
        // Validação para impedir nomes duplicados no mesmo campus
        if (cursoRepository.existsByNomeAndCampus(curso.getNome(), curso.getCampus())) {
            throw new IllegalStateException("Já existe um curso com este nome neste campus.");
        }
        return cursoRepository.save(curso);
    }

    // READ (Listar todos)
    @Override
    @Transactional(readOnly = true)
    public List<Curso> listarTodos() {
        return cursoRepository.findAll();
    }

    // READ (Buscar por ID)
    @Override
    @Transactional(readOnly = true)
    public Curso buscarPorId(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com o ID: " + id));
    }

    // UPDATE
    @Override
    @Transactional
    public Curso atualizar(Long id, Curso curso) {
        Curso cursoExistente = buscarPorId(id);

        // Atualiza os dados
        cursoExistente.setNome(curso.getNome());
        cursoExistente.setNivel(curso.getNivel());
        cursoExistente.setCampus(curso.getCampus());
        cursoExistente.setCoordenador(curso.getCoordenador());

        return cursoRepository.save(cursoExistente);
    }

    // DELETE
    @Override
    @Transactional
    public void deletar(Long id) {

        // Antes de apagar, verifica se existe alguma equipe associada a este curso
        boolean existeEquipeNoCurso = equipeRepository.existsByCursoIdCurso(id);

        if (existeEquipeNoCurso) {
            throw new IllegalStateException("Não é possível apagar este curso, pois ele já possui equipes associadas.");
        }

        cursoRepository.deleteById(id);
    }
}