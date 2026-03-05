package com.projeto_web1.jogos_internos.service.coordenador.impl;

import com.projeto_web1.jogos_internos.model.Atleta;
import com.projeto_web1.jogos_internos.model.Coordenador;
import com.projeto_web1.jogos_internos.model.Curso;
import com.projeto_web1.jogos_internos.repository.AtletaRepository;
import com.projeto_web1.jogos_internos.repository.CoordenadorRepository;
import com.projeto_web1.jogos_internos.repository.CursoRepository;
import com.projeto_web1.jogos_internos.service.coordenador.CoordenadorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CoordenadorServiceImpl implements CoordenadorService {

    @Autowired
    private AtletaRepository atletaRepository;

    @Autowired
    private CoordenadorRepository coordenadorRepository;

    @Autowired
    private CursoRepository cursoRepository;


    // No seu ficheiro CoordenadorServiceImpl.java, substitua o método

    @Override
    @Transactional
    public void habilitarAtletaComoTecnico(Long idCoordenador, Long idAtleta) {

        // Busca o coordenador que está a tentar realizar a ação
        Coordenador coordenador = coordenadorRepository.findById(idCoordenador)
                .orElseThrow(() -> new EntityNotFoundException("Coordenador não encontrado com o ID: " + idCoordenador));

        // Busca o atleta que será habilitado
        Atleta atleta = atletaRepository.findById(idAtleta)
                .orElseThrow(() -> new EntityNotFoundException("Atleta não encontrado com o ID: " + idAtleta));


        // Busca o curso que é coordenado por este coordenador
        Curso cursoDoCoordenador = cursoRepository.findByCoordenadorIdCoordenador(idCoordenador)
                .orElseThrow(() -> new IllegalStateException("Coordenador não está associado a nenhum curso."));

        // Verifica se o atleta pertence ao mesmo curso do coordenador
        if (atleta.getCurso() == null || !atleta.getCurso().equals(cursoDoCoordenador)) {
            throw new IllegalStateException("Ação não permitida. O coordenador só pode habilitar atletas do seu próprio curso.");
        }


        // Se a validação passar, a lógica continua
        atleta.setTecnicoHabilitado(true);
        atletaRepository.save(atleta);
    }

    @Override
    @Transactional
    public void desabilitarAtletaComoTecnico(Long idCoordenador, Long idAtleta) {

        // Busca o coordenador que está a tentar realizar a ação
        Coordenador coordenador = coordenadorRepository.findById(idCoordenador)
                .orElseThrow(() -> new EntityNotFoundException("Coordenador não encontrado com o ID: " + idCoordenador));

        // Busca o atleta que será desabilitado
        Atleta atleta = atletaRepository.findById(idAtleta)
                .orElseThrow(() -> new EntityNotFoundException("Atleta não encontrado com o ID: " + idAtleta));

        // Busca o curso que é coordenado por este coordenador
        Curso cursoDoCoordenador = cursoRepository.findByCoordenadorIdCoordenador(idCoordenador)
                .orElseThrow(() -> new IllegalStateException("Coordenador não está associado a nenhum curso."));

        // Verifica se o atleta pertence ao mesmo curso do coordenador
        if (atleta.getCurso() == null || !atleta.getCurso().equals(cursoDoCoordenador)) {
            throw new IllegalStateException("Ação não permitida. O coordenador só pode desabilitar atletas do seu próprio curso.");
        }


        // Ação principal: muda o status para 'false'
        atleta.setTecnicoHabilitado(false);
        atletaRepository.save(atleta);
    }

    @Override
    @Transactional(readOnly = true)
    public Coordenador buscarPorId(Long id) {
        return coordenadorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Coordenador não encontrado com o ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Coordenador> listarTodos() {
        return coordenadorRepository.findAll();
    }
}