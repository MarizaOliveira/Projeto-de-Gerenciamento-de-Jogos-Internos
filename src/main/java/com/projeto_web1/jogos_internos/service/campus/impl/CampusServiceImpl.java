package com.projeto_web1.jogos_internos.service.campus.impl;

import com.projeto_web1.jogos_internos.model.Campus;
import com.projeto_web1.jogos_internos.repository.CampusRepository;
import com.projeto_web1.jogos_internos.repository.CursoRepository;
import com.projeto_web1.jogos_internos.service.campus.CampusService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CampusServiceImpl implements CampusService {

    @Autowired
    private CampusRepository campusRepository;

    @Autowired
    private CursoRepository cursoRepository; // Para a nossa validação de segurança

    // CREATE
    @Override
    @Transactional
    public Campus salvar(Campus campus) {
        // Validação para impedir nomes duplicados
        if (campusRepository.existsByNome(campus.getNome())) {
            throw new IllegalStateException("Já existe um campus com o nome: " + campus.getNome());
        }
        return campusRepository.save(campus);
    }

    // READ (Listar todos)
    @Override
    @Transactional(readOnly = true)
    public List<Campus> listarTodos() {
        return campusRepository.findAll();
    }

    // READ (Buscar por ID)
    @Override
    @Transactional(readOnly = true)
    public Campus buscarPorId(Long id) {
        return campusRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Campus não encontrado com o ID: " + id));
    }

    // UPDATE
    @Override
    @Transactional
    public Campus atualizar(Long id, Campus campus) {
        // Primeiro, busca o campus existente para garantir que ele está no banco
        Campus campusExistente = buscarPorId(id);

        // Atualiza o nome
        campusExistente.setNome(campus.getNome());

        // Salva as alterações. O JPA entende que é um UPDATE.
        return campusRepository.save(campusExistente);
    }

    // DELETE
    @Override
    @Transactional
    public void deletar(Long id) {
        // vaidação de segurança
        // Antes de apagar, verifica se existe algum curso associado a este campus
        boolean existeCursoNoCampus = cursoRepository.existsByCampusIdCampus(id);

        if (existeCursoNoCampus) {
            // Se houver cursos, a operação é barrada.
            throw new IllegalStateException("Não é possível apagar este campus, pois ele já possui cursos associados.");
        }

        // Se estiver seguro, apaga o campus
        campusRepository.deleteById(id);
    }
}