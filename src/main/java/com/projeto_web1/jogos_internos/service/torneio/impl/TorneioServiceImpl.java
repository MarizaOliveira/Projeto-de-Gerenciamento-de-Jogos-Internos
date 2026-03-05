package com.projeto_web1.jogos_internos.service.torneio.impl;

import com.projeto_web1.jogos_internos.model.Equipe;
import com.projeto_web1.jogos_internos.model.Grupo;
import com.projeto_web1.jogos_internos.model.Jogo;
import com.projeto_web1.jogos_internos.repository.EquipeRepository;
import com.projeto_web1.jogos_internos.repository.GrupoRepository;
import com.projeto_web1.jogos_internos.repository.JogoRepository;
import com.projeto_web1.jogos_internos.service.torneio.TorneioService;

import com.projeto_web1.jogos_internos.service.torneio.dto.ClassificadoPair;
import com.projeto_web1.jogos_internos.service.torneio.dto.GrupoDTO;
import com.projeto_web1.jogos_internos.service.torneio.dto.TimeClassificacaoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class TorneioServiceImpl implements TorneioService {

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private JogoRepository jogoRepository;

    @Override
    @Transactional
    public void gerarGruposEJogos(Long eventoId, Long esporteId) {

        // Caso tente Gerar os Grupos novamente;
        List<Grupo> gruposExistentes = grupoRepository.findGruposByEventoAndEsporte(eventoId, esporteId);
        if (!gruposExistentes.isEmpty()) {
            System.out.println("Sorteio anterior encontrado. Limpando dados antigos...");
            List<Long> idsDosGruposParaApagar = gruposExistentes.stream()
                    .map(Grupo::getIdGrupo)
                    .collect(Collectors.toList());

            // Apaga os jogos e depois os grupos
            jogoRepository.deleteAllByGrupo_IdGrupoIn(idsDosGruposParaApagar);
            grupoRepository.deleteAllByIdGrupoIn(idsDosGruposParaApagar);
        }


        //  Buscar, Validar, Embaralhar e Calcular Configuração
        List<Equipe> equipesInscritas = equipeRepository.findAllByEvento_IdEventoAndEsporte_IdEsporte(eventoId, esporteId);
        if (equipesInscritas.size() < 3) {
            throw new IllegalStateException("Não é possível gerar um torneio com menos de 3 equipes.");
        }
        Collections.shuffle(equipesInscritas);
        List<Integer> tamanhoDosGrupos = calcularTamanhosDeGrupo(equipesInscritas.size());
        System.out.println("Configuração de grupos calculada: " + tamanhoDosGrupos);

        // Criar Grupos, Distribuir Equipes e Gerar Jogos
        char nomeDoGrupoChar = 'A';
        int indiceAtualDaEquipe = 0;
        List<Jogo> todosOsJogosGerados = new ArrayList<>();

        // Laço principal que itera sobre a nossa "planta" de grupos
        for (Integer tamanhoDoGrupo : tamanhoDosGrupos) {
            // Criar e Salvar o Grupo
            Grupo novoGrupo = new Grupo();
            novoGrupo.setNome("Grupo " + nomeDoGrupoChar);
            Grupo grupoSalvo = grupoRepository.save(novoGrupo);
            nomeDoGrupoChar++; // Incrementa a letra para o próximo grupo (A -> B -> C...)

            // Distribuir as Equipes para este grupo
            // Pega uma "fatia" da lista de equipes embaralhada
            int fimDaFatia = indiceAtualDaEquipe + tamanhoDoGrupo;
            List<Equipe> equipesDoGrupo = equipesInscritas.subList(indiceAtualDaEquipe, fimDaFatia);
            indiceAtualDaEquipe = fimDaFatia; // Atualiza o índice para não pegar as mesmas equipes de novo

            // Associa as equipes ao grupo na tabela de junção
            grupoSalvo.getEquipes().addAll(equipesDoGrupo);
            //grupoRepository.save(grupoSalvo);

            //  Gerar os Jogos "todos contra todos" para este grupo
            for (int i = 0; i < equipesDoGrupo.size(); i++) {
                for (int j = i + 1; j < equipesDoGrupo.size(); j++) {
                    Equipe equipeA = equipesDoGrupo.get(i);
                    Equipe equipeB = equipesDoGrupo.get(j);

                    // Calcula uma data e hora para o jogo, adicionando 2 horas para cada jogo já criado na lista
                    LocalDateTime dataDoJogo = LocalDateTime.now().plusDays(1).plusHours(todosOsJogosGerados.size() * 2);


                    Jogo novoJogo = new Jogo();
                    novoJogo.setEquipeA(equipeA);
                    novoJogo.setEquipeB(equipeB);
                    novoJogo.setGrupo(grupoSalvo);
                    novoJogo.setFase("FASE_DE_GRUPOS");
                    novoJogo.setStatus("AGENDADO");

                    novoJogo.setDataHora(dataDoJogo);

                    novoJogo.setArbitro(null);

                    todosOsJogosGerados.add(novoJogo);
                }
            }
        }

        // Salvar todos os jogos gerados de uma só vez
        jogoRepository.saveAll(todosOsJogosGerados);
        System.out.println(todosOsJogosGerados.size() + " jogos da fase de grupos foram gerados com sucesso!");
    }

    // Algoritmo dinâmico com a prioridade 3 -> 4 -> 5
    private List<Integer> calcularTamanhosDeGrupo(int totalEquipes) {
        if (totalEquipes < 3) { // Casos base onde não é possível formar grupos
            throw new IllegalStateException("Número de equipes insuficiente para formar grupos. Mínimo de 3.");
        }

        // Itera tentando encontrar uma solução com o MENOR número de grupos de 5 possível.
        for (int numGruposDe5 = 0; numGruposDe5 * 5 <= totalEquipes; numGruposDe5++) {
            int equipesRestantes_apos5s = totalEquipes - (numGruposDe5 * 5);

            // Com o que sobrou, tenta formar com 3s e 4s, priorizando o máximo de 3s.
            for (int numGruposDe3 = equipesRestantes_apos5s / 3; numGruposDe3 >= 0; numGruposDe3--) {
                int equipesRestantes_apos3s = equipesRestantes_apos5s - (numGruposDe3 * 3);

                // Se o resto final for perfeitamente divisível por 4, encontramos a melhor solução!
                if (equipesRestantes_apos3s % 4 == 0) {
                    int numGruposDe4 = equipesRestantes_apos3s / 4;

                    // Monta a lista de distribuição e a retorna
                    List<Integer> distribuicao = new ArrayList<>();
                    for (int i = 0; i < numGruposDe3; i++) distribuicao.add(3);
                    for (int i = 0; i < numGruposDe4; i++) distribuicao.add(4);
                    for (int i = 0; i < numGruposDe5; i++) distribuicao.add(5);

                    return distribuicao;
                }
            }
        }

        // Este ponto não deve ser alcançado se a lógica estiver correta para N >= 3
        throw new IllegalStateException("Não foi possível encontrar uma distribuição de grupos válida para " + totalEquipes + " equipes.");
    }


    @Override
    @Transactional(readOnly = true)
    public List<TimeClassificacaoDTO> getTabelaDeClassificacao(Long grupoId) {
        //  Preparar a Tabela em Branco
        Grupo grupo = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new EntityNotFoundException("Grupo não encontrado com o ID: " + grupoId));

        // Cria um mapa para acessar facilmente as estatísticas de cada time pelo seu ID
        Map<Long, TimeClassificacaoDTO> estatisticas = new HashMap<>();
        for (Equipe equipe : grupo.getEquipes()) {
            estatisticas.put(equipe.getIdEquipe(), new TimeClassificacaoDTO(equipe));
        }

        //  Processar os Jogos Finalizados
        List<Jogo> jogosFinalizados = jogoRepository.findAllByGrupoAndStatusIn(grupo, List.of("FINALIZADO", "WO"));

        // Computar os Resultados Jogo a Jogo
        for (Jogo jogo : jogosFinalizados) {
            if ("WO".equals(jogo.getStatus())) {
                // Lógica para W.O.
                TimeClassificacaoDTO timeVencedor = estatisticas.get(jogo.getEquipeVencedoraWo().getIdEquipe());
                timeVencedor.setPontos(timeVencedor.getPontos() + 3);
                timeVencedor.setVitorias(timeVencedor.getVitorias() + 1);
                timeVencedor.setJogosJogados(timeVencedor.getJogosJogados() + 1);

                // Encontra o time perdedor e atualiza suas estatísticas
                Equipe equipePerdedora = jogo.getEquipeA().equals(jogo.getEquipeVencedoraWo()) ? jogo.getEquipeB() : jogo.getEquipeA();
                TimeClassificacaoDTO timePerdedor = estatisticas.get(equipePerdedora.getIdEquipe());
                timePerdedor.setDerrotas(timePerdedor.getDerrotas() + 1);
                timePerdedor.setJogosJogados(timePerdedor.getJogosJogados() + 1);

            } else { // Lógica para jogo com placar normal
                TimeClassificacaoDTO timeA = estatisticas.get(jogo.getEquipeA().getIdEquipe());
                TimeClassificacaoDTO timeB = estatisticas.get(jogo.getEquipeB().getIdEquipe());

                // Atualiza jogos jogados e gols
                timeA.setJogosJogados(timeA.getJogosJogados() + 1);
                timeB.setJogosJogados(timeB.getJogosJogados() + 1);
                timeA.setGolsPro(timeA.getGolsPro() + jogo.getPlacarEquipeA());
                timeA.setGolsContra(timeA.getGolsContra() + jogo.getPlacarEquipeB());
                timeB.setGolsPro(timeB.getGolsPro() + jogo.getPlacarEquipeB());
                timeB.setGolsContra(timeB.getGolsContra() + jogo.getPlacarEquipeA());

                // Atribui pontos
                if (jogo.getPlacarEquipeA() > jogo.getPlacarEquipeB()) { // Vitória do Time A
                    timeA.setPontos(timeA.getPontos() + 3);
                    timeA.setVitorias(timeA.getVitorias() + 1);
                    timeB.setDerrotas(timeB.getDerrotas() + 1);
                } else if (jogo.getPlacarEquipeB() > jogo.getPlacarEquipeA()) { // Vitória do Time B
                    timeB.setPontos(timeB.getPontos() + 3);
                    timeB.setVitorias(timeB.getVitorias() + 1);
                    timeA.setDerrotas(timeA.getDerrotas() + 1);
                } else { // Empate
                    timeA.setPontos(timeA.getPontos() + 1);
                    timeA.setEmpates(timeA.getEmpates() + 1);
                    timeB.setPontos(timeB.getPontos() + 1);
                    timeB.setEmpates(timeB.getEmpates() + 1);
                }
            }
        }

        // Cálculos Finais e Ordenação
        List<TimeClassificacaoDTO> tabelaFinal = new ArrayList<>(estatisticas.values());

        // Calcula o saldo de gols para todos
        tabelaFinal.forEach(TimeClassificacaoDTO::calcularSaldoDeGols);

        // Ordena a lista com os critérios de desempate
        tabelaFinal.sort((t1, t2) -> {
            // 1. Compara por Pontos (maior primeiro)
            int pontosComp = Integer.compare(t2.getPontos(), t1.getPontos());
            if (pontosComp != 0) {
                return pontosComp;
            }
            // 2. Se os pontos forem iguais, compara por Saldo de Golos (maior primeiro)
            int saldoComp = Integer.compare(t2.getSaldoDeGols(), t1.getSaldoDeGols());
            if (saldoComp != 0) {
                return saldoComp;
            }
            // 3. Se o saldo for igual, compara por Golos Pró (maior primeiro)
            return Integer.compare(t2.getGolsPro(), t1.getGolsPro());
        });
        for (int i = 0; i < tabelaFinal.size(); i++) {
            tabelaFinal.get(i).setPosicaoNoGrupo(i + 1); // Posição 1, 2, 3...
        }

        return tabelaFinal;
    }



    @Override
    @Transactional
    public void gerarQuartasDeFinal(Long eventoId, Long esporteId) {
        boolean existemJogosAgendados = jogoRepository.existsAgendadoInFaseDeGrupos(eventoId, esporteId);

        if (existemJogosAgendados) {
            throw new IllegalStateException("Não é possível gerar as quartas de final. Todos os jogos da fase de grupos precisam de ter um resultado registado.");
        }
        // Coletar e Ranquer TODOS os Classificados
        List<Grupo> gruposDoTorneio = grupoRepository.findGruposByEventoAndEsporte(eventoId, esporteId);
        if (gruposDoTorneio.isEmpty()) {
            throw new IllegalStateException("Nenhum grupo encontrado para gerar o mata-mata.");
        }

        List<ClassificadoPair> paresDeClassificados = new ArrayList<>();
        for (Grupo grupo : gruposDoTorneio) {
            List<TimeClassificacaoDTO> classificacaoDoGrupo = getTabelaDeClassificacao(grupo.getIdGrupo());
            if (classificacaoDoGrupo.size() >= 2) {
                paresDeClassificados.add(new ClassificadoPair(classificacaoDoGrupo.get(0), classificacaoDoGrupo.get(1)));
            }
        }
        paresDeClassificados.sort(Comparator.comparing((ClassificadoPair par) -> par.getPrimeiroColocado().getPontos()).reversed()
                .thenComparing((ClassificadoPair par) -> par.getPrimeiroColocado().getSaldoDeGols()).reversed()
                .thenComparing((ClassificadoPair par) -> par.getPrimeiroColocado().getGolsPro()).reversed());


        // Lógica principal de decisão da fase
        List<Jogo> jogosMataMata = new ArrayList<>();
        int totalClassificados = paresDeClassificados.size() * 2;

        if (totalClassificados > 8) {

            throw new UnsupportedOperationException("Lógica de Play-in para mais de 8 times ainda não implementada com a nova estrutura de DTO.");

        } else if (totalClassificados == 8) {
            // Lógica para 8 times (Quartas de Final Cheia)
            ClassificadoPair par1 = paresDeClassificados.get(0);
            ClassificadoPair par2 = paresDeClassificados.get(1);
            ClassificadoPair par3 = paresDeClassificados.get(2);
            ClassificadoPair par4 = paresDeClassificados.get(3);


            // Buscamos a equipe usando o ID que está no DTO
            Equipe eq_1_1 = equipeRepository.findById(par1.getPrimeiroColocado().getEquipeId()).orElseThrow();
            Equipe eq_4_2 = equipeRepository.findById(par4.getSegundoColocado().getEquipeId()).orElseThrow();
            jogosMataMata.add(criarJogoMataMata(eq_1_1, eq_4_2, "QUARTAS_DE_FINAL", null, jogosMataMata.size()));

            Equipe eq_2_1 = equipeRepository.findById(par2.getPrimeiroColocado().getEquipeId()).orElseThrow();
            Equipe eq_3_2 = equipeRepository.findById(par3.getSegundoColocado().getEquipeId()).orElseThrow();
            jogosMataMata.add(criarJogoMataMata(eq_2_1, eq_3_2, "QUARTAS_DE_FINAL", null, jogosMataMata.size()));

            Equipe eq_3_1 = equipeRepository.findById(par3.getPrimeiroColocado().getEquipeId()).orElseThrow();
            Equipe eq_2_2 = equipeRepository.findById(par2.getSegundoColocado().getEquipeId()).orElseThrow();
            jogosMataMata.add(criarJogoMataMata(eq_3_1, eq_2_2, "QUARTAS_DE_FINAL", null, jogosMataMata.size()));

            Equipe eq_4_1 = equipeRepository.findById(par4.getPrimeiroColocado().getEquipeId()).orElseThrow();
            Equipe eq_1_2 = equipeRepository.findById(par1.getSegundoColocado().getEquipeId()).orElseThrow();
            jogosMataMata.add(criarJogoMataMata(eq_4_1, eq_1_2, "QUARTAS_DE_FINAL", null, jogosMataMata.size()));

        } else if (totalClassificados == 6) {
            // Lógica para 6 times (Quartas com Byes)
            ClassificadoPair par1 = paresDeClassificados.get(0);
            ClassificadoPair par2 = paresDeClassificados.get(1);
            ClassificadoPair par3 = paresDeClassificados.get(2);


            Equipe bye1 = equipeRepository.findById(par1.getPrimeiroColocado().getEquipeId()).orElseThrow();
            Equipe bye2 = equipeRepository.findById(par2.getPrimeiroColocado().getEquipeId()).orElseThrow();
            System.out.println("BYE para: " + bye1.getNome());
            System.out.println("BYE para: " + bye2.getNome());

            Equipe eq_3_1 = equipeRepository.findById(par3.getPrimeiroColocado().getEquipeId()).orElseThrow();
            Equipe eq_1_2 = equipeRepository.findById(par1.getSegundoColocado().getEquipeId()).orElseThrow();
            jogosMataMata.add(criarJogoMataMata(eq_3_1, eq_1_2, "QUARTAS_DE_FINAL", null, jogosMataMata.size()));

            Equipe eq_2_2 = equipeRepository.findById(par2.getSegundoColocado().getEquipeId()).orElseThrow();
            Equipe eq_3_2 = equipeRepository.findById(par3.getSegundoColocado().getEquipeId()).orElseThrow();
            jogosMataMata.add(criarJogoMataMata(eq_2_2, eq_3_2, "QUARTAS_DE_FINAL", null, jogosMataMata.size()));

        } else if (totalClassificados == 4) {
            // Lógica para 4 times (Semifinal Direta)
            ClassificadoPair par1 = paresDeClassificados.get(0);
            ClassificadoPair par2 = paresDeClassificados.get(1);


            Equipe eq_1_1 = equipeRepository.findById(par1.getPrimeiroColocado().getEquipeId()).orElseThrow();
            Equipe eq_2_2 = equipeRepository.findById(par2.getSegundoColocado().getEquipeId()).orElseThrow();
            jogosMataMata.add(criarJogoMataMata(eq_1_1, eq_2_2, "SEMIFINAL", null, jogosMataMata.size()));

            Equipe eq_2_1 = equipeRepository.findById(par2.getPrimeiroColocado().getEquipeId()).orElseThrow();
            Equipe eq_1_2 = equipeRepository.findById(par1.getSegundoColocado().getEquipeId()).orElseThrow();
            jogosMataMata.add(criarJogoMataMata(eq_2_1, eq_1_2, "SEMIFINAL", null, jogosMataMata.size()));

            // Caso de 1 grupo/ 2 classificados ( direto na final)
        }  else if (totalClassificados == 2) {
            // Lógica para 2 times (Final Direta)
            System.out.println("Apenas 2 equipes classificadas. Gerando a FINAL diretamente...");
            // Pegao único par da lista (1º e 2º colocados do único grupo)
            ClassificadoPair par1 = paresDeClassificados.get(0);
            // busca primeira equipe
            Equipe finalista1 = equipeRepository.findById(par1.getPrimeiroColocado().getEquipeId())
                    .orElseThrow(() -> new EntityNotFoundException("Equipe finalista não encontrada com ID: " + par1.getPrimeiroColocado().getEquipeId()));

            // Busca 2 equipe
            Equipe finalista2 = equipeRepository.findById(par1.getSegundoColocado().getEquipeId())
                    .orElseThrow(() -> new EntityNotFoundException("Equipe finalista não encontrada com ID: " + par1.getSegundoColocado().getEquipeId()));

            // cria jogo final
            jogosMataMata.add(criarJogoMataMata(finalista1, finalista2, "FINAL", null, jogosMataMata.size()));

        }else {
            throw new IllegalStateException("Número de classificados (" + totalClassificados + ") insuficiente para gerar o mata-mata.");
        }

        jogoRepository.saveAll(jogosMataMata);
        System.out.println(jogosMataMata.size() + " jogos gerados para a próxima fase.");
    }

   //Metofo auxiliar
    private Jogo criarJogoMataMata(Equipe equipeA, Equipe equipeB, String fase, Grupo grupo, int contadorDeJogos) {
        Jogo novoJogo = new Jogo();
        novoJogo.setEquipeA(equipeA);
        novoJogo.setEquipeB(equipeB);
        novoJogo.setFase(fase);
        novoJogo.setStatus("AGENDADO");
        novoJogo.setGrupo(grupo);

        // Calcula uma data e hora para o jogo, adicionando 2 horas para cada jogo já criado
        LocalDateTime dataDoJogo = LocalDateTime.now().plusDays(10).plusHours(contadorDeJogos * 2); // Começa 10 dias depois para separar bem
        novoJogo.setDataHora(dataDoJogo);



        return novoJogo;
    }


    @Override
    @Transactional
    public void gerarSemifinal(Long eventoId, Long esporteId) {
        String faseAnterior = "QUARTAS_DE_FINAL";
        System.out.println("Iniciando geração da SEMIFINAL...");

        // 1. Busca os jogos das quartas que já terminaram, EM ORDEM
        List<Jogo> jogosDasQuartas = jogoRepository.findAllByFaseAndStatusInOrderByIdJogoAsc(faseAnterior, List.of("FINALIZADO", "WO"));

        // 2. Coleta os vencedores na ordem do chaveamento
        List<Equipe> vencedoresDosJogos = new ArrayList<>();
        for (Jogo jogo : jogosDasQuartas) {
            vencedoresDosJogos.add(getVencedorDoJogo(jogo));
        }

        // 3. Busca os times que tiveram "bye" na fase anterior
        List<Equipe> timesComBye = buscarTimesComBye(eventoId, esporteId);

        // 4. Monta os jogos da semifinal com o chaveamento fixo e correto
        List<Jogo> jogosDaSemifinal = new ArrayList<>();

        if (!timesComBye.isEmpty()) { // Cenário de 6 equipas (2 vencedores + 2 "byes")
            // Chaveamento: O 1º "bye" joga contra o vencedor do 2º jogo das quartas.
            // O 2º "bye" joga contra o vencedor do 1° jogo.
            jogosDaSemifinal.add(criarJogoMataMata(timesComBye.get(0), vencedoresDosJogos.get(1), "SEMIFINAL", null, 0));
            jogosDaSemifinal.add(criarJogoMataMata(timesComBye.get(1), vencedoresDosJogos.get(0), "SEMIFINAL", null, 1));
        } else { // Cenário de 8 equipas (4 vencedores)
            // Chaveamento: Vencedor do Jogo 1 vs. Vencedor do Jogo 2, e Vencedor do Jogo 3 vs. Vencedor do Jogo 4.
            jogosDaSemifinal.add(criarJogoMataMata(vencedoresDosJogos.get(0), vencedoresDosJogos.get(1), "SEMIFINAL", null, 0));
            jogosDaSemifinal.add(criarJogoMataMata(vencedoresDosJogos.get(2), vencedoresDosJogos.get(3), "SEMIFINAL", null, 1));
        }

        jogoRepository.saveAll(jogosDaSemifinal);
        System.out.println("Fase 'SEMIFINAL' gerada com sucesso!");
    }


    private List<Equipe> buscarTimesComBye(Long eventoId, Long esporteId) {
        // Refaz o ranking para descobrir quem eram os classificados
        List<Grupo> grupos = grupoRepository.findGruposByEventoAndEsporte(eventoId, esporteId);
        List<ClassificadoPair> pares = new ArrayList<>();
        for (Grupo grupo : grupos) {
            List<TimeClassificacaoDTO> classificacao = getTabelaDeClassificacao(grupo.getIdGrupo());
            if (classificacao.size() >= 2) {
                pares.add(new ClassificadoPair(classificacao.get(0), classificacao.get(1)));
            }
        }

        List<Equipe> timesComBye = new ArrayList<>();
        // Se eram 6 equipas (3 pares), significa que 2 tiveram bye
        if (pares.size() == 3) {
            // Ordena os pares para encontrar os dois melhores
            pares.sort(Comparator.comparing((ClassificadoPair par) -> par.getPrimeiroColocado().getPontos()).reversed()
                    .thenComparing((ClassificadoPair par) -> par.getPrimeiroColocado().getSaldoDeGols()).reversed()
                    .thenComparing((ClassificadoPair par) -> par.getPrimeiroColocado().getGolsPro()).reversed());

            // Primeiro, pegamos os IDs dos times com bye
            Long idBye1 = pares.get(0).getPrimeiroColocado().getEquipeId();
            Long idBye2 = pares.get(1).getPrimeiroColocado().getEquipeId();

            // Depois, buscamos as equipes completas no banco de dados
            Equipe equipeBye1 = equipeRepository.findById(idBye1)
                    .orElseThrow(() -> new EntityNotFoundException("Equipe com bye não encontrada: " + idBye1));
            Equipe equipeBye2 = equipeRepository.findById(idBye2)
                    .orElseThrow(() -> new EntityNotFoundException("Equipe com bye não encontrada: " + idBye2));

            timesComBye.add(equipeBye1);
            timesComBye.add(equipeBye2);
        }

        return timesComBye;
    }

    //Método da Fase Final
    @Override
    @Transactional
    public void gerarFinal(Long eventoId, Long esporteId) {
        String faseAnterior = "SEMIFINAL";
        System.out.println("Iniciando geração da FINAL...");

        // 1. Busca os jogos da semifinal que já terminaram
        List<Jogo> jogosDaSemifinal = jogoRepository.findAllByFaseAndStatusInOrderByIdJogoAsc(faseAnterior, List.of("FINALIZADO", "WO"));

        if (jogosDaSemifinal.size() < 2) {
            throw new IllegalStateException("Os dois jogos da semifinal precisam ser concluídos.");
        }

        // 2. Coleta os dois finalistas
        List<Equipe> finalistas = new ArrayList<>();
        for (Jogo jogo : jogosDaSemifinal) {
            finalistas.add(getVencedorDoJogo(jogo));
        }

        // 3. Monta o jogo da final
        List<Jogo> jogoFinal = new ArrayList<>();
        jogoFinal.add(criarJogoMataMata(finalistas.get(0), finalistas.get(1), "FINAL", null, 0));

        jogoRepository.saveAll(jogoFinal);
        System.out.println("Fase 'FINAL' gerada com sucesso!");
    }

    private Equipe getVencedorDoJogo(Jogo jogo) {
        if ("WO".equals(jogo.getStatus())) {
            return jogo.getEquipeVencedoraWo();
        }

        // Se o placar normal não for empate, retorna o vencedor normal
        if (!jogo.getPlacarEquipeA().equals(jogo.getPlacarEquipeB())) {
            return jogo.getPlacarEquipeA() > jogo.getPlacarEquipeB() ? jogo.getEquipeA() : jogo.getEquipeB();
        }

        // Se o placar normal foi empate, desempata pelos pênaltis
        if (jogo.getPenaltiPlacarA() != null && jogo.getPenaltiPlacarB() != null) {
            return jogo.getPenaltiPlacarA() > jogo.getPenaltiPlacarB() ? jogo.getEquipeA() : jogo.getEquipeB();
        }

        // Se chegou aqui, é um empate em fase de grupos ou um erro.
        // Como o método é usado no mata-mata, lançamos um erro.
        throw new IllegalStateException("Não foi possível determinar o vencedor do jogo de mata-mata: " + jogo.getIdJogo());
    }


    // ===================================================
    // NOVO MÉTODO ADICIONADO
    // ===================================================
    @Override
    @Transactional(readOnly = true)
    public List<GrupoDTO> listarGrupos(Long eventoId, Long esporteId) {
        List<Grupo> grupos = grupoRepository.findGruposByEventoAndEsporte(eventoId, esporteId);

        // Mapeia a lista de entidades Grupo para uma lista de GrupoDTO
        return grupos.stream().map(grupo -> {
            GrupoDTO dto = new GrupoDTO();
            dto.setIdGrupo(grupo.getIdGrupo());
            dto.setNome(grupo.getNome());
            return dto;
        }).collect(Collectors.toList());
    }


}