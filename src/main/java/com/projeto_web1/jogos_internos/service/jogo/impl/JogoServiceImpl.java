package com.projeto_web1.jogos_internos.service.jogo.impl;

import com.projeto_web1.jogos_internos.model.Equipe;
import com.projeto_web1.jogos_internos.model.Jogo;
import com.projeto_web1.jogos_internos.model.TipoUsuario;
import com.projeto_web1.jogos_internos.model.Usuario;
import com.projeto_web1.jogos_internos.repository.EquipeRepository;
import com.projeto_web1.jogos_internos.repository.JogoRepository;
import com.projeto_web1.jogos_internos.repository.UsuarioRepository;
import com.projeto_web1.jogos_internos.service.jogo.JogoService;
import com.projeto_web1.jogos_internos.service.jogo.dto.ResultadoDTO;
import com.projeto_web1.jogos_internos.service.jogo.form.ResultadoForm;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JogoServiceImpl implements JogoService {

    @Autowired
    private JogoRepository jogoRepository;

    @Autowired
    private EquipeRepository equipeRepository; // Precisamos para buscar a equipe do W.O.

    @Autowired
    private UsuarioRepository usuarioRepository;



    @Override
    @Transactional
    public ResultadoDTO registrarResultado(Long jogoId,Long idArbitro, ResultadoForm form) {
        // Busca o jogo no banco.
        Jogo jogo = jogoRepository.findById(jogoId)
                .orElseThrow(() -> new EntityNotFoundException("Jogo não encontrado com o ID: " + jogoId));

        // Busca o usuário (árbitro) que está a submeter o resultado
        Usuario arbitro = usuarioRepository.findById(idArbitro)
                .orElseThrow(() -> new EntityNotFoundException("Usuário (árbitro) não encontrado com o ID: " + idArbitro));


        // Validações...
        if (!"AGENDADO".equalsIgnoreCase(jogo.getStatus())) {
            throw new IllegalStateException("Este jogo já foi finalizado ou cancelado.");
        }

        if (arbitro.getTipoUsuario() != TipoUsuario.ARBITRO) {
            throw new IllegalStateException("Ação não permitida. Apenas usuários do tipo ARBITRO podem registrar resultados.");
        }


        Equipe equipeVencedora = null; // Começamos assumindo que não há vencedor (empate)

        jogo.setArbitro(arbitro);

        // Verifica se o formulário indica um W.O.
        if (form.getWo() != null && form.getWo()) {
            if (form.getIdEquipeVencedoraWo() == null) {
                throw new IllegalArgumentException("Para registrar W.O., é necessário informar a equipa vencedora.");
            }
            equipeVencedora = equipeRepository.findById(form.getIdEquipeVencedoraWo())
                    .orElseThrow(() -> new EntityNotFoundException("Equipa vencedora do W.O. não encontrada."));

            jogo.setStatus("WO");
            jogo.setEquipeVencedoraWo(equipeVencedora);

        } else {
            // Lógica para placar normal
            if (form.getPlacarEquipeA() == null || form.getPlacarEquipeB() == null) {
                throw new IllegalArgumentException("Para um resultado normal, ambos os placares devem ser informados.");
            }

            boolean isMataMata = !"FASE_DE_GRUPOS".equals(jogo.getFase());
            boolean isEmpate = form.getPlacarEquipeA().equals(form.getPlacarEquipeB());

            if (isMataMata && isEmpate) {
                if (form.getPenaltiPlacarA() == null || form.getPenaltiPlacarB() == null) {
                    throw new IllegalStateException("Jogos de mata-mata que terminam empatados precisam de um resultado de pênaltis.");
                }
                if (form.getPenaltiPlacarA().equals(form.getPenaltiPlacarB())) {
                    throw new IllegalStateException("O resultado dos pênaltis não pode ser um empate.");
                }
                jogo.setPenaltiPlacarA(form.getPenaltiPlacarA());
                jogo.setPenaltiPlacarB(form.getPenaltiPlacarB());
                equipeVencedora = form.getPenaltiPlacarA() > form.getPenaltiPlacarB() ? jogo.getEquipeA() : jogo.getEquipeB();
            } else if (!isEmpate) {
                equipeVencedora = form.getPlacarEquipeA() > form.getPlacarEquipeB() ? jogo.getEquipeA() : jogo.getEquipeB();
            }

            jogo.setStatus("FINALIZADO");
            jogo.setPlacarEquipeA(form.getPlacarEquipeA());
            jogo.setPlacarEquipeB(form.getPlacarEquipeB());
        }

        jogoRepository.save(jogo);

        // montando resposta de dinamica
        ResultadoDTO resposta = new ResultadoDTO();
        if (equipeVencedora != null) {
            resposta.setMensagem("Vitória da equipe " + equipeVencedora.getNome() + "!");

        } else {
            resposta.setMensagem("O jogo terminou empatado.");

        }

        return resposta;
    }


    @Override
    @Transactional
    public void desfazerWo(Long jogoId, Long idArbitro) {
        //Busca as entidades
        Jogo jogo = jogoRepository.findById(jogoId)
                .orElseThrow(() -> new EntityNotFoundException("Jogo não encontrado com o ID: " + jogoId));

        Usuario arbitro = usuarioRepository.findById(idArbitro)
                .orElseThrow(() -> new EntityNotFoundException("Usuário (árbitro) não encontrado com o ID: " + idArbitro));

        //Validações de segurança
        if (arbitro.getTipoUsuario() != TipoUsuario.ARBITRO) {
            throw new IllegalStateException("Ação não permitida. Apenas usuários do tipo ARBITRO podem desfazer um W.O.");
        }
        if (!"WO".equalsIgnoreCase(jogo.getStatus())) {
            throw new IllegalStateException("Este jogo não está com status de W.O., portanto a ação não pode ser desfeita.");
        }

        // Reverte as alterações
        jogo.setStatus("AGENDADO");
        jogo.setEquipeVencedoraWo(null);

        //mantemos o árbitro que desfez a ação como o árbitro do jogo
        jogo.setArbitro(arbitro);

        //Salva o jogo com o estado revertido
        jogoRepository.save(jogo);
    }

}