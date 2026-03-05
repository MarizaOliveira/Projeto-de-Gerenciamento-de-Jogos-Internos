# Projeto de Gerenciamento de Jogos Internos

Uma API RESTful desenvolvida em Java com Spring Boot para gerir torneios desportivos internos (como competições intercursos ou intercampus universitários). O sistema automatiza a criação de equipas, a geração de chaves de torneio (fases de grupos e mata-mata) e a gestão de resultados, aplicando regras de negócio rigorosas para garantir a integridade da competição.

## 🚀 Tecnologias Utilizadas

* **Linguagem:** Java 21
* **Framework:** Spring Boot 3.5.3 (Web, Data JPA)
* **Banco de Dados:** PostgreSQL
* **Utilitários:** Lombok, Maven Wrapper (`mvnw`)

## ⚙️ Principais Funcionalidades

### 1. Gestão de Perfis de Utilizador
O sistema divide as permissões em três categorias principais:
* **ATLETA:** Pode ser inscrito em equipas.
* **COORDENADOR:** Responsável por gerir o seu curso, tendo a autoridade exclusiva para habilitar ou desabilitar alunos (Atletas) como "Técnicos".
* **ÁRBITRO:** O único com permissão para registar ou alterar os resultados das partidas.

### 2. Gestão Académica e Estrutural
* **Campus e Cursos:** CRUD completo para mapear a estrutura da instituição. Um curso pertence a um campus e possui um Coordenador responsável.
* **Eventos e Esportes:** Criação de eventos desportivos (ex: "Jogos Internos 2024") e modalidades (ex: "Futsal", "Voleibol"), definindo o número mínimo e máximo de atletas por modalidade.

### 3. Criação e Validação de Equipas
As regras de formação de equipas são rigorosas e validadas pelo sistema:
* **Técnico Obrigatório:** Uma equipa só pode ser criada por um aluno habilitado como "Técnico" pelo Coordenador do seu respetivo curso.
* **Filtro por Curso:** Todos os atletas de uma equipa devem, obrigatoriamente, pertencer ao mesmo curso.
* **Limites da Modalidade:** A equipa deve respeitar o número mínimo e máximo de jogadores estipulado para o desporto em questão.

### 4. Motor Automático de Torneios
O sistema possui um algoritmo dinâmico (`TorneioService`) que organiza a competição sem intervenção manual:
* **Sorteio e Fase de Grupos:** Distribui as equipas inscritas de forma aleatória em grupos otimizados de 3, 4 ou 5 equipas. Gera automaticamente todos os jogos no formato "todos-contra-todos" dentro do grupo.
* **Tabela de Classificação em Tempo Real:** Calcula automaticamente a pontuação (Vitória = 3, Empate = 1, Derrota = 0), saldo de golos e golos pró, ordenando a tabela com critérios de desempate.
* **Mata-Mata Inteligente:** Gera automaticamente os emparelhamentos para os Quartos de Final, Semifinais e Final com base nos classificados. O sistema suporta cenários complexos, como atribuição de "Byes" (avanço direto) quando passam apenas 6 equipas para a fase eliminatória.

### 5. Gestão de Jogos e Resultados
* **Placar Normal e Penáltis:** Registo de resultados com suporte a penáltis (obrigatório em caso de empate nas fases de mata-mata).
* **Gestão de W.O.:** Permite declarar vitória por falta de comparência (W.O.) e atribuir os pontos corretamente.
* **Auditoria de Árbitros:** Apenas utilizadores com o perfil de Árbitro podem submeter placares ou desfazer um W.O. registado indevidamente.

---

## 🛠️ Como Executar o Projeto Localmente

### Pré-requisitos
* Java 21 instalado na máquina.
* PostgreSQL instalado e a correr no seu ambiente local.

### Configuração do Banco de Dados
Crie uma base de dados no seu PostgreSQL chamada `bd_jogos_internos_v2`. As credenciais padrão definidas no ficheiro `application.properties` são:
* **URL:** `jdbc:postgresql://localhost:5432/bd_jogos_internos_v2`
* **Username:** `postgres`
* **Password:** `****`

*(Caso utilize um utilizador ou senha diferentes, altere estas propriedades no ficheiro `src/main/resources/application.properties` antes de arrancar a aplicação).*

### Arrancar a Aplicação
1. Clone o repositório para a sua máquina:
   ```bash
   git clone https://github.com/MarizaOliveira/Projeto-de-Gerenciamento-de-Jogos-Internos.git