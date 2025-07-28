package com.projeto_web1.jogos_internos.service.atleta.impl;

import com.projeto_web1.jogos_internos.model.Atleta;
import com.projeto_web1.jogos_internos.repository.AtletaRepository;
import com.projeto_web1.jogos_internos.repository.EquipeRepository;
import com.projeto_web1.jogos_internos.repository.UsuarioRepository;
import com.projeto_web1.jogos_internos.service.atleta.AtletaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AtletaServiceImpl implements AtletaService {

    @Autowired
    private AtletaRepository atletaRepository;

    @Autowired
    private EquipeRepository equipeRepository; // Para a nossa validação de segurança

    @Autowired
    private UsuarioRepository usuarioRepository;

    // READ (Listar todos)
    @Override
    @Transactional(readOnly = true)
    public List<Atleta> listarTodos() {
        return atletaRepository.findAll();
    }

    // READ (Buscar por ID)
    @Override
    @Transactional(readOnly = true)
    public Atleta buscarPorId(Long id) {
        return atletaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Atleta não encontrado com o ID: " + id));
    }

    // UPDATE
    @Override
    @Transactional
    public Atleta atualizar(Long id, Atleta atleta) {
        Atleta atletaExistente = buscarPorId(id);

        // Atualiza apenas os dados permitidos
        atletaExistente.setNomeCompleto(atleta.getNomeCompleto());
        atletaExistente.setApelido(atleta.getApelido());
        atletaExistente.setTelefone(atleta.getTelefone());

        return atletaRepository.save(atletaExistente);
    }

    // DELETE
    @Override
    @Transactional
    public void deletar(Long id) {

        // Antes de apagar, verifica se o atleta faz parte de alguma equipe
        boolean atletaEstaEmEquipe = equipeRepository.existsByAtletas_IdAtleta(id);

        if (atletaEstaEmEquipe) {
            throw new IllegalStateException("Não é possível apagar este atleta, pois ele já está associado a uma ou mais equipes.");
        }

        Atleta atletaParaApagar = buscarPorId(id); // Reutilizamos o nosso método de busca
        Long idUsuario = atletaParaApagar.getUsuario().getIdUsuario();

        //  apagamos o filho primeiro
        atletaRepository.deleteById(id);

        // E depois apagamos o pai(usuário);
        usuarioRepository.deleteById(idUsuario);


    }
}