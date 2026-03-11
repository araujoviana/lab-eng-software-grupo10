# ZeloDesk — Requisitos do Sistema

## 1. Requisitos Funcionais (RF)

### RF01 – Autenticação de Usuários
O sistema deve permitir que usuários realizem login utilizando credenciais válidas (email e senha).

### RF02 – Controle de Acesso por Perfil
O sistema deve controlar o acesso às funcionalidades com base no perfil do usuário:
- Solicitante
- Triagem
- Executor

### RF03 – Abrir Ticket de Zeladoria
O sistema deve permitir que um solicitante registre um novo ticket de manutenção ou zeladoria.

### RF04 – Informar Categoria do Ticket
O sistema deve permitir selecionar uma categoria para classificar o ticket.

### RF05 – Informar Localização do Problema
O sistema deve permitir indicar o local onde o problema ocorreu.

### RF06 – Anexar Arquivos ao Ticket
O sistema deve permitir anexar arquivos (como imagens) ao ticket.

### RF07 – Visualizar Tickets
O sistema deve permitir que usuários visualizem tickets cadastrados.

### RF08 – Visualizar Detalhes do Ticket
O sistema deve permitir visualizar todas as informações associadas a um ticket.

### RF09 – Registrar Histórico do Ticket
O sistema deve registrar alterações realizadas nos tickets em um log de auditoria.

### RF10 – Classificar Ticket na Triagem
O sistema deve permitir que um usuário responsável pela triagem classifique os tickets recebidos.

### RF11 – Definir Prioridade do Ticket
O sistema deve permitir definir o nível de prioridade de um ticket.

### RF12 – Atribuir Responsável
O sistema deve permitir atribuir um executor responsável pelo atendimento do ticket.

### RF13 – Atualizar Status do Ticket
O sistema deve permitir atualizar o status do ticket.

### RF14 – Adicionar Comentários
O sistema deve permitir adicionar comentários ao ticket durante o atendimento.

### RF15 – Registrar Evidências de Atendimento
O sistema deve permitir anexar arquivos como evidência da execução do serviço.

### RF16 – Concluir Ticket
O sistema deve permitir que o executor marque o ticket como concluído.

### RF17 – Acompanhar Andamento do Ticket
O solicitante deve poder acompanhar o andamento do ticket aberto.

### RF18 – Filtrar Tickets
O sistema deve permitir filtrar tickets por:
- status
- categoria
- localização
- período

---

# 2. Requisitos Não Funcionais (RNF)

## Segurança

### RNF01 – Controle de Acesso
O sistema deve garantir que usuários só possam acessar funcionalidades permitidas para seu perfil.

### RNF02 – Proteção de Credenciais
O sistema deve armazenar credenciais de forma segura utilizando criptografia.

### RNF03 – Registro de Auditoria
O sistema deve manter registro das alterações realizadas nos tickets.

---

## Usabilidade

### RNF04 – Interface Responsiva
O sistema deve funcionar corretamente em dispositivos móveis, tablets e computadores.

### RNF05 – Facilidade de Uso
O sistema deve permitir abertura de ticket em poucos passos.

---

## Desempenho

### RNF06 – Tempo de Resposta
O sistema deve responder às requisições em até 3 segundos em condições normais de uso.

---

## Confiabilidade

### RNF07 – Persistência de Dados
O sistema deve garantir que os dados dos tickets sejam armazenados de forma segura.

### RNF08 – Histórico Completo
O sistema deve manter o histórico completo de alterações de cada ticket.

---

## Arquitetura

### RNF09 – API
O sistema deve disponibilizar uma API para comunicação entre front-end e back-end.

### RNF10 – Banco de Dados
O sistema deve utilizar um banco de dados para armazenamento das informações do sistema.

---

# 3. Rastreabilidade entre Casos de Uso e Requisitos

| Caso de Uso | Requisitos Relacionados |
|---|---|
| UC-01 – Abrir ticket de zeladoria | RF03, RF04, RF05, RF06, RF09 |
| UC-02 – Realizar triagem e atribuir responsável | RF10, RF11, RF12, RF13 |
| UC-03 – Executar atendimento e concluir ticket | RF13, RF14, RF15, RF16 |
