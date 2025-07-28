package com.projeto_web1.jogos_internos.service.esporte.impl;

import com.projeto_web1.jogos_internos.model.Esporte;
import com.projeto_web1.jogos_internos.repository.EquipeRepository;
import com.projeto_web1.jogos_internos.repository.EsporteRepository;
import com.projeto_web1.jogos_internos.service.esporte.EsporteService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EsporteServiceImpl implements EsporteService {

    @Autowired
    private EsporteRepository esporteRepository;

    @Autowired
    private EquipeRepository equipeRepository; //validação de segurança

    // CREATE
    @Override
    @Transactional
    public Esporte salvar(Esporte esporte) {
        // Validação para impedir nomes duplicados
        if (esporteRepository.existsByNome(esporte.getNome())) {
            throw new IllegalStateException("Já existe um esporte com o nome: " + esporte.getNome());
        }
        return esporteRepository.save(esporte);
    }

    // READ (Listar todos)
    @Override
    @Transactional(readOnly = true)
    public List<Esporte> listarTodos() {
        return esporteRepository.findAll();
    }

    // READ (Buscar por ID)
    @Override
    @Transactional(readOnly = true)
    public Esporte buscarPorId(Long id) {
        return esporteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Esporte não encontrado com o ID: " + id));
    }

    // UPDATE
    @Override
    @Transactional
    public Esporte atualizar(Long id, Esporte esporte) {
        Esporte esporteExistente = buscarPorId(id);

        // Atualiza os dados
        esporteExistente.setNome(esporte.getNome());
        esporteExistente.setMinAtletas(esporte.getMinAtletas());
        esporteExistente.setMaxAtletas(esporte.getMaxAtletas());

        return esporteRepository.save(esporteExistente);
    }

    // DELETE
    @Override
    @Transactional
    public void deletar(Long id) {
        //validação
        // Antes de apagar, verifica se existe alguma equipe associada a este esporte
        boolean existeEquipeNoEsporte = equipeRepository.existsByEsporteIdEsporte(id);

        if (existeEquipeNoEsporte) {
            throw new IllegalStateException("Não é possível apagar este esporte, pois ele já possui equipes associadas.");
        }

        esporteRepository.deleteById(id);
    }
}